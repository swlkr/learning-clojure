(ns api.models.users
  (:require [api.utils :refer [valid-email? between? bind-error]]
            [crypto.password.scrypt :as password]))

(def min-length 13)
(def max-length 50)

(defn validate-email [params]
  "Ensure (:email params) matches a regexp"
  (if (valid-email? (:email params))
    [nil params]
    ["Please enter a valid email address" nil]))

(defn validate-password [params]
  "Ensure (:password params) is between min-length and max-length"
  (if (between? (count (:password params)) min-length max-length)
    [nil params]
    [(str "Please enter a password between " min-length " and " max-length " characters long") nil]))

(defn validate-confirm-password [params]
  "Ensure (:confirm-password params) is equal to (:password params)"
  (let [{:keys [password confirm-password]} params]
    (if (= password confirm-password)
      [nil params]
      ["Please make sure that your password confirmation matches" nil])))

(defn encrypt-password [params]
  "Hashes the password with scrypt"
  (if (contains? params :password)
    [nil (update-in params [:password] password/encrypt)]
    [nil params]))

(defn validate [params]
  (->> (validate-email params)
       (bind-error validate-password)
       (bind-error validate-confirm-password)))

(defn strip-password [params]
  (if (contains? params :password)
    (dissoc params :password)
    params))
