(ns api.config)

(def connection-string (get (System/getenv) "DATABASE_URL"))

(def connection {:classname "org.postgresql.Driver"
                 :subprotocol "postgresql"
                 :subname connection-string})
