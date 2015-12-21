(ns api.core
  (:require [compojure.core :refer [defroutes]]
            [ring.middleware.json :as ring-json]
            [compojure.handler :as handler]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [api.routes.users :as users]
            [api.routes.root :as root]
            [api.config :as config]))

; migrations
(defn load-config []
  {:datastore (jdbc/sql-database (str "jdbc:postgresql:" config/connection-string))
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))

; routes
(defroutes app-routes
  users/routes
  root/routes)

; middleware
(defn wrap-fallback-exception [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        {:status 500 :body (.getMessage e)}))))

(defn simple-logging-middleware [handler]
  (fn [request]
    (println request)
    (handler request)))

(def app
  (-> (handler/api app-routes)
      (ring-json/wrap-json-body {:keywords? true})
      (ring-json/wrap-json-response)
      (simple-logging-middleware)
      (wrap-fallback-exception)))
