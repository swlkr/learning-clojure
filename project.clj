(defproject api "0.1.0-SNAPSHOT"
  :description "A simple db to json rest api in clojure"
  :url ""
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-json "0.4.0"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [yesql "0.5.1"]
                 [ragtime "0.5.2"]
                 [org.clojure/tools.logging "0.3.1"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler api.core/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}}
  :aliases {"migrate" ["run" "-m" "api.core/migrate"]
            "rollback" ["run" "-m" "api.core/rollback"]})
