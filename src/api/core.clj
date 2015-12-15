(ns api.core
  (:require [compojure.core :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as middleware]
            [compojure.handler :as handler]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [api.routes.users :as users]
            [api.routes.root :as root]))

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
  root/routes
  (route/not-found
    {:status 404
     :body {:message "not found!"}}))

(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      (middleware/wrap-json-response)))
