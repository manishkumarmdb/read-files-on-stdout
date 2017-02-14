(defproject read-files-on-stdout "0.1.0-SNAPSHOT"
  :description "READ files from system and print on STDOUT."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure   "1.8.0"]
                 [doric                 "0.9.0"]]
  :main ^:skip-aot read-files-on-stdout.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
