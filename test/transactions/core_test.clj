(ns transactions.core-test
  (:require [clojure.test :refer :all ]
            [clojure.java.io :refer [reader]]
            [transactions.core :as core]))

(def correct-QIF (line-seq (reader "./test/resources/test-transactions-old.qif")))
(def correct-QIF-csv (line-seq (reader "./test/resources/test-transactions-csv.qif")))

(deftest convert-file-txt
  "Convert file in old legacy txt format"
  (is (= (core/perform-conv "./test/resources/test-transactions-old.txt"
                            "./test/resources/test-account-map.xml"
                            "TestAccount")
         correct-QIF)))

(deftest convert-file-csv
  "Convert file in csv format"
  (is (= (core/perform-conv "./test/resources/test-transactions-clean.csv"
                            "./test/resources/test-account-map.xml"
                            "TestAccount")
         correct-QIF-csv)))



