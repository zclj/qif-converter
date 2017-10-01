(ns transactions.accounts-test
  (:use clojure.test
        transactions.accounts
        [clojure.java.io :only [reader]]))

(def accounts
  {"One" "Account:NumberOne" "Alpha" "Account:Alphas" "Beta" "Account:Alphas" "Gamma" "Account:Alphas"})

(deftest load-accounts-map
  (testing "Given file URI returns a map of descriptions to accounts {desc acc}"
    (is (= (account-map "./test/resources/test-account-map.xml")
           accounts))))