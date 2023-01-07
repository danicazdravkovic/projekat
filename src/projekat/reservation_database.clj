(ns projekat.reservation_database
  (:require
   [clojure.java.jdbc :as jdbc]
   [clojure.pprint :as p]
   [projekat.massage_data_base :as massage_db]
    [projekat.database :as db]

 ))  
  
(def db {:classname   "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname     "resources/reservation.db"})

; (jdbc/db-do-commands db
;                      "create table reservation (
;                        id integer primary key autoincrement,
;                        client_id integer,
;                        massage_id integer
;                        );"
;                      )

(defn reservations [] (jdbc/query db ["SELECT * FROM reservation"]))
(reservations)
(defn client-reservations [client_id]
  (filter #(= (:client_id %) client_id) (reservations)))
(defn client-amounts [client_id]
  ;return all amounts for a client
  (for [i (range 0 (count (client-reservations client_id)))] 
      (massage_db/get-massage-price-by-id (:massage_id (nth (reservations) i)))))

; (client-amounts 4)
(defn total-client-amount [client_id]
  ;sums clients amounts
  (reduce + (client-amounts client_id)))

; (defn add-reservation [{phone :phone massage :massage }]
;   ;from http post method phone of client and massage name are delivered
  
;   (jdbc/execute! db ["insert into reservation (client_id, massage_id) values (?, ?) "   
;                      (db/get-id-by-phone phone) (massage_db/get-massage-id-by-name massage)]) 
;   ;updating amount into client db, field amount
;    (db/update-client-amount phone (total-client-amount (db/get-id-by-phone phone)))
  
  
  ; )
; (total-client-amount 1)
(defn add-reservation [id_client id_massage]
  ;from http post method phone of client and massage name are delivered

  (jdbc/execute! db ["insert into reservation (client_id, massage_id) values (?, ?) "
                     id_client id_massage])
  ;updating amount into client db, field amount
  (db/update-client-amount id_client (total-client-amount id_client))
  )
; (db/update-client-amount 1 0)
; (total-client-amount 1)
(defn delete-reservation [id]
  (jdbc/execute! db ["DELETE FROM reservation WHERE id = ?" id]))
; (delete-reservation 1)
(defn get-next-id []
  (+ 1 (:m (nth (jdbc/query db ["SELECT MAX(id) as m FROM reservation"]) 0))))

(defn table-view-reservation []
  (p/print-table (jdbc/query db (str "select * from reservation  ;"))))

(table-view-reservation)

;computing 
(defn number-of-reservations [massage_id]
  (count (filter #(=(:massage_id %) massage_id)(reservations)));returns
  )

(defn total-number-of-reserv[]
  (map number-of-reservations (map :id (massage_db/massages))))




