(ns api.functions.db
  (:require [clojure.java.jdbc :as jdbc]))

(defn wrap-exceptions [insert-call]
  (fn [connection table data]
    (try
      (insert-call connection table data)
      (catch org.postgres.util.PSQLException e
        (throw (ex-info "Invalid data" data))))))

(def do-insert
  wrap-exceptions (jdbc/insert!))

(defn insert! [connection table data]
  (do-insert connection table data))
