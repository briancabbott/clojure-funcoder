(ns mbox-extractor-client.core
  (:require [mbox-extractor.core :refer :all]
            [mbox-extractor.message-parser :as mp]

            [rx.lang.clojure.interop :as rx])
  (:import rx.Observable rx.subscriptions.Subscriptions java.io.FileReader java.io.BufferedReader)
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (-> (extractor-via-observable "/Volumes/data-store/takeout/Takeout/Mail/All mail Including Spam and Trash.mbox")
    (.subscribe (rx/action [v]
      ; (prn v)
      ; (println (get v :sequence-number))
      ; (println (get v :raw-text))
      (prn "starting parse-message")
      (mp/parse-message v)
      (prn "out of parsee-message")
      ))))
