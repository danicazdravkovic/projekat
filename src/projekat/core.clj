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
(defn name-surname [name]
  ;name in format Nevena+Arsic
  (clojure.string/replace name #"\+" " "))
(defn name-phone-id [string]
  ;string contains name and phone in this format
  ;name=Nevena+Arsic&phone=0000&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*
  (let [map {:name  (name-surname (clojure.string/replace (get (clojure.string/split string #"&") 0) "name=" ""))
             :phone (clojure.string/replace (get (clojure.string/split string #"&") 1) "phone=" "")
             :id (clojure.string/replace (get (clojure.string/split string #"&") 2) "id=" "")}] map))

(defn prepare-admin [string]
  (let [map {:login  (clojure.string/replace (get (clojure.string/split string #"&") 0) "logintf=" "")
             :pass (clojure.string/replace (get (clojure.string/split string #"&") 1) "passwordtf=" "")}] map))
(defn prepare-massage [string]
  (let [map {:name  (clojure.string/replace(clojure.string/replace (get (clojure.string/split string #"&") 0) "nametf=" "")#"\+" " ")
             :description (clojure.string/replace (clojure.string/replace (get (clojure.string/split string #"&") 1) "descriptiontf=" "") #"\+" " ")
             :price (parse-double (clojure.string/replace (get (clojure.string/split string #"&") 2) "pricetf=" "") )
             }] map))
(defn prepare-reservation[string]
  (let [map {:phone   (clojure.string/replace(clojure.string/replace (get (clojure.string/split string #"&") 0) "phonetf=" "") #"\+" " ")
             :massage (clojure.string/replace (clojure.string/replace (get (clojure.string/split string #"&") 1) "massagetf=" "") #"\+" " ")
        }] map)
  )
;*****************ROUTES**************
(defroutes app-routes

  (GET "/" [] (p/index (p/massage-index-page)))

  (GET "/clients" [] (p/clients-view (db/clients)))
  (GET "/client/:id" [id] (p/client-view  (db/get-client-by-id (read-string id))))


  ;ako je admin ulogovan odmah vraca na pocetnu stranu
  ;ako admin nije ulogovan, prikazuje se forma za logovanje
  ;kada se admin izloguje menja se sesija
  (GET "/admin/login" [:as {session :session}]
    ; if admin is already logged in then go to index page
    (if (:admin session)
      (resp/redirect "/")
      (p/admin-login)))

  (POST "/admin/login" req
    (let [admin (prepare-admin (slurp (:body req)))]
      (if (a/check-login admin)
        (-> (resp/redirect "/")
            (assoc-in [:session :admin] true));u http zahtev dodaje se polje :session{:admin true} 
        (p/admin-login "Invalid username or password")))
    
    
    )


  (GET "/admin/logout" []
    (-> (resp/redirect "/")
        (assoc-in [:session :admin] false)))
  (GET "/massages/new" [] (p/new-massage-form))
(POST "/massages/new/:id" req
  (do (let [massage (prepare-massage (slurp (:body req)))]
        (massage_db/add-massage massage))
      (resp/redirect "/")))
  
  (GET "/reservations/new" [] (p/new-reservation-form))
  
  
  (POST "/reservations/new/:id" req 
    (do (let [reservation (prepare-reservation (slurp (:body req)))]
          (reservations_db/add-reservation reservation))
        (resp/redirect "/"))
    
    )

  
  (route/not-found "Not found")
  
  )

;routes accessable only for admin
(defroutes admin-routes

  (GET "/clients/new" [] (p/new-client-form))
  
  
  (POST "/clients/new/:id" req (do (let [client (name-phone-id (slurp (:body req)))]
                                     (db/add-client client))
                                   (resp/redirect "/")))
  
  ;kad se pozove samo kao ruta
  (GET "/client-edit/edit/:id" [id]
    (str (let [client (db/get-client-by-id (read-string id))] (p/edit-client client))))

  ;za edit polje kod clienta, fja p/client-view poziva post metodu
  (POST "/client-edit/edit/:id" [id]
    (str (let [client (db/get-client-by-id (read-string id))] (p/edit-client client))))
  
  (POST "/client-edit/:id" req
    (do (let [client (name-phone-id (slurp (:body req)))]
          (db/update-client client))
        (resp/redirect "/")))
  ;https://github.com/weavejester/hiccup/blob/1.0.5/src/hiccup/form.clj#L123
  ;NE POSTOJI DELETE RUTA ZA form/form-to hiccup, samo get i post
  (POST "/client-delete/delete/:id" [id]
    (do (db/delete-client (read-string id))
        (resp/redirect "/")))


)
; ****************************************


;HANDLERS: app-routes, admin-routes
;MIDDLEWARE: wrap-admin-only
;ROUTES function combines more handlers into 1

(defn wrap-admin-only [handler]
  (fn [req]
    (if (-> req :session :admin)
      (handler req)
      (resp/redirect "/admin/login"))))
(def wrapping
  (-> (routes (wrap-routes admin-routes wrap-admin-only)
              app-routes)
      (wrap-multipart-params)
      session/wrap-session))


;prikaz pojedinacnih ruta
; (wrapping
;  {:uri            "/"
;   :request-method :get})

(def server
  (ring/run-jetty wrapping {:port 3023 :join? false}))











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
























