(ns api.routes.users
  (:require [compojure.core :refer [GET POST defroutes]]
            [api.functions.users :as users]))

(defroutes routes
  (GET "/users" []
    {:status 200
     :body {:message "/users get response changed"}})
  (POST "/users" {body :body}
    (let [{:keys [email password confirmPassword]} body]
      {:status 200
       :body (users/validate body)})))
