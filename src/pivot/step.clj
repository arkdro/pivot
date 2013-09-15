(ns pivot.step
  (:require pivot.misc)
  )

(defn find-entering-var [indexes values]
  (cond (empty? indexes) nil
        (empty? values) nil
        :default (let [joined (map #(vec [%1 %2]) indexes values)
                       pos (filter #(pos? (second %)) joined)]
                   (if (empty? pos) nil
                       (reduce #(min-key first %1 %2) pos)))))

;; {:m m
;;  :n n
;;  :basic-indexes basic-indexes
;;  :nonbasic-indexes nonbasic-indexes
;;  :obj-values obj-values
;;  :basic-values basic-values
;;  :matrix matrix}

(defn calc [{n-items :n
             capacity :c
             items :items
             :as data}]
  (let [
        ]
    {:opt
     :val
     })
  )

