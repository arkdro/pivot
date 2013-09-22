(ns pivot.step
  (:require [pivot.misc :as m])
  )

(defn get-row-item [i row]
  (if (nil? row) nil
      (get row i)))

(defn get-x-column [i matrix]
  (cond
    (nil? i) nil
    (nil? matrix) nil
    :default (vec (map #(get-row-item i %) matrix))))

(defn find-entering-var [indexes values]
  (cond (empty? indexes) nil
        (empty? values) nil
        :default (let [joined (map #(vec [%1 (get values %1)]) indexes)
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

(defn find-leaving-var [indexes basic-column x-column]
  (cond (empty? indexes) nil
        (empty? basic-column) nil
        (empty? x-column) nil
        :default (let [joined (map
                               #(vec [%
                                      (get basic-column %)
                                      (get x-column %)])
                               indexes)
                       neg (filter #(neg? (get % 2)) joined)]
                   (if (empty? neg) nil
                       (reduce #(min-by-ratio-or-index %1 %2) neg)))))

(defn make-empty-row [m n]
  (let [size (+ m n 1)]
    (vec (repeat size nil))))

(defn make-empty-matrix [m n]
  (let [size (+ m n 1)]
    (vec (repeat size nil))))

(defn fill-one-row-aux [[h-row & t-row] [h-idx & t-idx] acc]
  (if (or (nil? h-row) (nil? h-idx)) acc
      (let [new-acc (assoc acc h-idx h-row)]
        (recur t-row t-idx new-acc))))

(defn fill-one-row [m n in-row nonbasic-indexes]
  (let [row (make-empty-row m n)
        out-row (fill-one-row-aux in-row nonbasic-indexes row)]
    (vec out-row)))

(defn fill-obj-row [m n nonbasic-indexes obj-values]
  (let [obj-koef (first obj-values)
        obj-row (rest obj-values)
        new-obj-row (fill-one-row m n obj-row nonbasic-indexes)]
    new-obj-row))

(defn fill-basic-column [m n basic-indexes basic-values]
  (fill-one-row m n basic-values basic-indexes))

(defn get-z-value [obj-values]
  (first obj-values))

(defn fill-rows-aux [idx acc-matrix
                     {m :m
                      n :n
                      basic-indexes :basic-indexes
                      nonbasic-indexes :nonbasic-indexes
                      basic-values :basic-values
                      obj-values :obj-values
                      matrix :matrix
                      :as dict}]
  (if (>= idx m) [(fill-obj-row m n nonbasic-indexes obj-values)
                  (fill-basic-column m n basic-indexes basic-values)
                  (get-z-value obj-values)
                  acc-matrix]
      (let [in-row (get matrix idx)
            new-row (fill-one-row m n in-row nonbasic-indexes)
            subscript (get basic-indexes idx)
            new-acc-matrix (assoc acc-matrix subscript new-row)]
        (recur (inc idx) new-acc-matrix dict))))

(defn fill-rows [acc-matrix dict]
  (fill-rows-aux 0 acc-matrix dict)
  )

(defn make-full-matrix [{m :m
                         n :n
                         :as dict}]
  (let [empty (make-empty-matrix m n)]
    (fill-rows empty dict)))

(defn prepare-dict [{basic-indexes :basic-indexes
                     nonbasic-indexes :nonbasic-indexes
                     :as dict}]
  (let [[obj-row basic-column z matrix] (make-full-matrix dict)]
    {
     :z z
     :obj-row obj-row
     :basic-column basic-column
     :nonbasic-indexes nonbasic-indexes
     :basic-indexes basic-indexes
     :matrix matrix
     }
    ))

(defn calc [dict]
  (let [new-dict (prepare-dict dict)
        {obj-row :obj-row
         nonbasic-indexes :nonbasic-indexes
         basic-indexes :basic-indexes
         basic-column :basic-column
         matrix :matrix}
        new-dict
        [entering _] (find-entering-var nonbasic-indexes obj-row)
        [leaving _ _] (find-leaving-var basic-indexes
                                        basic-column
                                        (get-x-column entering matrix))
        ]
    new-dict
    )
  )

