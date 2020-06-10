(ns mbox-extractor.extractor-via-observables-test
  (:require [clojure.test :refer :all]
            [mbox-extractor.core :refer :all]
            [rx.lang.clojure.interop :as rx])
  (:import rx.Observable rx.subscriptions.Subscriptions java.io.FileReader java.io.BufferedReader))



; (defn basic-observable-test []
;
;   )


  (deftest basic-observable-test
    (testing "Basic observable...."
      ; (-> (fetchWikipediaArticleAsynchronously ["Tiger" "Elephant"])
      ;   (.subscribe (rx/action [v] (println "--- Article ---\n" (subs (:body v) 0 125) "...")))
      ;
      (-> (extractor-via-observable "/Volumes/data-store/takeout/Takeout/Mail/All mail Including Spam and Trash.mbox")
        (.subscribe (rx/action [v] (println v))))))
