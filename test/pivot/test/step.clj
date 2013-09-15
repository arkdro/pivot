(ns pivot.test.step
  (:use clojure.tools.trace)
  (:use [clojure.test]))

;; (trace-ns 'pivot.step)

(deftest find-entering-var-test
  (is (= [3 2.1] (pivot.step/find-entering-var [3 1 2] [2.1 0 0])))
  (is (= nil (pivot.step/find-entering-var [3 1 2] [0 0 0])))
  (is (= [1 0.014] (pivot.step/find-entering-var [3 1 2] [2 0.014 11])))
  (is (= [2 0.001] (pivot.step/find-entering-var [3 1 2] [-2 -1 0.001])))
  (is (= nil (pivot.step/find-entering-var [3 1 2] [-2 -1 -0.001])))
  (is (= nil (pivot.step/find-entering-var [] [])))
  )

(deftest min-by-ratio-or-index-test
  (is (= [] (pivot.step/min-by-ratio-or-index [])))
  (is (= [1 2 0] (pivot.step/min-by-ratio-or-index [1 2 0])))
  (is (= [1 2 3] (pivot.step/min-by-ratio-or-index [1 2 3])))
  (is (= [1 2 3] (pivot.step/min-by-ratio-or-index [1 2 3] [4 5 6])))
  (is (= nil (pivot.step/min-by-ratio-or-index [1 2 0] [4 5 0])))
  (is (= [1 2 3] (pivot.step/min-by-ratio-or-index [1 2 3] [4 5 0])))
  (is (= [1 -2 3] (pivot.step/min-by-ratio-or-index [1 -2 3] [4 5 -6])))
  (is (= [4 5 -6] (pivot.step/min-by-ratio-or-index [1 -3 -2] [4 5 -6])))
  )

(deftest find-leaving-var-test-1
  (is (= nil (pivot.step/find-leaving-var [] [] [])))
  (is (= nil (pivot.step/find-leaving-var [3] [2] [3])))
  (is (= [5 3 -1] (pivot.step/find-leaving-var [5] [3] [-1])))
  )

(deftest find-leaving-var-test-2
  (is (= [5 3 -1] (pivot.step/find-leaving-var
                   [3 4 5 6]
                   [2 11 3 6]
                   [3 0 -1 -1])))
  )

