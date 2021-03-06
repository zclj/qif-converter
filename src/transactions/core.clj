(ns transactions.core
  (:gen-class)
  (:import (java.io File))
  (:require [transactions.convertion :as conv]
            [transactions.accounts :as acc]
            [clojure.tools.cli :as tools]
            [clojure.string :refer [join]]
            [transactions.csv-utils :as csv]))

(def accounts-option
  ["-a" "--accounts" "Path to accounts file" :default "./accounts.xml"])

(def help-flag ["-h" "--help" "Show help" :default false :flag true])

(defn write-and-exit! [message]
  (println message)
  ;;(System/exit 0)
  )

(defn files-not-existing [files]
  (filter #(not (.exists (File. %))) files))

(defn perform-conv [transactions-uri accounts-uri account-name]
  (let [file-ext (last (clojure.string/split transactions-uri #"\."))
        transactions (if (= file-ext "csv")
                       (conv/convert-csv
                        (rest (csv/csv-from-file transactions-uri \;))
                        (acc/account-map accounts-uri))
                       (conv/convert-to-data
                        (conv/load-transactions-file transactions-uri)
                        (acc/account-map accounts-uri)))
        unkowns (map #(:desc %) (filter #(= (:account %) "Unknown") transactions))]
    (if (seq unkowns)
      (cons "Missing account description for:"
            (map #(str "<from>" % "</from>") unkowns))
      (conv/convert-to-QIF transactions account-name))))

(defn -main
  "Entry point for QIF transactions converter"
  [& args]
  (let [[options arguments banner] (tools/cli args accounts-option help-flag)]
    (when (:help options)
      (println banner)
      (System/exit 0))
    (if (not= 2 (count arguments))
      (write-and-exit! "Usage: tcnv [OPTIONS] transactions-file account-name")
      (let [trans-uri (arguments 0)
            accs-uri (:accounts options)
            acc-name (arguments 1)
            files-not-found (files-not-existing [trans-uri accs-uri])]
        (if (seq files-not-found)
          (write-and-exit! (str "Could not find file :" (first files-not-found)))
          (println (join "\n" (perform-conv trans-uri accs-uri acc-name)))
          )))))
