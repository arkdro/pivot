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

