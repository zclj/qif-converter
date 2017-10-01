(ns transactions.accounts
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.data.zip.xml :as zxml]))

(defn load-accounts [zip]
  (zxml/xml-> zip :map (zxml/attr :account)))

(defn account-descriptions [zip account]
  (zxml/xml-> zip :map (zxml/attr= :account account) :from zxml/text))

(defn map-description-to-account [descs account]
  (apply merge (for [description descs] {description account})))

(defn account-map [uri]
  (let [accounts-zip (zip/xml-zip (xml/parse uri))
        accounts (load-accounts accounts-zip)]
    (apply merge
           (for [account accounts]
             (map-description-to-account (account-descriptions accounts-zip account) account)))))
