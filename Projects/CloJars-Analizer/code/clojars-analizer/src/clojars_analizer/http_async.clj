(ns clojars-analizer.http-async
  [:require
    [clojure.core.async :refer [go chan >! close! promise-chan]]
    [clojure.string :refer [upper-case]]
    [org.httpkit.client :as httpkit]
    ; [camelsnake.core :refer [->camelCase ->kebab-case-keyword]]
  ])

;; ----------------------------------------------------------------------------
; Usage Notes:
;
; To setup a series of requests ready to be received on asynchronous
; channels, you can do something like the following block of code:
;
; [:require xhealth-web.utils.http-async-lib :as http-async]
;
; (def url-resp-map (atom {})) ; hashmap to contain the req handles.
;
; (for [url ts-urls]
;   (do
;     (try
;       (let [resp-chan (http-async/async-http-req> {:url url})]
;         (swap! url-resp-map assoc (keyword url) resp-chan))
;       (catch Exception e (prn (str "Caught Exception: " (.getMessage e)))))
;       )))
;
; Then to receive the results, listen for the channel responses in a seperate sequence/code block:
;
; (doall
; (for [k (keys @url-resp-map)]
;   (let [resp-val (<!! (go (<! (k @url-resp-map))))]
;     (condp = (type resp-val)
;       ;; Handle response based on data type or,
;     )))
;

(def connection-error-status 503)

; (defn logger [status]
;   (-> (str "http-async: " status)
;       (org.slf4j.LoggerFactory/getLogger)))
;
; (defmacro log-error [status & message]
;   `(-> (logger ~status)
;        (.error (print-str ~@message))))
;
; (defmacro log-warn [status & message]
;   `(-> (logger ~status)
;        (.warn (print-str ~@message))))
;
; (defmacro log-debug [status & message]
;   `(-> (logger ~status)
;        (.debug (print-str ~@message))))


;;; REQUEST / RESPONSE HANDLING

(defn json-body? [body]
  (and body (or (map? body) (sequential? body))))

(defn- request-body
  [body & {:keys [json-key-fn] }] ; :or {json-key-fn ->camelCase}}]
  ; (if (json-body? body)
  ;   (write-json body :json-key-fn json-key-fn)
  ;   body))
  body)

(defn- request-headers [body headers]
  headers)
  ; (if (json-body? body)
  ;   (update headers "Content-Type" #(or % "application/json"))
  ;   headers))

(defn- process-error-response
  [full-url status body cause]
  (let [status (if cause connection-error-status status)
        body (if cause (str cause) body)
        message (str "Error requesting " full-url ": "
                     (if cause
                       (str "Connection error " (str cause))
                       (str "HTTP Error " status)))
        ex (ex-info message {:status status, :body body} cause)]
    ; (if (>= status 500)
    ;   (log-error status message)
    ;   (log-warn status message))
    ex))

(defn- process-response
  [req full-url result-channel response-parser print-errors?
   {:keys [opts status headers body error] :as res}]
  (go
    (try
      (->> (if (or error (> status 399))
             (process-error-response full-url status body error)
             (let [res (if response-parser
                         (response-parser res)
                         res)]
               ; (log-debug status
               ;            "Response " full-url
               ;            "status:" status
               ;            (when body (str "body:" body))
               ;            "headers:" headers)
               res))
           (>! result-channel))
      (catch Exception e
        ; (log/error e "Error parsing response")
        (if print-errors?
          (>! result-channel (ex-info (str "Error parsing response: " e) {:status 500} e))
          (>! result-channel "not printing error"))))
    (close! result-channel)))

(defn create-json-response-parser
  ([json-key-fn]
   (create-json-response-parser json-key-fn []))
  ([json-key-fn preserve-keys]
   (fn [{:keys [opts status headers body] :as res}]
     ; (cond
     ;   (= :head (:method opts))
     ;   headers
     ;   (> status 299)
     ;   res  ; 30x status - return response as is
     ;   (and (not= status 204)  ; has content
     ;        (.startsWith (:content-type headers "") "application/json"))
     ;   (or (read-json body
     ;                  :json-key-fn json-key-fn
     ;                  :preserve-keys preserve-keys)
     ;       {})
     ;   :else
     ;   (or body ""))
     res
     )))


; (def kebab-case-json-response-parser
;   (create-json-response-parser ->kebab-case-keyword))

(defn async-http-req>
  "Performs asynchronous API request. Always returns result channel which will
  return either response or exception."
  [{:keys [base-url resource url method params body headers basic-auth
           timeout form-params body-json-key-fn response-parser oauth-token
           follow-redirects? as files out-chan print-errors]
    :or {method :get
         ; body-json-key-fn ->camelCase
         response-parser (create-json-response-parser nil nil)  ; kebab-case-json-response-parser
         follow-redirects? true
         as :auto}}]
  ; {:pre [(or url (and base-url resource))]}
  (let [req {:url (or url (str base-url "/" resource))
             :method method
             :body body ; (request-body body :json-key-fn body-json-key-fn)
             :query-params params
             :headers headers ; (request-headers body headers)
             :multipart files
             :form-params form-params
             :basic-auth basic-auth
             :oauth-token oauth-token
             :timeout (* 30 1000) ; (* (or timeout @http-timeout) 1000)
             :follow-redirects follow-redirects?
             :as as}
        full-url (str (upper-case (name method))
                      " " (:url req)
                      ; (if (not-empty (:query-params req))
                      ;   (str "?" (query-string (:query-params req))) "")
                        )
        result-channel (or out-chan (promise-chan))]
    ; (log/debug "Request" full-url
    ;            (if-let [body (:body req)] (str "body:" body) "")
    ;            (if-let [headers (:headers req)] (str "headers:" headers) ""))
    (httpkit/request req (partial process-response
                                  req
                                  full-url
                                  result-channel
                                  response-parser
                                  print-errors
                                  ))
    result-channel))

;; Takes a URL-Collection and returns a map of URL -> response-value
; (defn make-reqs [urls]
;   )