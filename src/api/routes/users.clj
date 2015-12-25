(ns api.routes.users
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes context]]
            [compojure.coercions :refer [as-int]]
            [api.config :as config]
            [api.utils :refer [bind-error parse-int]]
            [api.models.users :refer [validate strip-password encrypt-password]]
            [api.db :as db]))

(defn list-users [limit offset]
  {:status 200
   :body (->> (db/get-users {:offset (parse-int offset) :limit (parse-int limit :fallback 10)})
              (map strip-password))})

(defn create-user [user]
  {:status 200
   :body (->> (validate user)
              (bind-error encrypt-password)
              (bind-error db/insert-user<!)
              (strip-password))})

(defn get-user [id]
  {:status 200
   :body (->> (db/get-user {:id id})
              (first)
              (strip-password))})

(defn update-user [id user]
  (let [db-user (first (db/get-user {:id id}))]
    {:status 200
     :body (->> (merge db-user user {:confirm-password (or (:password user) (:password db-user))})
                (validate)
                (bind-error db/update-user<!)
                (strip-password))}))

(defn delete-user [id]
  {:status 200
   :body (->> (db/delete-user<! {:id id})
              (strip-password))})

(defroutes routes
  (context "/users" []
    (defroutes users-routes
      (GET "/" [limit offset] (list-users limit offset))
      (POST "/" {body :body} (create-user body))
      (context "/:id" [id :<< as-int]
        (defroutes user-routes
          (GET "/" [] (get-user id))
          (PUT "/" {body :body} (update-user id body))
          (DELETE "/" [] (delete-user id)))))))
