(ns projekat.database
  (:require
   [clojure.java.jdbc :as jdbc];sql/sqlite database 
   [clojure.pprint :as p]))

; ; **********************SQL DATABASE***********************
; (def sql-db {:dbtype "mysql"
;              :dbname "clojure"
;              :host "localhost"
;              :user "root"
;              :password ""}) 

; (def clients (j/query sql-db ["SELECT * FROM client"]))
; clients

; (defn add-client [client]
;   (j/execute! sql-db ["INSERT INTO client (name, phone)values (?, ?) "  (:name client) (:phone client)]))
; (defn update-client [client]
;   (j/execute! sql-db ["UPDATE client  SET phone = ? WHERE id = ?"  (:phone client) (:id client)])
;   (j/execute! sql-db ["UPDATE client  SET name = ? WHERE id = ?"  (:name client) (:id client)]))
; (update-client { :id 2 :name "Ana nikolic" :phone "090"})

; ;; (j/query sql-db ["SELECT * FROM client"]);citanje iz baze

; ; (j/insert! sql-db :klijent {:id 3 :name "Ana Milosevic" :phone "0602030499"});dodavanje u bazu 

; ;; (j/delete! sql-db :klijent ["id=?" 3]);brisanje iz tabele, vraca br redoova koji je izbrisan

; (defn get-client-by-id [id]
;   (nth (filter #(= (:id %) id) clients) 0) ;vraca 1. element koji zadovoljava uslov
;   )
; (defn get-next-id []
;  (+ 1 (:m (nth (j/query sql-db ["SELECT MAX(id) as m FROM client"]) 0))))

; (defn delete-client [id]
;   (j/execute! sql-db ["DELETE FROM client WHERE id = ?" id])
;   )
; (delete-client 5)

; DEFINING SQLITE DATABASE

;https://github.com/johngrib/example-clojure-sqlite/blob/main/src/step01/simple_query.clj
;MORALI SMO DA DODAMO SQLITE DRIVER U DEPENDECY U PROJECT FILE [org.xerial/sqlite-jdbc "3.7.2"]
(def db {:classname   "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname     "resources/client.db"})
; ;CREATE TABLE
; (jdbc/db-do-commands db
;                      "create table client (
;                        id integer primary key autoincrement,
;                        name varchar(255),
;                        phone varchar(255),
;                       amount double,
;                       password varchar(255),
;                       role varchar(255)
;                        );"
;                      )


(defn clients [] (jdbc/query db ["SELECT * FROM client"]))
(clients)
(defn add-client [client]
  (jdbc/execute! db ["insert into client (name, phone, amount, password, role) values (?, ?, ?, ?,?) "  (:name client) (:phone client) 0 (:password client) "client"]))
; (add-client { :name "Nevena Stanic" :phone "0679089700" :password "nevena" })
; (add-client {:name "Nikola Nikolic" :phone "065409756" :password "nikola"})
; (add-client {:name "Milica Milic" :phone "060342567" :password "milica"})



(defn update-client [client]
  (jdbc/execute! db ["UPDATE client  SET phone = ? WHERE id = ?"  (:phone client) (:id client)])
  (jdbc/execute! db ["UPDATE client  SET name = ? WHERE id = ?"  (:name client) (:id client)])
  (jdbc/execute! db ["UPDATE client  SET password = ? WHERE id = ?"  (:password client) (:id client)]))

   ;(update-client { :id 1 :name "Ana nikolic" :phone "090 9089009"})
(defn update-client-amount [id amount] 
    (jdbc/execute! db ["UPDATE client  SET amount = ? WHERE id = ?"  amount id])

  )
; (update-client-amount 22 0)

(defn get-client-by-id [id]
  (try
    (nth (filter #(= (:id %) id) (clients)) 0)
    (catch Exception e (str "Exception method: database/get-client-by-id"))
    )
  )
; (get-client-by-id 11)
(defn get-next-id []
  (+ 1 (:m (nth (jdbc/query db ["SELECT MAX(id) as m FROM client"]) 0))))

; (get-next-id)
(defn delete-client [id]
  (jdbc/execute! db ["DELETE FROM client WHERE id = ?" id]))
; (delete-client 22)

(defn table-view-client []
  (p/print-table (jdbc/query db (str "select * from client  ;"))))
(defn get-id-by-phone [phoneString]
  (try
  (:id (nth (jdbc/query db ["SELECT id FROM client where phone=?" phoneString]) 0))
   (catch Exception e (str "Exception method: database/get-id-by-phone")))
  )

(defn check-login[{phone :phone password :password}]
  (try
  (nth (jdbc/query db ["SELECT * FROM client WHERE phone=? AND password=?" phone password]) 0) 
    (catch Exception e (str "Exception method: database/check-login")))

    )
(table-view-client)

; (check-login {:phone "0679089700"  :password "nevena" })
; (get-id-by-phone "090 9089009")

; ; INSERT
;   (jdbc/db-do-commands db
;                        "insert into client (name, phone)
;                        values ('Danica Zdravkovic', '0652010059');")
; (jdbc/db-do-commands db
;                      "insert into client (name, phone)
;                        values ('Ana Anic', '060654332');")

; ; SELECT
;   (jdbc/query db "select * from client;")

;   ; SELECT -> TABLE VIEW
;   (p/print-table (jdbc/query db (str "select * from client  ;")))


; | :id |             :name |     :phone |
; |-----+-------------------+------------|
; |   1 | Danica Zdravkovic | 0652010059 |
; |   2 |          Ana Anic |  060654332 |