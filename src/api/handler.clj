(ns api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as middleware]
            [compojure.handler :as handler]))

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
