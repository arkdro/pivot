(ns pivot.core
  {:doc "pivot"}
  (:use [clojure.tools.cli :only [cli]])
  (:require clojure.string)
  (:use clojure.tools.trace)
  (:require pivot.misc)
  (:require pivot.rdat)
  (:require pivot.step)
  (:gen-class)
  )

;; (trace-ns 'pivot.step)

(defn call-calc [verbose data]
  (if verbose
    (binding [*out* *err* pivot.misc/*verbose* 'true]
      (time (pivot.step/calc data)))
    (pivot.step/calc data)))

(defn print-result [{:keys [opt val used-items]}]
  (if (= opt val) (println val "1")
      (println val "0"))
  (let [])
  (doseq [x used-items]
    (print x ""))
  (println ""))

(defn -main [& args]
  (let [opts (cli
              args
              ["-v" "--[no-]verbose" :default false]
              ["-f" "--file" "input file"])
        [options _ _] opts
        data (pivot.rdat/get-data (:file options))
        res (call-calc (:verbose options) data)
        ]
    (print-result res)))

