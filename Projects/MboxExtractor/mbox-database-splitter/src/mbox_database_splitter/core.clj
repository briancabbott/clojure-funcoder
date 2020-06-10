(ns mbox-database-splitter.core
  (:require [cli-matic.core :refer [run-cmd]])
  (:gen-class))

(defprotocol SplitOperationConfiguration
  (split-strategy [this])
  (mbox-file [this])
  (dest-dir [this]))

(defn setup-split-operation [& args]
  (prn args))


(def CLI-CONFIGURATION
  {:app         {:command     "mbox-splitter"
                 :description "This is a simple command line utility that's installed alongside the other
                               MBOX-Extractor Apps to perform a simple split operation, most likely from the
                               single monolithic .mbox file to single files for each message (the default split
                               configuration) in a directory specified by the user's choosing. However, other
                               split-strategies will be supported as well."
                 :version     "0.0.1"}
   :global-opts []
   :commands    [{:command     "split" :short "s"
                  :description ["Splits the specified MBOX file."]
                  :opts        [
                                 ;; "split-strategy" options:
                                 ;;     - By Size (every so many MB, split and create a new file)
                                 ;;     - By Query (execute a series of queries and for each set of positive hits to each query, create a unique file)
                                 ;;         - this could be combined with by-size so that within the query response files, an enumeration around a given size constraint also exists.
                                 ;;     - By Count - just limit to so many per file. The SINGLE-ENTRY-SINGLE-FILE is done through this: --by-count {count: 1}
                                {:option "split-strategy" :short "ss"
                                 :as "Split-Strategy" :type #{:by-size :by-query :by-count :single-msg}
                                 :default :by-count }

                                {:option "mbox-file" :short "mf"
                                 :as "The source MBOX file to process" :type :string}

                                {:option "dest-dir" :short "dd"
                                 :as "Destination Directory" :type :string}]
                  :runs        setup-split-operation}]})

(defn -main
  [& args]
  (run-cmd args CLI-CONFIGURATION))
