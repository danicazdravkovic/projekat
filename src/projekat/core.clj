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
   [hiccup.page :refer [html5]]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [projekat.pages :as p]
   [projekat.database :as db]
   [ring.middleware.session :as session]
   [projekat.admin :as a]))

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




;*****************ROUTES**************
(defroutes app-routes

  (GET "/" [] (p/index ""))
  (GET "/clients" [] (p/clients-view db/clients))
  (GET "/client/:id" [id] (p/client-view  (db/get-client-by-id (read-string id))))

  (GET "/client-edit/edit/:id" [id]
    (str (let [client (db/get-client-by-id (read-string id))] (p/edit-client client))))
  (POST "/client-edit/:id" req (do (let [client (name-phone-id (slurp (:body req)))]
                                     (db/update-client client))
                                   (resp/redirect "/")))
  ;slurp cita slovo po slovo i vrac string 
  (GET "/clients/new" [] (p/new-client-form))
  (POST "/clients/new/:id" req (do (let [client (name-phone-id (slurp (:body req)))]
                                     (db/add-client client))
                                   (resp/redirect "/")))




  (GET "/admin/login" [:as {session :session}]
    ; (if (:admin session)
    ;   (resp/redirect "/")
    ;   (p/admin-login)))
    (str session "ses")
    )
  (POST "/admin/login" req (str req)
    (if (a/check-login "login" "pass")

      (-> (resp/redirect "/")
          (assoc-in [:session :admin] true))
      (p/admin-login)))
  (GET "/admin/logout" []
    (-> (resp/redirect "/")
        (assoc-in [:session :admin] false)))
  ;(route/not-found "Not found")
  )

;lein ring server
; (def handler
;   (ring/run-jetty app-routes {:port 3005 :join? false}))
(def app-handler
   
   ;(ring/run-jetty app-routes {:port 3005 :join? false}) ;handler
     (-> (wrap-defaults app-routes site-defaults) ;middleware
      (session/wrap-session);ring/session allows to store user's session using session key response, this values are availabe in req
      ))


;***********request  s, responses, handlers

;https://www.baeldung.com/clojure-ring

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



