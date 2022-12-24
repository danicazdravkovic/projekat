(ns projekat.pages
  (:require
   [hiccup.page :refer [html5]]
   [hiccup.form :as form]
   [ring.util.anti-forgery :refer (anti-forgery-field)]
   [projekat.database :as db]
   [projekat.massage_data_base :as massage_db]
   [hiccup.table :as table]
   [projekat.reservation_database :as reservations_db]))


(defn base-page [& body]
  (html5
   [:head [:title "SPA Center"]
    [:link
     {:rel "stylesheet"
      :href "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
      :integrity "sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
      :crossorigin "anonymous"}]]
   [:body
    [:div.container
     [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
      [:a.navbar-brand {:href "/"} "Spa center"]
      [:div.navbar-nav.ml-auto
       [:a.nav-item.nav-link {:href "/reservations/new"} "Make a reservation"]
       [:a.nav-item.nav-link {:href "/clients/new"} "New client"]
       [:a.nav-item.nav-link {:href "/massages/new"} "New massage"]
       [:a.nav-item.nav-link {:href "/admin/login"} "Log in"]
       [:a.nav-item.nav-link {:href "/admin/logout"} "Log out"]]]


     body]]))

(defn index [body]
  (base-page body))

(defn client-view [{id :id name :name phone :phone amount :amount}]
  (html5

   [:b (format "Name: %s" name)]
   [:br]
   (format "Phone: %s" phone)
   [:br]
   (format "Amount: %.2f" amount)
   [:br]
   (form/form-to
    [:post (str "/client-delete/delete/" id)]
    (anti-forgery-field)
    (form/submit-button  {:class "btn btn-danger"} "Delete"))
   (form/form-to
    [:post (str "/client-edit/edit/" id)]
    (anti-forgery-field)
    (form/submit-button  {:class "btn btn-primary"} "Edit"))
   [:hr]))
(defn clients-view [clients]
  (html5
   (map  client-view clients)))
(defn edit-client [client]
  (html5
   [:body
    [:p (:id client)]
    (form/form-to [:post (if client
                           (str "/client-edit/" (:id client))
                           "/clients")]
                  [:div.form-group
                   (form/label "name" "Name: ")
                   (form/text-field {:class "form-control"} "name" (:name client))]

                  [:div.form-group
                   (form/label "phone" "Phone: ")
                   (form/text-field {:class "form-control"} "phone" (:phone client))

                   (form/hidden-field "id" (:id client))]
                  (anti-forgery-field)

                  (form/submit-button {:class "btn btn-primary"} "Save changes"))]))


(defn admin-login [& [message]]
  (html5
   ;[:head [:titile "Admin login"]]
   (when message
     [:div.alert.alert-danger message])
   [:body [:h1 "Admin login"]
    (form/form-to [:post (str "/admin/login")]

                  [:div.form-group
                   (form/label "usernamel" "Username: ")
                   (form/text-field {:class "form-control"} "logintf")]

                  [:div.form-group
                   (form/label "passwordl" "Password: ")
                   (form/password-field {:class "form-control"} "passwordtf")]
                  (anti-forgery-field)
                  (form/submit-button {:class "btn btn-primary"} "Login"))]))



(defn new-client-form []
  (html5
   [:body
    (form/form-to [:post (str "/clients/new/" (db/get-next-id))]

                  (form/label "name" "Name: ")
                  (form/text-field "name")
                  (form/label "phone" "Phone: ")
                  (form/text-field "phone")
                  (form/hidden-field "id" (db/get-next-id))
                  (anti-forgery-field)

                  (form/submit-button "Save changes"))]))
; (defn add-new-client [client]
;   (db/add-client client))

(defn new-massage-form []
  (html5
   [:body
    (form/form-to [:post (str "/massages/new/" (massage_db/get-next-id))]

                  (form/label "name" "Name: ")
                  (form/text-field "nametf" "")
                  (form/label "desription" "Description: ")
                  (form/text-field "descriptiontf" "")
                  (form/label "price" "Price: ")
                  (form/text-field "pricetf" "")
                  (form/hidden-field "id" (massage_db/get-next-id))
                  (anti-forgery-field)

                  (form/submit-button "Save changes"))]))
(defn massage-index-page []
  (html5
   [:table
    [:tr
     [:th "Massages"]]
    [:tr
     [:th "Id"]
     [:th "Name"]
     [:th "Description"]
     [:th "Price [EUR]"]]
    (for [n (range 0 (count (massage_db/massages)))]
      [:tr
       [:td (:id (nth (massage_db/massages) n))]
       [:td (:name (nth (massage_db/massages) n))]
       [:td (:description (nth (massage_db/massages) n))]
       [:td (:price (nth (massage_db/massages) n))]])]))


(defn new-reservation-form []
  (html5
   [:body
    (form/form-to [:post (str "/reservations/new/" (reservations_db/get-next-id))]

                  (form/label "phone" "Phone: ")
                  (form/text-field "phonetf" "")
                  (form/label "massage" "Massage: ")
                  (form/text-field "massagetf" "")
                  (form/hidden-field "id" (reservations_db/get-next-id))
                  (anti-forgery-field)

                  (form/submit-button "Save changes"))]))
