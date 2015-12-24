(ns api.core
  (:require [clojure.string :refer [upper-case]]
            [compojure.core :refer [defroutes]]
            [ring.middleware.json :as ring-json]
            [compojure.handler :as handler]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [api.routes.users :as users]
            [api.routes.root :as root]
            [api.config :as config]
            [clojure.tools.logging :as log]
            [io.aviso.ansi :refer [bold bold-red bold-green bold-yellow]]))

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

(defn color-status [status]
  (cond
    (< status 300) (bold-green status)
    (< status 500) (bold-yellow status)
    (< status 600) (bold-red status)
    :else (bold status)))

(defn log-request [handler]
  (fn [request]
    (let [{:keys [uri request-method]} request
          response (handler request)]
      (println (str (-> request-method name upper-case) " " uri " " (-> response :status color-status)))
      response)))

  (fn [request]
    (println request)
    (handler request)))

(def app
  (-> (handler/api app-routes)
      (ring-json/wrap-json-body {:keywords? true})
      (ring-json/wrap-json-response)
      (log-request)
      (wrap-fallback-exception)))
