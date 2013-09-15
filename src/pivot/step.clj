(ns pivot.step
  (:require [pivot.misc :as m])
  )

(defn find-entering-var [indexes values]
  (cond (empty? indexes) nil
        (empty? values) nil
        :default (let [joined (map #(vec [%1 %2]) indexes values)
                       pos (filter #(pos? (second %)) joined)]
                   (if (empty? pos) nil
                       (reduce #(min-key first %1 %2) pos)))))

(defn min-by-ratio-or-index
  ([[_i1 _b1 _v1 :as item]]
     item)
  ([[i1 b1 v1 :as item1] [i2 b2 v2 :as item2]]
     (cond
       (and (= v1 0) (= v2 0)) nil
       (= v1 0) item2
       (= v2 0) item1
       :default (let [r1 (m/abs (/ b1 v1))
                      r2 (m/abs (/ b2 v2))]
                  (cond
                    (< r1 r2) item1
                    (> r1 r2) item2
                    (< i1 i2) item1
                    :default item2)))))

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

