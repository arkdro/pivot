(ns pivot.rdat
  (:require clojure.string)
  (:use clojure.tools.trace)
  )

;; (trace-ns 'pivot.other)

(def regex #"\s+")

(defn parse-line-with-fun [f line]
  (let [str-seq (clojure.string/split line regex)]
    (vec (map f str-seq))))

(defn parse-index-line [line]
  (parse-line-with-fun #(Integer. %) line))

(defn parse-data-line [line]
  (parse-line-with-fun #(Double. %) line))

(defn parse-basic-index-line [lines]
  (let [line (get lines 1)]
    (parse-index-line line)))

(defn parse-basic-data-line [lines]
  (let [line (get lines 3)]
    (parse-data-line line)))

(defn parse-obj-data-line [m lines]
  (let [idx (+ m 4)
        line (get lines idx)]
    (parse-data-line line)))

(defn parse-nonbasic-index-line [lines]
  (let [line (get lines 2)]
    (parse-index-line line)))

(defn parse-matrix [[m n] lines]
  (let [offset 4
        stop (+ m offset)
        row-indexes (range offset stop)
        rows (map #(parse-data-line (get lines %)) row-indexes)]
    (vec rows)))

(defn split-text-to-lines [text]
  (vec (clojure.string/split-lines text)))

(defn parse-all-lines [lines]
  (let [sizes (parse-index-line (get lines 0))
        [m n] sizes
        basic-indexes (parse-basic-index-line lines)
        nonbasic-indexes (parse-nonbasic-index-line lines)
        basic-values (parse-basic-data-line lines)
        matrix (parse-matrix sizes lines)
        obj-values (parse-obj-data-line m lines)
        cnt-lines (count matrix)]
    {:m m
     :n n
     :basic-indexes basic-indexes
     :nonbasic-indexes nonbasic-indexes
     :obj-values obj-values
     :basic-values basic-values
     :matrix matrix}
    ))

(defn parse-whole-text [text]
  (let [lines (split-text-to-lines text)]
    (parse-all-lines lines)))

(defn validate-parsed [{m :m
                        n :n
                        basic-indexes :basic-indexes
                        nonbasic-indexes :nonbasic-indexes
                        obj-values :obj-values
                        basic-values :basic-values
                        matrix :matrix
                        }]
  (assert (= (count basic-indexes) m) "wrong number of basic indexes")
  (assert (= (count nonbasic-indexes) n) "wrong number of non-basic indexes")
  (assert (= (count obj-values) (inc n)) "wrong number of obj values")
  (assert (= (count basic-values) m) "wrong number of basic values")
  (assert (= (count matrix) m) "wrong number of matrix rows")
  (doseq [row matrix]
    (assert (= (count row) n) "wrong number of row items"))
  )

(defn get-data [fname]
  (let [text (slurp fname)
        data (parse-whole-text text)]
    (validate-parsed data)
    data))

