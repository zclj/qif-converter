(defproject transactions "0.1.0-SNAPSHOT"
  :description "Convert transaction lines to QIF-format"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/data.zip "0.1.0"]
                 [org.clojure/tools.cli "0.2.1"]]
  :main transactions.core)