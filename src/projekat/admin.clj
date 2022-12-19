(ns projekat.admin)

(def admin-username(or (System/getenv "ADMIN_USERNAME") "admin"))
(def admin-pass (or (System/getenv "ADMIN_PASS") "admin"))

(defn check-login [username pass]
  
  (and (= username admin-username)
       (= pass admin-pass))
  )
