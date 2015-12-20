(ns api.utils)

(defn valid-email? [email]
  (if
    (and
      (not (nil? email))
      (string? email)
      (re-find #".*@\w+[.]\w+" email))
    true
    false))

(defn between? [val min max]
  (and
    (> max min)
    (> max val)
    (< min val)))

(defn bind-error [f [error val]]
  (if (nil? error)
    (f val)
    [error nil]))
