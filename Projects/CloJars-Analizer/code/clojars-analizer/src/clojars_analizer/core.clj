(ns clojars-analizer.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.core.async :refer [go chan >! close! <!! <! promise-chan]]
            [clojure.data.xml :refer :all]
            [clojars-analizer.http-async :as http-async]
            [clojars-analizer.downloader :as downloader])
  (:import [java.util Date] [java.lang Runtime StringBuilder] [java.io StringReader BufferedReader])
  (:gen-class))


(def operation-timestamp (atom nil))

(defn set-operation-timestamp []
  (reset! operation-timestamp (.getTime (Date.))))

(defn -main
  [& args]
  (set-operation-timestamp )
  (downloader/set-operation-timestamp @operation-timestamp)

  (println "starting all-poms download")
  (downloader/refresh-poms-list )
  (println "all poms download finished")

  (downloader/capture-pom-files ))
