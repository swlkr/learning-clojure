(ns api.config
  (:require [envvar.core :refer [env]])
  (:require [api.utils :refer [parse-int]]))

(def connection-string (:database-url @env))
(def clj-env (:clj-env @env))
(def port (or (parse-int (:port @env)) 3000))

(def connection {:classname "org.postgresql.Driver"
                 :subprotocol "postgresql"
                 :subname connection-string})
