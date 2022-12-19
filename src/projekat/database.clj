(ns projekat.database
  (:require
   [clojure.java.jdbc :as j]))

(def sql-db {:dbtype "mysql"
             :dbname "clojure"
             :host "localhost"
             :user "root"
             :password ""}) 

(def clients (j/query sql-db ["SELECT * FROM client"]))
clients

(defn add-client [client]
  (j/execute! sql-db ["INSERT INTO client (name, phone)values (?, ?) "  (:name client) (:phone client)]))
(defn update-client [client]
  (j/execute! sql-db ["UPDATE client  SET phone = ? WHERE id = ?"  (:phone client) (:id client)])
  (j/execute! sql-db ["UPDATE client  SET name = ? WHERE id = ?"  (:name client) (:id client)]))
(update-client { :id 2 :name "Ana nikolic" :phone "090"})

;; (j/query sql-db ["SELECT * FROM client"]);citanje iz baze

; (j/insert! sql-db :klijent {:id 3 :name "Ana Milosevic" :phone "0602030499"});dodavanje u bazu 

;; (j/delete! sql-db :klijent ["id=?" 3]);brisanje iz tabele, vraca br redoova koji je izbrisan

(defn get-client-by-id [id]
  (nth (filter #(= (:id %) id) clients) 0) ;vraca 1. element koji zadovoljava uslov
  )
(defn get-next-id []
 (+ 1 (:m (nth (j/query sql-db ["SELECT MAX(id) as m FROM client"]) 0))))
