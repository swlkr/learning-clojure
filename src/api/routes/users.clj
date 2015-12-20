(ns api.routes.users
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes context]]
            [compojure.coercions :refer [as-int]]
            [api.config :as config]
            [api.utils :refer [bind-error]]
            [api.models.users :refer [validate-insert validate-update strip-password]]
            [api.db :as db]))

(defn list-users [params]
  (let [{:keys [offset limit] :or {offset 0 limit 10}} params]
    {:status 200
     :body (db/get-users {:offset offset :limit limit})}))

(defn create-user [user]
  {:status 200
   :body (->> (validate-insert user)
              (bind-error db/insert-user<!)
              (strip-password))})

(defn get-user [id]
  {:status 200
   :body (db/get-user {:id id})})

(defn update-user [id user]
  (let [{:keys [email password]} user]
    {:status 200
     :body (->> (validate-update {:id id, :email email :password password})
                (bind-error db/update-user<!))}))

(defn delete-user [id]
  {:status 200
   :body (db/delete-user<! {:id id})})

(defroutes routes
  (context "/users" []
    (defroutes users-routes
      (GET "/" {params :query-params} (list-users params))
      (POST "/" {body :body} (create-user body))
      (context "/:id" [id :<< as-int]
        (defroutes user-routes
          (GET "/" [] (get-user id))
          (PUT "/" {body :body} (update-user id body))
          (DELETE "/" [] (delete-user id)))))))
