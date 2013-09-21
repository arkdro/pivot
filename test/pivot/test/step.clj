(ns pivot.test.step
  (:use clojure.tools.trace)
  (:use clojure.pprint)
  (:use [clojure.test]))

;; (trace-ns 'pivot.step)

(deftest find-entering-var-test
  (is (= [3 2.1] (pivot.step/find-entering-var [3 1 2] [nil 0 0 2.1])))
  (is (= nil (pivot.step/find-entering-var [3 1 2] [nil 0 0 0])))
  (is (= [1 0.014] (pivot.step/find-entering-var [3 1 2] [nil 0.014 11 2])))
  (is (= [2 0.001] (pivot.step/find-entering-var [3 1 2] [nil -1 0.001 -2])))
  (is (= nil (pivot.step/find-entering-var [3 1 2] [nil -1 -0.001 -2])))
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
  (is (= nil (pivot.step/find-leaving-var [3] [nil nil nil 2] [nil nil nil 3])))
  (is (= [2 3 -1] (pivot.step/find-leaving-var [2] [nil nil 3] [nil nil -1])))
  )

(deftest find-leaving-var-test-2
  (is (= [5 3 -1] (pivot.step/find-leaving-var
                   [3 4 5 6]
                   [nil nil nil 2 11 3 6]
                   [nil nil nil 3 0 -1 -1])))
  )

(deftest make-empty-row-test
  (is (= [nil nil nil nil nil] (pivot.step/make-empty-row 2 2)))
  )

(deftest make-empty-matrix-test
  (let [act (pivot.step/make-empty-matrix 2 2)
        exp [nil nil nil nil nil]
        ]
    (is (= exp act))
    ))

(deftest fill-one-row-aux-test-1
  (is (= :stub (pivot.step/fill-one-row-aux [] [] :stub)))
  )

(deftest fill-one-row-aux-test-2
  (let [row [2 -3 1 1]
        indexes [3 4 2 7]
        acc [nil nil nil nil nil nil nil nil]
        act (pivot.step/fill-one-row-aux row indexes acc)
        exp [nil nil 1 2 -3 nil nil 1]]
    (is (= exp act))
    ))

(deftest fill-one-row-test-1
  (let [row [2 -3 1 1]
        indexes [3 4 2 7]
        basic-koef 11
        m 3
        n 4
        act (pivot.step/fill-one-row m n row indexes)
        exp [nil nil 1 2 -3 nil nil 1]]
    (is (= exp act))
    ))

(deftest fill-obj-row-test
  (let [obj-values [11 2 -3 1 1]
        nonbasic-indexes [3 4 2 7]
        matrix [
                [nil nil nil nil nil nil nil nil]
                [0 1 2 3 4 5 6 7]
                ]
        m 3
        n 4
        act (pivot.step/fill-obj-row m n nonbasic-indexes obj-values)
        exp [nil nil 1 2 -3 nil nil 1]
        ]
    (is (= exp act))
    ))

(deftest fill-basic-column-test
  (let [m 3
        n 4
        basic-indexes [1 5 6]
        basic-values [4.0 5.0 0.0]
        act (pivot.step/fill-basic-column m n basic-indexes basic-values)
        exp [nil 4.0 nil nil nil 5.0 0.0 nil]]
    (is (= exp act))
    ))

(deftest prepare-dict-test
  (let [dict {:m 3
              :n 4
              :basic-indexes [1 5 6]
              :nonbasic-indexes [3 4 2 7]
              :obj-values [10.0 -1.0 1.0 -1.0 0.0]
              :basic-values [4.0 5.0 0.0]
              :matrix [[2.0 -3.0 1.0 1.0]
                       [-1.0 3.0 -1.0 -2.0]
                       [0.0 -1.0 1.0 3.0]]
              }
        exp {:m 3
             :n 4
             :basic-indexes [1 5 6]
             :nonbasic-indexes [3 4 2 7]
             :obj-values [10.0 -1.0 1.0 -1.0 0.0]
             :basic-values [4.0 5.0 0.0]
             :basic-column [nil 4.0 nil nil nil 5.0 0.0 nil]
             :obj-row [nil nil -1.0 -1.0 1.0 nil nil 0.0]
             :matrix [
                      nil
                      [nil nil 1.0 2.0 -3.0 nil nil 1.0]
                      nil
                      nil
                      nil
                      [nil nil -1.0 -1.0 3.0 nil nil -2.0]
                      [nil nil 1.0 0.0 -1.0 nil nil 3.0]
                      nil
                      ]
             :z 10.0
             }
        act (pivot.step/prepare-dict dict)
        ]
    ;; (pprint act)
    (is (= exp act))
    ))

(deftest get-row-item-test
  (is (= nil (pivot.step/get-row-item 3 nil)))
  (is (= nil (pivot.step/get-row-item nil [[3 4 5] nil [1 2 0] nil])))
  (is (= 5 (pivot.step/get-row-item 2 [3 4 5])))
  )

(deftest get-x-column-test
  (is (= nil (pivot.step/get-x-column nil [[]])))
  (is (= nil (pivot.step/get-x-column 3 nil)))
  (is (= [5 nil 0 nil] (pivot.step/get-x-column 2 [[3 4 5] nil [1 2 0] nil])))
  )

