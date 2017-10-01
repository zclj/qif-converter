(ns transactions.convertion-test
  (:use clojure.test
        transactions.convertion
        [clojure.java.io :only [reader]]))

(def transactions (line-seq (reader "./test/resources/test-transactions.txt")))

(def correct-QIF (line-seq (reader "./test/resources/test-transactions.qif")))

(def accounts
  {"One" "Account:NumberOne" "Alpha" "Account:Alphas" "Beta" "Account:Alphas" "Gamma" "Account:Alphas"})

(deftest convert-transactions-to-QIF
  (testing "Convert transactions to QIF"
    (is (= (convert-to-QIF transactions accounts "Räkningar")
           correct-QIF ))))

(deftest convert-date-test
  (testing "Correct convertion of dates"
    (is (= (convert-date "11-10-31") "31/10/11"))))

(deftest create-header-test
  (testing "Creation of QIF header given an account name"
    (is (= (create-header "Räkningar")
           (list "!Account" "NRäkningar" "^" "!Type:Bank")))))

(def transaction-line-1 "11-12-30 	11-12-30  	One  	  	-56,50 	20 895,12")
(def QIF-list-1 (list "D30/12/11" "T-56.50" "CR" "P" "MOne" "LAccount:NumberOne" "^"))
(def QIF-list-2 (list "D30/12/11" "T-56.50" "CR" "P" "MOne" "LUnknown" "^"))

(deftest convert-line-to-QIF-with-know-account
  (testing "Convert a single line to QIF given a known description to account mapping"
    (is (= (convert-line-to-QIF  {"One" "Account:NumberOne"} transaction-line-1)
           QIF-list-1))))

(deftest convert-line-to-QIF-with-unknow-account
  (testing "Convert a single line to QIF given a unknown description to account mapping"
    (is (= (convert-line-to-QIF  {"Joa" "Account:Hoha"} transaction-line-1)
           QIF-list-2))))

