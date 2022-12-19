(ns projekat.pages
  (:require
   [hiccup.page :refer [html5]]
   [hiccup.form :as form]
   [ring.util.anti-forgery :refer (anti-forgery-field)]
   [projekat.database :as db]))

(defn base-page [& body]
  (html5
   [:head [:title "SPA Center"]]
   [:body
    [:h1 "Spa center"]
    [:a {:href "/clients/new"} "New client"]
    [:hr]
    body]))


(defn index [body]
  (base-page body))

(defn client-view [{id :id name :name phone :phone}]
  (html5
   [:li (format "Name: %s          Phone: %s" name phone)]))
(defn clients-view [clients]
  (html5 [:ul
          (map  client-view clients)]))
(defn edit-client [client]
  (html5
   [:body
    [:p (:id client)]
    (form/form-to [:post (if client
                           (str "/client-edit/" (:id client))
                           "/clients")]

                  (form/label "name" "Name: ")
                  (form/text-field "name" (:name client))
                  (form/label "phone" "Phone: ")
                  (form/text-field "phone" (:phone client))
                  (form/hidden-field "id" (:id client))
                  (anti-forgery-field)

                  (form/submit-button "Save changes"))]))
; (edit-client (db/get-client-by-id 2))
; (defn edit-client [{id :id name :name phone :phone}]
;   (html5
;    (form/form-to [:post (str "/client-edit/" id)]
;                  (form/label "namel" "Name: ")
;                  (form/text-field "nametf" name)

;                  (form/label "phonel" "Phone: ")
;                  (form/text-field "phonetf" phone)
;                  (anti-forgery-field)
;                  (form/submit-button "Save changes"))))

(defn admin-login []
  (html5
   ;[:head [:titile "Admin login"]]
   [:body [:h1 "Admin login"]
    (form/form-to [:post (str "/admin/login")]
                  (form/label "usernamel" "Username: ")
                  (form/text-field "logintf")
                  (form/label "passwordl" "Password: ")
                  (form/password-field "passwordtf")
                  (anti-forgery-field)
                  (form/submit-button "Login"))]))



(defn new-client-form []
  (html5
   [:body
    (form/form-to [:post (str "/clients/new/" (db/get-next-id))]

                  (form/label "name" "Name: ")
                  (form/text-field "name" "ime")
                  (form/label "phone" "Phone: ")
                  (form/text-field "phone" "telefon")
                  (form/hidden-field "id" (db/get-next-id))
                  (anti-forgery-field)

                  (form/submit-button "Save changes"))]))
(defn add-new-client [client]
  (db/add-client client))