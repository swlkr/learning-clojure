(ns api.utils.validation)

(defn valid-email? [email]
  (if
    (and
      (not (nil? email))
      (string? email)
      (re-find #".*@\w+[.]\w+" email))
    true
    false))

(defn validate-email [key email message]
  (if
    (valid-email? email)
    {key nil}
    {key message}))

(defn between? [val min max]
  (and
    (> max min)
    (> max val)
    (< min val)))

(defn validate-in-range [key password min max message]
  (if (between? (count password) min max)
    {key nil}
    {key message}))

(defn all-vals-nil? [map]
  (=
    (count
      (filter (fn [v] (not (nil? v)))
        (vals map)))
    0))
