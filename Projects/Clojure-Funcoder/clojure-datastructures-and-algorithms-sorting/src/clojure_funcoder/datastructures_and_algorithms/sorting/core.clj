(ns clojure-funcoder.datastructures-and-algorithms.sorting.core
  (:require [cli-matic.core :refer [run-cmd]])
  (:gen-class))

(defn run-list-datasets [& args]
  (prn args))

(defn run-bubble-sort [& args]
  (prn args))

(defn run-bucket-sort [& args]
  (prn args))

(defn run-column-sort [& args]
  (prn args))

(defn run-heap-sort [& args]
  (prn args))

(defn run-insertion-sort [& args]
  (prn args))

(defn run-merge-sort [& args]
  (prn args))

(defn run-quick-sort [& args]
  (prn args))

(defn run-radix-sort [& args]
  (prn args))

(def CLI-CONFIGURATION {:app         {:command      "sorting"
                                      :description  "execute a collection of standard computer-science sorting algorithms."
                                      :version      "0.0.1"}
                        :global-opts []

                                      ;; List Dataset Definitions
                        :commands    [{:command     "list-datasets"
                                       :short       "ld"
                                       :description ["Lists the currently available data-sets."]
                                       :opts        [{:option "data-set"
                                                      :as "select the data set you would like to run"
                                                      :type :string
                                                      :default "short-rand-list"}]
                                       :runs        run-list-datasets}

                                      ;; Bubble-Sort
                                      {:command     "bubble-sort"
                                       :short       "bb"
                                       :description ["Executes a bubble-sort operation on a selected data-set."]
                                       :opts        [{:option     "data-set"
                                                      :as         "Select the data set you would like to run"
                                                      :type       :string
                                                      :default    "short-rand-list"}]
                                       :runs        run-bubble-sort}

                                      ;; Bucket-Sort
                                      {:command     "bucket-sort"
                                       :short       "bc"
                                       :description ["Executes a bucket-sort operation on a selected data-set."]
                                       :opts        [{:option     "data-set"
                                                      :as         "Select the data set you would like to run"
                                                      :type       :string
                                                      :default    "short-rand-list"}]
                                       :runs        run-bucket-sort}

                                      ;; Column-Sort
                                      {:command     "column-sort"
                                       :short       "cs"
                                       :description ["Executes a column-sort operation on a selected data-set."]
                                       :opts        [{:option     "data-set"
                                                      :as         "Select the data set you would like to run"
                                                      :type       :string
                                                      :default    "short-rand-list"}]
                                       :runs        run-column-sort}

                                      ;; Heap-Sort
                                      {:command     "heap-sort"
                                       :short       "hs"
                                       :description ["Executes a heap-sort operation on a selected data-set."]
                                       :opts        [{:option     "data-set"
                                                      :as         "Select the data set you would like to run"
                                                      :type       :string
                                                      :default    "short-rand-list"}]
                                       :runs        run-heap-sort}

                                      ;; Insertion-Sort
                                      {:command     "insertaion-sort"
                                       :short       "is"
                                       :description ["Executes a insertion-sort operation on a selected data-set."]
                                       :opts        [{:option     "data-set"
                                                      :as         "Select the data set you would like to run"
                                                      :type       :string
                                                      :default    "short-rand-list"}]
                                       :runs        run-insertion-sort}

                                      ;; Merge-Sort
                                      {:command     "merge-sort"
                                       :short       "ms"
                                       :description ["Executes a merge-sort operation on a selected data-set."]
                                       :opts        [{:option     "data-set"
                                                      :as         "Select the data set you would like to run"
                                                      :type       :string
                                                      :default    "short-rand-list"}]
                                       :runs        run-merge-sort}

                                      ;; Quick-Sort
                                      {:command     "quick-sort"
                                       :short       "qs"
                                       :description ["Executes a quick-sort operation on a selected data-set."]
                                       :opts        [{:option     "data-set"
                                                      :as         "Select the data set you would like to run"
                                                      :type       :string
                                                      :default    "short-rand-list"}]
                                       :runs        run-quick-sort}

                                      ;; Radix-Sort
                                      {:command     "radix-sort"
                                       :short       "rs"
                                       :description ["Executes a radix-sort operation on a selected data-set."]
                                       :opts        [{:option     "data-set"
                                                      :as         "Select the data set you would like to run"
                                                      :type       :string
                                                      :default    "short-rand-list"}]
                                       :runs        run-radix-sort}]})

(defn -main
  [& args]
  (run-cmd args CLI-CONFIGURATION))
