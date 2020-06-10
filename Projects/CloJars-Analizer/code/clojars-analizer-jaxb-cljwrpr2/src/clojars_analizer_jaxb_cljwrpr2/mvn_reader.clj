
(ns clojars-analizer-jaxb-cljwrpr2.mvn-reader
  (:require [clojure.java.data :as jd]
            [clojure.data.xml :refer [parse-str]])
  (:import [clojarsAnalizer.core.model.packageDescriptor.maven Model MVNReader]
           [java.io File]))

;; Wraps the MVNReader Java Library into an idiomatic Clojure API

(defn convert-java-to-clj [^Model java-pom]
  (println "entering clojars-analizer-jaxb-cljwrpr2.core/convert-java-to-clj")
  (println (str "model value is: " java-pom))
  (let [converted-pom (jd/from-java java-pom)]
    (println (str "converted-pom: " converted-pom))
    converted-pom))


(defn read-pom-file [pom-content]
  ;; Is a valid xml-doc as string...
  ; (if (and
  ;       (instance? String pom-content)
  ;       (parse-str pom-content :validating true))
  ;   (println "reading as content-str")
  ;   ())
  ;; Is a valid file-path string...)
  (if (and (instance? String pom-content)
           (.exists (File. pom-content)))
    (do
      (println "reading as file-path")
      (let [mvn-reader (MVNReader.)
            pom-model (.unmarJaxbProjectFile mvn-reader pom-content)
            ; clj-model (convert-java-to-clj pom-model)
            ]
        ; clj-model
        pom-model
        ))))
