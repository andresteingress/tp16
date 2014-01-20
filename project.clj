(defproject clj-talks "0.1.0-SNAPSHOT"
  :description "Slides for an introduction to Clojure"
  :url "http://technologieplauscherl.github.io/"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [
                 [org.clojure/clojure "1.5.1"]

                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [overtone "0.9.1"]]

  :repl-options {
                 :init (do (use '[clojure.java.browse]))}) ;; default ns import for the 'browse-url' function
