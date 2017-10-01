(ns transactions.convertion-test
  (:require [clojure.test :refer :all ]
            [transactions.convertion :as c]
            [clojure.java.io :refer [reader]]
            [transactions.csv-utils :as csv]))


(def transactions (line-seq (reader "./test/resources/test-transactions.txt")))

(def correct-QIF (line-seq (reader "./test/resources/test-transactions.qif")))

(def accounts
  {"One" "Account:NumberOne"
   "Alpha" "Account:Alphas"
   "Beta" "Account:Alphas"
   "Gamma" "Account:Alphas"})

(deftest convert-date-test
  (testing "Correct convertion of dates"
    (is (= (c/convert-date "11-10-31") "31/10/11"))))

(deftest create-header-test
  (testing "Creation of QIF header given an account name"
    (is (= (c/create-header "Räkningar")
           (list "!Account" "NRäkningar" "^" "!Type:Bank")))))

(def transaction-line-1 "11-12-30 	11-12-30  	One  	  	-56,50 	20 895,12")

(def QIF-list-1
  '("!Account"
    "N"
    "^"
    "!Type:Bank"
    "D30/12/11"
    "T-56.50"
    "CR"
    "P"
    "MOne"
    "LAccount:NumberOne"
    "^"))
(def QIF-list-2 (list "D30/12/11" "T-56.50" "CR" "P" "MOne" "LUnknown" "^"))

(def data-format-1 {:date "30/12/11",
                    :desc        "One",
                    :amount      "-56.50",
                    :account     "Account:NumberOne"})

(def data-format-2 {:date "30/12/11",
                    :desc        "One",
                    :amount      "-56.50",
                    :account     "Unknown"})

(deftest convert-line-to-data-with-know-account
  (testing "Convert a single transaction line to data format"
    (is (= (c/convert-to-data  [transaction-line-1] {"One" "Account:NumberOne"})
           [data-format-1]))))

(deftest convert-line-to-data-with-unknow-account
  (testing "Convert a single transaction line to data format"
    (is (= (c/convert-to-data  [transaction-line-1] {"Joa" "Account:Hoha"})
           [data-format-2]))))

(deftest convert-line-to-QIF-with-know-account
  (testing "Convert a single transaction lines data format to QIF"
    (is (= (c/convert-to-QIF  [data-format-1] "")
           QIF-list-1))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CSV source
(deftest convert-from-csv-file
  (testing "Convert transactions from csv file"
    (let [csv-tx (csv/csv-resource "test-transactions.csv" \;)]
      (is (= (c/convert-csv (rest csv-tx) accounts)
             [{:date    "29/09/2017",
               :desc    "One",
               :amount  "-1000",
               :account "Account:NumberOne"}
              {:date    "29/09/2017",
               :desc    "Alpha",
               :amount  "-556",
               :account "Account:Alphas"}
              {:date    "28/09/2017",
               :desc    "Beta",
               :amount  "-544",
               :account "Account:Alphas"}
              {:date    "28/09/2017",
               :desc    "Trams",
               :amount  "-100",
               :account "Unknown"}
              {:date    "28/09/2017",
               :desc    "Trams",
               :amount  "-200",
               :account "Unknown"}])))))
