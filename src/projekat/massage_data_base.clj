(ns projekat.massage_data_base
  (:require
   [clojure.java.jdbc :as jdbc];sql/sqlite database 
   [clojure.pprint :as p]
   [clj-time.core :as t]))

(def db {:classname   "org.sqlite.JDBC"
         :subprotocol "sqlite"
         :subname     "resources/massage.db"})
; CREATE TABLE
; (jdbc/db-do-commands db
;                      "create table massage (
;                        id integer primary key autoincrement,
;                        name varchar(255),
;                        description varchar(255),
;                       price real
;                        );"
;                      )


(defn  massages [] (jdbc/query db ["SELECT * FROM massage"]))

(massages)
(defn add-massage [massage]
  (jdbc/execute! db ["insert into massage (name, description, price) values (?, ?, ?) "  (:name massage) (:description massage) (:price massage)]))
; (add-massage { :name "Swedish" :description "Gentle touch" :price 40}) 
; (add-massage {:name "Thai" :description "Flexibility" :price 35})
; (add-massage {:name "Shiatsu" :description "Relaxation and relieve pain" :price 37})
; (add-massage {:name "Sport" :description "Release muscle tension" :price 45})
; (add-massage {:name "Chair" :description "Relaxed neck, shoulders and back." :price 60})

(defn get-massage-names []
  (map :name (jdbc/query db ["SELECT name FROM massage "])))
; (get-massage-names)

(defn noticeClients [massage]
  (spit "resources/news.txt"
        (format "[%s] Massage %s  has been changed. New description: %s. New price:%s \n "
                (clojure.string/replace (str (t/now)) "T" "  ")
                (:name massage)
                (:description massage)
                (:price massage))

        :append true))
(defn update-massage [massage]
  (jdbc/execute! db ["UPDATE massage  SET name = ? WHERE id = ?"  (:name massage) (:id massage)])
  (jdbc/execute! db ["UPDATE massage  SET description = ? WHERE id = ?"  (:description massage) (:id massage)])
  (jdbc/execute! db ["UPDATE massage  SET price = ? WHERE id = ?"  (:price massage) (:id massage)])
  (noticeClients massage))

    ; (update-massage { :id 5 :name "Relax" :description "Relaxing massage" :price 44})


(defn get-massage-by-id [id]
  (try
    (nth (filter #(= (:id %) id) (massages)) 0)
    (catch Exception e (str "Exception method: massage_data_base/get-massage-by-id")))
  ;vraca 1. element koji zadovoljava uslov
  )
; (get-massage-by-id 1)
(defn get-next-id []
  (+ 1 (:m (nth (jdbc/query db ["SELECT MAX(id) as m FROM massage"]) 0))))

; (get-next-id)
(defn delete-massage [id]
  (jdbc/execute! db ["DELETE FROM massage WHERE id = ?" id]))
; (delete-massage 6)

(defn get-massage-id-by-name [name]
  (try

    (:id (nth (jdbc/query db ["SELECT id FROM massage where name=?" name]) 0))
    (catch Exception e (str "Exception method: massage_data_base/get-massage-id-by-name"))))

(defn get-massage-price-by-id [id]
  (try
    (:price (nth (jdbc/query db ["SELECT price FROM massage where id=?" id]) 0))
    (catch Exception e (str "Exception method: massage_data_base/get-massage-price-by-id"))))

; (get-massage-price-by-id 1)
(defn table-view-massage []
  (p/print-table (jdbc/query db (str "select * from massage  ;"))))
(table-view-massage)

