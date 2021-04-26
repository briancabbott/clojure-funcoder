(defproject clojure-funcoder-datastructures-and-algorithms-sorting "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [cli-matic "0.4.3"]]
  :plugins [[lein-cljfmt "0.6.7"]]

  :main ^:skip-aot clojure-funcoder.datastructures-and-algorithms.sorting.core

  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}})