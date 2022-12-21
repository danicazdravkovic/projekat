(ns projekat.admin)

(def admin-login(or (System/getenv "ADMIN_LOGIN") "admin"))
(def admin-pass (or (System/getenv "ADMIN_PASS") "admin"))

(defn check-login [{login :login pass :pass}]
  
  (and (= login admin-login)
       (= pass admin-pass))
  )
