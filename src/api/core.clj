(ns api.core
  (:require [compojure.core :refer [defroutes]]
            [ring.middleware.json :as ring-json]
            [compojure.handler :as handler]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [api.routes.users :as users]
            [api.routes.root :as root]
            [api.functions.db :as db]))

; migrations
(defn load-config []
  {:datastore (jdbc/sql-database "jdbc:postgresql://localhost/simple-ajax")
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))

; routes
(defroutes app-routes
  users/routes
  root/routes)

(defn wrap-database-exception [handler]
  (fn [request]
    (try
      (handler request)
      (catch clojure.lang.ExceptionInfo e
        {:status 400 :body (.getMessage e)}))))

(defn wrap-fallback-exception [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        {:status 500 :body "Something isn't quite right"}))))

(def app
  (-> (handler/api app-routes)
      (ring-json/wrap-json-body {:keywords? true})
      (ring-json/wrap-json-response)
      (wrap-database-exception)
      (wrap-fallback-exception)))
