(defproject clojure-funcoder.clojars-analizer.jaxb-cljwrpr2 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/java.data "0.1.1"]
                 [org.clojure/data.xml "0.2.0-alpha6"]
                 [clojure-funcoder.CloJars-Analizer/clojars-analizer-jaxb "1.0-SNAPSHOT"]]

  :main ^:skip-aot clojars-analizer-jaxb-cljwrpr2.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
