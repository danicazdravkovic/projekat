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
(defn client-amounts [client_id]
  ;return all amounts for a client
  (for [i (range 0 (count (reservations)))]
    (when (= client_id (:client_id (nth (reservations) i)))
      (massage_db/get-massage-price-by-id (:massage_id (nth (reservations) i))))))
(defn total-client-amount [client_id]
  ;sums clients amounts
  (reduce + (client-amounts client_id)))


(defn add-reservation [{phone :phone massage :massage }]
  ;from http post method phone of client and massage name are delivered
  
  (jdbc/execute! db ["insert into reservation (client_id, massage_id) values (?, ?) "   
                     (db/get-id-by-phone phone) (massage_db/get-massage-id-by-name massage)]) 
  ;updating amount into client db, field amount
   (db/update-client-amount phone (total-client-amount (db/get-id-by-phone phone)))
  
  
  )

; (add-reservation { :client_id 1  :massage_id 1})
(defn delete-reservation [id]
  (jdbc/execute! db ["DELETE FROM reservation WHERE id = ?" id]))
; (delete-reservation 1)
(defn get-next-id []
  (+ 1 (:m (nth (jdbc/query db ["SELECT MAX(id) as m FROM reservation"]) 0))))




; (db/table-view-client)
; (massage_db/table-view-massage)
; (table-view-reservation)
; (client-amounts 1)
(total-client-amount 1)


(defn table-view-reservation []
  (p/print-table (jdbc/query db (str "select * from reservation  ;"))))

(table-view-reservation)
