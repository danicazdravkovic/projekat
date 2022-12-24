(ns projekat.massage_data_base
  (:require
   [clojure.java.jdbc :as jdbc];sql/sqlite database 
   [clojure.pprint :as p]))

(def db {:classname   "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname     "resources/massage.db"})
;CREATE TABLE
; (jdbc/db-do-commands db
;                      "create table massage (
;                        id integer primary key autoincrement,
;                        name varchar(255),
;                        description varchar(255)
;                        );"
;                      )


(def massages (jdbc/query db ["SELECT * FROM massage"]))
massages
(defn add-massage [massage]
  (jdbc/execute! db ["insert into massage (name, description) values (?, ?) "  (:name massage) (:description massage)]))
; (add-massage { :name "Swedish" :description "Gentle touch" }) 
; (add-massage {:name "Thai" :description "Flexibility"})
; (add-massage {:name "Shiatsu" :description "Relaxation and relieve pain"})
; (add-massage {:name "Sport" :description "Release muscle tension"})
; (add-massage {:name "Chair" :description "Relaxed neck, shoulders and back."})




(defn update-massage [massage]
  (jdbc/execute! db ["UPDATE massage  SET name = ? WHERE id = ?"  (:name massage) (:id massage)])
  (jdbc/execute! db ["UPDATE massage  SET description = ? WHERE id = ?"  (:description massage) (:id massage)]))

   ; (update-massage { :id 6 :name "Relax" :description "Relaxing massage"})


(defn get-massage-by-id [id]
  (nth (filter #(= (:id %) id) massages) 0) ;vraca 1. element koji zadovoljava uslov
  )
; (get-massage-by-id 1)
(defn get-next-id []
  (+ 1 (:m (nth (jdbc/query db ["SELECT MAX(id) as m FROM massage"]) 0))))

; (get-next-id)
(defn delete-massage [id]
  (jdbc/execute! db ["DELETE FROM massage WHERE id = ?" id]))
;(delete-massage 10)

(defn table-view-massage []
  (p/print-table (jdbc/query db (str "select * from massage  ;"))))
(table-view-massage)