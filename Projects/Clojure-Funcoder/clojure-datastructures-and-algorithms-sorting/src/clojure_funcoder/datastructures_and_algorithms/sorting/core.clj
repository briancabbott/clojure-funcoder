(ns clojure-funcoder.datastructures-and-algorithms.sorting.core
  (:require [cli-matic.core :refer [run-cmd]])
  (:gen-class))

(defn run-configuration [& args]
  (prn args))

(def CLI-CONFIGURATION {:app         {:command     "sorting"
                                      :description ""
                                      :version     "0.0.1"}
                        :global-opts []
                        :commands    [{:command     "list-datasets" :short "ld"
                                       :description ["Lists the currently available data-sets."]
                                       :opts        []
                                       :runs        run-configuration}
                                      {:command     "bubble-sort" :short "bb"
                                       :description ["Executes a bubble-sort operation on a selected data-set."]
                                       :opts        []
                                       :runs        run-configuration}
                                      {:command     "bucket-sort" :short "bc"
                                       :description ["Executes a bucket-sort operation on a selected data-set."]
                                       :opts        []
                                       :runs        run-configuration}
                                      {:command     "column-sort" :short "cs"
                                       :description ["Executes a column-sort operation on a selected data-set."]
                                       :opts        []
                                       :runs        run-configuration}
                                      {:command     "heap-sort" :short "hs"
                                       :description ["Executes a heap-sort operation on a selected data-set."]
                                       :opts        []
                                       :runs        run-configuration}
                                      {:command     "insertaion-sort" :short "is"
                                       :description ["Executes a insertion-sort operation on a selected data-set."]
                                       :opts        []
                                       :runs        run-configuration}
                                      {:command     "merge-sort" :short "ms"
                                       :description ["Executes a quick-sort operation on a selected data-set."]
                                       :opts        []
                                       :runs        run-configuration}
                                      {:command     "quick-sort" :short "qs"
                                       :description [""]
                                       :opts        []
                                       :runs        run-configuration}
                                      {:command     "radix-sort" :short "rs"
                                       :description ["Executes a radix-sort operation on a selected data-set."]
                                       :opts        []
                                       :runs        run-configuration}]})

(defn -main
  [& args]
  (run-cmd args CLI-CONFIGURATION))
