(ns api.routes.users
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes context]]
            [compojure.coercions :refer [as-int]]
            [api.config :as config]
            [api.utils :refer [bind-error parse-int]]
            [api.models.users :refer [validate validate-email validate-password strip-password]]
            [api.db :as db]))

(defn list-users [limit offset]
  {:status 200
   :body (->> (db/get-users {:offset (parse-int offset) :limit (parse-int limit :fallback 10)})
              (map strip-password))})

(defn create-user [user]
  {:status 200
   :body (->> (validate user)
              (bind-error db/insert-user<!)
              (strip-password))})

(defn get-user [id]
  {:status 200
   :body (->> (db/get-user {:id id})
              (first)
              (strip-password))})

(defn update-password [id password]
  {:status 200
   :body (->> (validate-password {:id id :password password})
              (bind-error db/update-user-password<!)
              (strip-password))})

(defn update-email [id email]
  {:status 200
   :body (->> (validate-email {:id id :email email})
              (bind-error db/update-user-email<!)
              (strip-password))})

(defn delete-user [id]
  {:status 200
   :body (db/delete-user<! {:id id})})

(defroutes routes
  (context "/users" []
    (defroutes users-routes
      (GET "/" [limit offset] (list-users limit offset))
      (POST "/" {body :body} (create-user body))
      (context "/:id" [id :<< as-int]
        (defroutes user-routes
          (GET "/" [] (get-user id))
          (PUT "/password" {body :body} (update-password id (:password body)))
          (PUT "/email" {body :body} (update-email id (:email body)))
          (DELETE "/" [] (delete-user id)))))))
