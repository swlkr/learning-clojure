(ns api.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as middleware]
            [compojure.handler :as handler]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]))

; migrations
(defn load-config []
  {:datastore (jdbc/sql-database "jdbc:postgresql://localhost/simple-ajax")
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))

(defroutes app-routes
  (GET "/" []
    {:status 200
     :body {:status "alive"}})
  (GET "/test" []
      {:status 200
       :body {:hello "world"}})
  (POST "/names" request
    (let [name (get-in request [:body :name])]
      {:status 200
       :body {:name name}}))
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      (middleware/wrap-json-response)))
