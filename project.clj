(defproject api "0.1.0-SNAPSHOT"
  :description "A simple db to json rest api in clojure"
  :url ""
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-devel "1.4.0"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [yesql "0.5.1"]
                 [ragtime "0.5.2"]
                 [io.aviso/pretty "0.1.20"]
                 [crypto-password "0.1.3"]
                 [clj-time "0.11.0"]
                 [envvar "1.1.0"]
                 [http-kit "2.1.18"]]
  :main api.core
  :aliases {"migrate" ["run" "-m" "api.core/migrate"]
            "rollback" ["run" "-m" "api.core/rollback"]})
