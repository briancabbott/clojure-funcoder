(ns mbox-extractor.message-parser
  (:require [clojure.string :as str])
  (:import java.net.URL))


(defn is-url [str]
  (try
    (URL. str)
    true
    (catch Exception ex
      ; (prn ex)
      ))
  false
    )

(defn line-begins-with-label? [line]
  ; (println "")
  ; (println "")
  ; (println "")
  ; (println "")
  ; (println "")
  ; (println "")
  ; (println "")
  ; (println "")
  ; (println "")
  ; (prn (str "Line: " line))
  (if (or
        (str/blank? line)
        (not (str/includes? line ":"))
        (is-url line))
      false
    (when-let [elements (str/split line #":")]
      (when-let [first-label (first elements)]
        (let [starts-with-space (re-find (re-pattern #"^\s+") first-label)
              contains-a-space (re-find (re-pattern #"\s+") first-label)
              ret-val (and (nil? starts-with-space)
                           (nil? contains-a-space))]
          ; (println (str "elements: " elements))
          ; (println (str "first-label: " first-label))
          ; (println (str "starts-with-space: " starts-with-space))
          ; (println (str "contains-a-space: " contains-a-space))
          ; (println (str "ret-val: " ret-val))
          ret-val
          )))))

(defn line-begins-with-body-indentation? [line]
  (let [re-find-res (re-find (re-pattern #"^\s+") line)]
    (if (not (nil? re-find-res))
      true
      false)))

; (defn list-attribute-labels [email-message]
;   (let [sorted-attribs (do (if (instanceof? String email-message)
;                          (do
;                             (let [parsed-message (parse-message email-message)
;                                   sorted-header-attribs (sory-by (get-seq-num
;                                                                    (get-header-attributes parsed-message)))]
;                               sorted-header-attribs)))
;                         (if (instanceof? EmailMessage email-message)
;                           (sory-by (get-seq-num (get-header-attributes parsed-message)))))]
;     (mapv (fn [attr] (get-label attr)) sorted-attribs)))

(defn parse-message [mbox-msg]
  ;; Split line on first colon, everything prior to the colon contains no spaces - then its a likely valid header-label
  ;;    - Verify this, find the RFC that drives this formatting. (I think its 3676?)
  (let [msg-txt (get mbox-msg :raw-text)
        list (str/split-lines msg-txt)
        labelled-headers (atom [])
        current-label (atom {:label "" :value []})]
    (doall
      (for [el list]
        (try

            (if (line-begins-with-label? el)
              (do
                (if (not (str/blank? (get @current-label :label)))
                  (do
                    (swap! labelled-headers conj @current-label)
                    (reset! current-label {:label "" :value []})))
                (let [label-val (str/split el #":")
                      label (first label-val)]
                  ; rest-of-str (.replaceFirst el label "")]
                  (swap! current-label assoc :label label)
                  (println (str "label-val: " label-val))
                  (println (str "label: " label))
                  (let [rest-of-str (.replaceFirst el label "")]
                    (swap! current-label assoc :value (conj (get current-label :value) rest-of-str))))))


            (if (line-begins-with-body-indentation? el)
              (swap! current-label assoc :value (conj (get current-label :value) el)))
          (catch Exception ex (prn ex)))))

    ;; Parse out the mime-data
    ;;   ----==_mimepart
    (let [pattern (re-pattern "----==_mimepart")
          matcher (.matcher pattern msg-text)]
      (if (.matches matcher)
        (prn "FOUND A MATCH FOR MIME DAT")
        (prn (str "number of groups found: " (.groupCount matcher)))))

    (println "Labelled-Headers for Message:")
    (println @labelled-headers)))
