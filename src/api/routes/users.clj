(ns api.routes.users
  (:require [compojure.core :refer [GET POST defroutes]]))

(defroutes routes
  (GET "/users" []
    {:status 200
     :body {:message "/users get response changed"}})
  (POST "/users" [user]
    {:status 200
     :body user}))
