(ns transactions.csv-utils
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Utils

(defn resource [file-path]
  (.getFile (clojure.java.io/resource file-path)))

(defn csv-from-file [file-name separator]
  (with-open [file (io/reader file-name)]
    (doall
     (csv/read-csv file :separator separator :quote \"))))

(defn csv-resource [file-path separator]
  (with-open [file (io/reader (resource file-path))]
    (doall
     (csv/read-csv file :separator separator :quote \"))))


