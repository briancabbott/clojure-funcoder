(ns clojars-analizer-jaxb-cljwrpr2.core
  (:require [clojars-analizer-jaxb-cljwrpr2.mvn-reader :refer [read-pom-file convert-java-to-clj]]
             [clojure.pprint :as pp])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (str "objres: "))
  (let [pom (read-pom-file "./resources/analytics-1.0.1.pom")
        pom-clj (convert-java-to-clj pom)]
    (pp/pprint pom-clj)))
