(ns api.functions.users
  (:require [api.utils.validation :as v]))

(defn validate [user]
  (let [{:keys [email password confirmPassword]} user
        min 13
        max 50
        messages {:email "Invalid email"
                  :password (str "Password must be between " min " and " max " letters/numbers long")}
        result (merge
                (v/validate-email :email email (:email messages))
                (v/validate-in-range :password password min max (:password messages))
                (if (= confirmPassword password)
                  {:confirmPassword nil}
                  {:confirmPassword "Passwords need to match"}))]
    (if (v/all-vals-nil? result)
      user
      result)))
