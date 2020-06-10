(ns scripts.print-artifacts
  (:require [clojure.tools.namespace :as tn])
  (:import [java.util.jar JarFile]))

(defn -main [& args]
  (let [file-paths ["~/.m2/repository/org/clojure/core.typed/0.6.0/core.typed-0.6.0.jar"
                    "~/.m2/repository/org/clojure/core.typed.analyzer.jvm/0.6.0/core.typed.analyzer.jvm-0.6.0.jar"
                    "~/.m2/repository/org/clojure/core.typed.infer/0.6.0/core.typed.infer-0.6.0.jar"
                    "~/.m2/repository/org/clojure/core.typed.rt/0.6.0/core.typed.rt-0.6.0.jar"]
        jar-files (map (fn [file-str] (JarFile . file-str)) file-paths)
        ns-map (map (fn [jar]
                      (st/join "\n" (sort (tn/find-namespaces-in-jarfile jar)))) jar-files)

        ]
    (println ns-map)))
