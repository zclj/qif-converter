(ns transactions.convertion
  (:use [clojure.java.io :only [reader]]
        [clojure.string :only [join split]]))

(defn load-transactions-file
  [file-name]
  (line-seq (reader file-name)))

(defn create-header
  [account]
  ["!Account"
   (str "N" account)
   "^"
   "!Type:Bank"])

(defn convert-date
  [date-to-convert]
  (join "/" (reverse (split date-to-convert #"-"))))

(defn create-QIF
  [{:keys [date desc amount account]}]
  [(str "D" date)
   (str "T" amount)
   "CR"
   "P"
   (str "M" desc)
   (str "L" account)
   "^"])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Convert Text line

(defn convert-line-to-data
  [account-map line]
  (let [columns (map #(.trim %) (split line #"\t+"))
        desc (nth columns 2)]
    {:date (convert-date (nth columns 0))
     :desc desc
     :amount (clojure.string/replace
              (clojure.string/replace (nth columns 4) "," ".") " " ",") 
     :account (get account-map desc "Unknown")}))

(defn convert-to-data
  [transactions accounts]
  (for [transaction transactions]
    (convert-line-to-data accounts transaction)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; CSV convertion

(defn convert-csv-line-to-data
  [account-map [desc tx-date amount _]]
  {:date (convert-date tx-date)
   :desc desc
   :amount (clojure.string/replace (clojure.string/replace amount "," ".") " " ",")
   :account (get account-map desc "Unknown")})

(defn convert-csv
  [transactions accounts]
  (mapv #(convert-csv-line-to-data accounts %) transactions))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn convert-to-QIF [transactions-data account-name]
  (flatten (conj
            (for [line transactions-data] (create-QIF line))
            (create-header account-name))))
