(ns scripts2.core
  (:require [clojure.tools.namespace :as tn]
            [clojure.string :as st])
  (:import [java.util.jar JarFile])
  (:gen-class))

  (defn -main
    [& args]
    (let [file-paths ["/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/algo.generic/target/algo.generic-0.1.4-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/algo.monads/target/algo.monads-0.1.7-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/clojure/target/clojure-1.11.0-master-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.async/target/core.async-0.4.491-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.cache/target/core.cache-0.7.3-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.contracts/target/core.contracts-0.0.7-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.logic/target/core.logic-0.8.12-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.match/target/core.match-0.3.1-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.memoize/target/core.memoize-0.7.2-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.rrb-vector/target/core.rrb-vector-0.0.15-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.specs.alpha/target/core.specs.alpha-0.2.45-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.typed/module-rt/target/core.typed.rt-0.6.1-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.typed/module-infer/target/core.typed.infer-0.6.1-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.typed/module-analyzer-jvm/target/core.typed.analyzer.jvm-0.6.1-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.typed/module-check/target/core.typed-0.6.1-SNAPSHOT-slim.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.typed.checker.jvm/target/core.typed.checker.jvm-0.7.2-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.typed.runtime.jvm/target/core.typed.runtime.jvm-0.7.2-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/core.unify/target/core.unify-0.5.8-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/data.avl/target/data.avl-0.0.19-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/data.codec/target/data.codec-0.1.2-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/data.csv/target/data.csv-0.1.5-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/data.finger-tree/target/data.finger-tree-0.0.4-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/data.fressian/target/data.fressian-0.2.2-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/data.generators/target/data.generators-0.1.3-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/data.int-map/target/data.int-map-0.2.5-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/data.json/target/data.json-0.2.7-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/data.priority-map/target/data.priority-map-0.0.11-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/data.xml/target/data.xml-0.2.0-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/data.zip/target/data.zip-0.1.4-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/java.classpath/target/java.classpath-0.3.1-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/java.data/target/java.data-0.1.2-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/java.jdbc/target/java.jdbc-0.7.10-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/java.jmx/target/java.jmx-0.3.5-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/jvm.tools.analyzer/target/jvm.tools.analyzer-0.6.3-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/math.combinatorics/target/math.combinatorics-0.1.6-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/math.numeric-tower/target/math.numeric-tower-0.0.5-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/spec-alpha2/target/spec-alpha2-0.2.177-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/spec.alpha/target/spec.alpha-0.2.177-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/test.check/target/test.check-0.10.0-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/test.generative/target/test.generative-0.5.3-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/tools.analyzer/target/tools.analyzer-0.7.1-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/tools.analyzer.jvm/target/tools.analyzer.jvm-0.7.3-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/tools.cli/target/tools.cli-0.4.3-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/tools.deps.alpha/target/tools.deps.alpha-0.6.497-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/tools.emitter.jvm/target/tools.emitter.jvm-0.1.0-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/tools.gitlibs/target/tools.gitlibs-0.2.65-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/tools.logging/target/tools.logging-0.5.0-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/tools.macro/target/tools.macro-0.1.6-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/tools.namespace/target/tools.namespace-0.3.0-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/tools.reader/target/tools.reader-1.3.3-SNAPSHOT.jar"
                      "/Volumes/data-store/dev_space/clojure-land/__CLOJURE-LAND-META/clojure.org-projects/tools.trace/target/tools.trace-0.7.11-SNAPSHOT.jar"]
          jar-files (map (fn [file-str] (JarFile. file-str)) file-paths)
          ns-map (map (fn [jar]
                        (str
                          (str "\nJARFILE: " (.getName jar) "\n")
                          (st/join "\n" (sort (tn/find-namespaces-in-jarfile jar))))) jar-files)

          ]
      (println ns-map)))
