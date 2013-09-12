(ns pivot.rdat
  (:require clojure.string)
  (:use clojure.tools.trace)
  )

;; (trace-ns 'pivot.other)

(def regex #"\s+")

(defn parse-line-with-fun [f line]
  (let [str-seq (clojure.string/split line regex)]
    (map f str-seq)))

(defn parse-index-line [line]
  (parse-line-with-fun #(Integer. %) line))

(defn parse-data-line [line]
  (parse-line-with-fun #(Double. %) line))

(defn valid-item [[n1 n2]]
  (and
   (not (= n1 nil))
   (not (= n2 nil))))

(defn parse-data-lines [lines]
  (filter valid-item (map parse-data-line lines)))

(defn get-data [fname]
  (let [text (slurp fname)
        lines (clojure.string/split-lines text)
        size-line (first lines)
        data-lines (rest lines)
        [n-items capacity] (parse-index-line size-line)
        items (vec (parse-data-lines data-lines))
        cnt-lines (count data-lines)
        cnt-items (count items)
        ]
    (assert (= cnt-lines cnt-items) "data items not equal data lines")
    {:n n-items :c capacity :items items}
    ))

