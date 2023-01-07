;netstat -ano
; taskkill -pid 10400 /f ;KILLING THE PROCESS WITH PID AND PORT 3000
(ns projekat.core
  (:gen-class)
  (:require
   [ring.adapter.jetty :as ring]
   [compojure.core :refer :all]
   [compojure.route :as route]
   [hiccup.core :as h]
   [hiccup.form :as form]
   [ring.util.response :as resp]
   [ring.util.request :as req]
   [hiccup.page :refer [html5]]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [projekat.pages :as p]
   [projekat.database :as db]
   [ring.middleware.session :as session]
   [ring.middleware.params :refer [wrap-params]]
   [projekat.admin :as a]
   [ring.middleware.keyword-params :refer [wrap-keyword-params]]
   [ring.middleware.multipart-params :refer [wrap-multipart-params]]
   [projekat.massage_data_base :as massage_db]
   [projekat.reservation_database :as reservations_db]))

;***********************************
;RAD SA PUTANJAMA
; (defn only-digits[string] (every? #(Character/isDigit %) string))
; (defn only-string [string] (every? #(or (Character/isLetter %) (Character/isSpace %)) string))

(defn name-surname [name]
  ;name in format Nevena+Arsic 
  (clojure.string/replace name #"\+" " "))
(defn name-phone-password-id [string]
  ;string contains name and phone in this format
  ;name=Nevena+Arsic&phone=0000&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token* 

  (let [map {:name  (name-surname (clojure.string/replace (get (clojure.string/split string #"&") 0) "nametf=" ""))
             :phone (clojure.string/replace (get (clojure.string/split string #"&") 1) "phonetf=" "")
             :password (clojure.string/replace (get (clojure.string/split string #"&") 2) "passwordtf=" "")
             :id (clojure.string/replace (get (clojure.string/split string #"&") 3) "id=" "")}]  map))

(defn prepare-admin [string]
  (let [map {:login  (clojure.string/replace (get (clojure.string/split string #"&") 0) "logintf=" "")
             :pass (clojure.string/replace (get (clojure.string/split string #"&") 1) "passwordtf=" "")}] map))
(defn prepare-massage [string]
  (let [map {:name  (clojure.string/replace (clojure.string/replace (get (clojure.string/split string #"&") 0) "nametf=" "") #"\+" " ")
             :description (clojure.string/replace (clojure.string/replace (get (clojure.string/split string #"&") 1) "descriptiontf=" "") #"\+" " ")
             :price (parse-double (clojure.string/replace (get (clojure.string/split string #"&") 2) "pricetf=" ""))
             :id (clojure.string/replace (get (clojure.string/split string #"&") 3) "id=" "")}] map))
(defn prepare-reservation [string]
  (let [map {:phone   (clojure.string/replace (clojure.string/replace (get (clojure.string/split string #"&") 0) "phonetf=" "") #"\+" " ")
             :massage (clojure.string/replace (clojure.string/replace (get (clojure.string/split string #"&") 1) "massagetf=" "") #"\+" " ")}] map))
(defn prepare-client [string]
  (let [map {:phone   (clojure.string/replace (clojure.string/replace (get (clojure.string/split string #"&") 0) "phonetf=" "") #"\+" " ")
             :password (clojure.string/replace (clojure.string/replace (get (clojure.string/split string #"&") 1) "passwordtf=" "") #"\+" " ")}] map))
;*****************ROUTES**************
(defroutes app-routes

  (GET "/" [] (p/login-page))

  (GET "/index" [] (p/index))
  (GET "/index-admin" [] (p/index-admin))
  (GET "/index-client" [:as {session :session}] (p/index-client session))



  (GET "/clients" [] (p/clients-view (db/clients)))

  ;ako je admin ulogovan odmah vraca na pocetnu stranu
  ;ako admin nije ulogovan, prikazuje se forma za logovanje
  ;kada se admin izloguje menja se sesija
  (GET "/admin/login" [:as {session :session}]
    ; if admin is already logged in then go to index page
    (if (:admin session)
      (resp/redirect "/index-admin")
      (p/admin-login)))

  (GET "/client-login" [:as {session :session}]
    (if (= (:role session) "client")
      (resp/redirect "/index-client")
      (p/client-login)))
  (POST "/client-login" req
    (let [client (prepare-client (slurp (:body req)))]
      (if (db/check-login client)
        (-> (resp/redirect "/index-client")
            (assoc-in [:session :role] "client")
            (assoc-in [:session :id] (db/get-id-by-phone (:phone client))));u http zahtev dodaje se polje :session{:admin true} 
        (p/client-login "Invalid username or password"))))

  (POST "/admin/login" req
    (let [admin (prepare-admin (slurp (:body req)))]
      (if (a/check-login admin)
        (-> (resp/redirect "/index-admin")
            (assoc-in [:session :admin] true));u http zahtev dodaje se polje :session{:admin true} 
        (p/admin-login "Invalid username or password"))))


  (GET "/admin/logout" []
    (-> (resp/redirect "/index")
        (assoc-in [:session :admin] false)))
  (GET "/client-logout" []
    (-> (resp/redirect "/index")
        (assoc-in [:session :role] false)))

  (GET "/client-sign-up" [] (p/client-sign-up))
  (POST "/client-sign-up/:id" req (do
                                    (let [client (name-phone-password-id (slurp (:body req)))]
                                      (db/add-client client))
                                    (resp/redirect "/index-client")))

  (route/not-found "Not found"))

;routes accessable only for admi
(defroutes admin-routes


  (GET "/client/:id" [id] (p/client-view  (db/get-client-by-id (read-string id))))




  ;https://github.com/weavejester/hiccup/blob/1.0.5/src/hiccup/form.clj#L123
  ;NE POSTOJI DELETE RUTA ZA form/form-to hiccup, samo get i post
  (POST "/client-delete/delete/:id" [id]
    (do (db/delete-client (read-string id))
        (resp/redirect "/clients")))

  (GET "/massages/new" [] (p/new-massage-form))
  (POST "/massages/new/:id" req
    (do (let [massage (prepare-massage (slurp (:body req)))]
          (massage_db/add-massage massage))
        (resp/redirect "/index-admin")))

  (GET "/reservations" [] (p/reservation-index-page))
  (GET "/reservations/new" [] (p/new-reservation-form))

  (GET "/admin/graph" [] (do (p/admin-massage-graph)
                             (resp/redirect "/clients")))
  (GET "/admin/massages" [] (p/admin-massages))

  (POST "/admin/change-massage/:id" [id] (p/edit-massage-form id))
  (POST "/admin/change-massage/massage/:id" req
    (do (let [massage (prepare-massage (slurp (:body req)))]
          (massage_db/update-massage massage)
          ) 
        (resp/redirect "/admin/massages")))
  
  )

(defroutes client-routes
  (GET "/client/client/news" [] (p/prepare-news))

  (POST "/reservations/new/:id_client/:id_massage" [id_client id_massage]
    (do (reservations_db/add-reservation (read-string id_client) (read-string id_massage))
        (resp/redirect "/index-client")))
  ;kad se pozove samo kao ruta
  (GET "/client-edit/edit/:id" [id]
    (str (let [client (db/get-client-by-id (read-string id))] (p/edit-client client))))

  ;za edit polje kod clienta, fja p/client-view poziva post metodu
; (POST "/client-edit/edit/:id" [id]
;   (str (let [client (db/get-client-by-id (read-string id))] (p/edit-client client))))

  (POST "/client-edit/:id" req
    (do (let [client (name-phone-password-id (slurp (:body req)))]
          (db/update-client client))
        (resp/redirect "/index-client"))))
; ****************************************

;HANDLERS: app-routes, admin-routes
;MIDDLEWARE: wrap-admin-only
;ROUTES function combines more handlers into 1

(defn wrap-admin-only [handler]
  (fn [req]
    (if (-> req :session :admin)
      (handler req)
      (resp/redirect "/admin/login"))))

(defn wrap-client [handler]
  (fn [req]
    (if (-> req :session :role)
      (handler req)
      (resp/redirect "/client-login"))))


(def wrapping
  (-> (routes (wrap-routes admin-routes wrap-admin-only)
              (wrap-routes client-routes wrap-client)
              app-routes)
      (wrap-multipart-params)
      session/wrap-session))


(def server
  (ring/run-jetty wrapping {:port 3000
                            :join? false}))


;***********requests, responses, handlers

;https://www.baeldung.com/clojure-ring
;https://functionalhuman.medium.com/compojure-clout-tutorial-cf2f644abc71

;REQUEST-map with fields
;keys: :uri, :query-string, :request-method (get, post, put, delete), :headers, :body

;RESPONSE-map with field
;keys: :status, :headers, :body

;ring.util.response/response "Hello" function that means
;{:status 200, :headers {}, :body "Hello"}

;HANDLER-function takes req and returns resp
;(defn handler [req] (ring.util.response/response "Hello"))

;MIDDLEWARE-function that wrap the main handler and can change req and resp
;first par is handler, others are changes for resp/req

;(defn middleware [handler contetnt-type]
; (fn [req]  (let [res (handler req)]  (assoc-in res [:headers "Content-type"] content-type))
; )
;)
;(def app-handler (middleware handler "text/html"))
























