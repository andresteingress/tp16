(defproject clj-talks "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [org.clojure/clojure "1.5.1"]

                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]

                 [org.elasticsearch/elasticsearch "1.0.0.Beta2"]

                 [com.netflix.hystrix/hystrix-clj "1.3.8"]

                 [overtone "0.9.1"]]

  :repl-options {
                 :init (do (use '[clojure.java.browse]))})
