(ns pivot.misc)

(def ^:dynamic *verbose* 'false)

(defn log-val [tag & val]
  (if-not pivot.misc/*verbose* nil
          (println (.toString (java.util.Date.)) tag val)))

