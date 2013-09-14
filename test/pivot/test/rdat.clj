(ns pivot.test.rdat
  (:use clojure.tools.trace)
  ;; (:use [pivot.rdat])
  (:use [clojure.test]))

;; (trace-ns 'pivot.rdat)

(deftest parse-index-line-test
  (let [line "3 4 7"
        act (pivot.rdat/parse-index-line line)]
    (is (= [3 4 7] act))
    ))

(deftest parse-data-line-test
  (let [line "3.00 4 7.001"
        act (pivot.rdat/parse-data-line line)]
    (is (= [3.0 4.0 7.001] act))
    ))

(def text
  "3 4\n1 3 6\n2 4 5 7\n1 3 0\n0 0 -1 -2\n1 -1 0 -1\n-1 0.001 -2 0\n1 -1  2 3 1\n\n\n"
  )

(def lines ["3 4"
            "1 3 6"
            "2 4 5 7"
            "1 3 0"
            "0 0 -1 -2"
            "1 -1 0 -1"
            "-1 0.001 -2 0"
            "1 -1  2 3 1"])

(def parsed {
             :m 3
             :n 4
             :basic-indexes [1 3 6]
             :nonbasic-indexes [2 4 5 7]
             :basic-values [1.0 3.0 0.0]
             :matrix [[0.0 0.0 -1.0 -2.0]
                      [1.0 -1.0 0.0 -1.0]
                      [-1.0 0.001 -2.0 0.0]]
             :obj-values [1.0 -1.0  2.0 3.0 1.0]
             }
  )

(deftest split-text-to-lines-test
  (let [act (pivot.rdat/split-text-to-lines text)
        exp ["3 4"
             "1 3 6"
             "2 4 5 7"
             "1 3 0"
             "0 0 -1 -2"
             "1 -1 0 -1"
             "-1 0.001 -2 0"
             "1 -1  2 3 1"]]
    (is (= exp act))
    ))

(deftest parse-all-lines-test
  (let [act (pivot.rdat/parse-all-lines lines)
        exp parsed]
    (is (= exp act))
    ))

