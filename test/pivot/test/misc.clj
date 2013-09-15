(ns pivot.test.misc
  (:use clojure.tools.trace)
  (:use [clojure.test]))

;; (trace-ns 'pivot.step)

(deftest abs-test
  (is (= 2 (pivot.misc/abs 2)))
  (is (= 2 (pivot.misc/abs -2)))
  (is (= 0 (pivot.misc/abs 0)))
  )

