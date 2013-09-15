(ns pivot.test.step
  (:use clojure.tools.trace)
  (:use [clojure.test]))

;; (trace-ns 'pivot.step)

(deftest find-entering-var-test
  (is (= [1 0.014] (pivot.step/find-entering-var [3 1 2] [2 0.014 11])))
  (is (= [2 0.001] (pivot.step/find-entering-var [3 1 2] [-2 -1 0.001])))
  (is (= nil (pivot.step/find-entering-var [3 1 2] [-2 -1 -0.001])))
  (is (= nil (pivot.step/find-entering-var [] [])))
  )

