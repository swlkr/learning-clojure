(ns api.config
  (:require [envvar.core :refer [env]]))

(def connection-string (:database-url @env))

(def connection {:classname "org.postgresql.Driver"
                 :subprotocol "postgresql"
                 :subname connection-string})
