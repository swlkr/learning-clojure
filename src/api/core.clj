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
            [io.aviso.ansi :refer [bold bold-red bold-green bold-yellow]]
            [clj-time.core :as t]))

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

(defn log-str [request response start-time]
  (str
    (-> request :request-method name upper-case)
    " "
    (:uri request)
    " "
    (-> response :status color-status)
    " "
    (t/in-millis (t/interval start-time (t/now)))
    "ms"))

(defn log-request [handler]
  (fn [request]
    (let [start-time (t/now)
          {:keys [uri request-method]} request
          response (handler request)]
      (println (log-str request response start-time))
      response)))

(def app
  (-> (handler/api app-routes)
      (wrap-fallback-exception)
      (ring-json/wrap-json-body {:keywords? true})
      (ring-json/wrap-json-response)
      (log-request)))
