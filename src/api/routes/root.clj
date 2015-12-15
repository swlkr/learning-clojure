(ns api.routes.root
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :as route]))

(defroutes routes
  (GET "/" []
    {:status 200
     :body {:status "alive!"}})
  (route/not-found
    {:status 404
     :body {:message "Sorry, that route doesn't exist"}}))
