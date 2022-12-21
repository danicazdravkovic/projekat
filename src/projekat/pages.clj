(ns projekat.pages
  (:require
   [hiccup.page :refer [html5]]
   [hiccup.form :as form]
   [ring.util.anti-forgery :refer (anti-forgery-field)]
   [projekat.database :as db]))

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
        [:a.nav-item.nav-link {:href "/clients/new"} "New client"]
        [:a.nav-item.nav-link {:href "/admin/login"} "Log in"]
        [:a.nav-item.nav-link {:href "/admin/logout"} "Log out"]

       ]
      
      ]


     body]]))


(defn index [body]
  (base-page body))

(defn client-view [{id :id name :name phone :phone}]
  (html5 
   
    [:li (format "Name: %s          Phone: %s" name phone)] 
    (form/form-to
   [:post (str "/client-delete/delete/" id )]
   (anti-forgery-field)
   (form/submit-button  {:class "btn btn-danger"} "Delete"))
   
    (form/form-to
     [:post (str "/client-edit/edit/" id)]
     (anti-forgery-field)
     (form/submit-button  {:class "btn btn-primary"} "Edit"))
    
   ))
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
                  [:div.form-group
                  (form/label "name" "Name: ")
                  (form/text-field {:class "form-control"} "name" (:name client))]
                  
                 [ :div.form-group
                  (form/label "phone" "Phone: ")
                  (form/text-field {:class "form-control"} "phone" (:phone client))
                  (form/hidden-field "id" (:id client))]
                  (anti-forgery-field)

                  (form/submit-button {:class "btn btn-primary"} "Save changes"))]))
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

(defn admin-login [& [message]]
  (html5
   ;[:head [:titile "Admin login"]]
   (when message
     [:div.alert.alert-danger message])
   [:body [:h1 "Admin login"]
    (form/form-to [:post (str "/admin/login")]
                  
                 [:div.form-group 
                  (form/label "usernamel" "Username: ")
                  (form/text-field {:class "form-control"} "logintf")
                  ]
                  
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
                  (form/text-field "name" "ime")
                  (form/label "phone" "Phone: ")
                  (form/text-field "phone" "telefon")
                  (form/hidden-field "id" (db/get-next-id))
                  (anti-forgery-field)

                  (form/submit-button "Save changes"))]))
(defn add-new-client [client]
  (db/add-client client))