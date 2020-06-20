(ns clojure-funcoder.datastructures-and-algorithms.sorting.insertion-sort

  )


;; Insertion-sort
;; for j = 2 to A.length
;;     key = A[j]
;;     // Insert A[j] into the sorted sequence A[1..j-1]
;;     i = j - 1
;;     while i > 0 and A[i] > key
;;         A[i + 1] = A[i]
;;         i = i - 1
;;     A[i + 1] = key

(defn insertion-sort [a-seq]
  (let [starting-idx 1]
    (loop [j = starting-idx]
      (let [key (get a-seq j)

            ]
        (println (str "j is: " j))
        (loop [i (- j 1)]
          (if (and
                (> i 0)
                (> (get a-seq i) key))
            (recur (dec i))
            ))
        ;; Recursive conditional - maintain non-computational...
        (if (< j (count a-seq))
          (do
            (println "doing recur")
            (recur (inc j))))))))
