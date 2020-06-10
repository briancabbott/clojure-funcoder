(ns clojars-analizer.downloader
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.core.async :refer [go chan >! close! <!! <! promise-chan]]
            [clojure.data.xml :refer :all]
            ; [clojars-analizer.core :refer [operation-timestamp]]
            [clojars-analizer.http-async :as http-async])
  (:import [java.util Date] [java.lang Runtime StringBuilder] [java.io StringReader BufferedReader])
  (:gen-class))

(def CLOJARS-POMS-LIST-URL "https://repo.clojars.org/all-poms.txt")
(def dest-pomfile-name (atom nil))

(def operation-timestamp (atom nil))
(defn set-operation-timestamp [ts]
  (reset! operation-timestamp ts))

(defn clean-line-breaks [str]
  (let [sr (StringReader. str)
        reader (BufferedReader. sr)
        buf (StringBuilder. )
        NL (System/getProperty "line.separator" "\r\n")]
    (loop [line (.readLine reader)]
      (if (not (nil? line))
        (if (not (.isEmpty (.trim line)))
          (do
            (.append buf line)
            (.append buf NL)))
        (recur (.readLine reader))))
    (.toString buf)))

(defn ppxml [xml]
  (let [in (javax.xml.transform.stream.StreamSource.
            (java.io.StringReader. xml))
        writer (java.io.StringWriter.)
        out (javax.xml.transform.stream.StreamResult. writer)
        transformer (.newTransformer
                     (javax.xml.transform.TransformerFactory/newInstance))]
    (.setOutputProperty transformer
                        javax.xml.transform.OutputKeys/INDENT "yes")
    (.setOutputProperty transformer
                        "{http://xml.apache.org/xslt}indent-amount" "2")
    (.setOutputProperty transformer
                        javax.xml.transform.OutputKeys/METHOD "xml")
    (.setOutputProperty transformer
                        javax.xml.transform.OutputKeys/ENCODING "UTF-16")
    (.transform transformer in out)
    (-> out .getWriter .toString)))

(defn gen-pomslist-name []
  (if (nil? @dest-pomfile-name)
    (reset! dest-pomfile-name (str "./clojars-all-poms-" @operation-timestamp)))
  @dest-pomfile-name)

(defn gen-dest-pomfile-name [pom-url]
  (let [pomfile (last (str/split pom-url #"/"))]
    ; (println (str "dest-pomfile: " pomfile))
    pomfile))

(defn refresh-poms-list []
  (with-open [in (io/input-stream CLOJARS-POMS-LIST-URL)
              out (io/output-stream (gen-pomslist-name ))]
    (io/copy in out)))

(defn capture-pom-files []
  (let [channel-response-map (atom {})
        download-retries-map (atom {})
        pom-files (str/split-lines (slurp (gen-pomslist-name)))
        pom-folder (str "./pom-files-" @operation-timestamp)
        pom-blocks (partition 20000 pom-files)]
    ;; make the destination directory
    (.mkdir (java.io.File. pom-folder))

    ;; start the file-download operations
    (doall
      (for [b pom-blocks] (do
        (doall (for [f b]
          (do
            ; (println (.freeMemory (Runtime/getRuntime)))
            ; (println (.maxMemory (Runtime/getRuntime)))
            ; (println (.totalMemory (Runtime/getRuntime)))
            (let [pom-url (str "https://repo.clojars.org/" (str/replace-first f #"./" ""))
                  pom-file (str pom-folder "/" (gen-dest-pomfile-name f))
                  resp-chan (http-async/async-http-req> {:url pom-url :print-errors true})]
              (swap! channel-response-map assoc (keyword pom-url) {:pom-url pom-url :pom-file pom-file :response-channel resp-chan})))))

        ;; receive and save the results
        (doall
          (for [url-key (keys @channel-response-map)]
            (do
              (let [resp-data (url-key @channel-response-map)
                    channel (:response-channel resp-data)
                    resp-value (<!! (go (<! channel)))]
                (if (not (nil? resp-value))
                  (do
                    (try
                      (if (not (nil? (:body resp-value)))
                        (let [xml-body (parse-str (:body resp-value))]
                          (println (str "writing file: " (:pom-file resp-data)))
                          (spit (:pom-file resp-data) (emit-str xml-body)))
                        (prn "content of pom-xml was nil for file: " (:pom-file resp-data)))
                      (catch Exception ex (prn ex))))
                  (do
                    (swap! download-retries-map assoc url-key resp-data))))))))))))
