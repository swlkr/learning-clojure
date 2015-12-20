(ns api.db
  (:require [yesql.core :refer [defqueries]]
            [api.config :as config]))

(defqueries "api/sql/users.sql"
  {:connection config/connection})
