(ns projekat.pages
  (:require
   [hiccup.page :refer [html5]]
   [hiccup.form :as form]
   [ring.util.anti-forgery :refer (anti-forgery-field)]
   [projekat.database :as db]
   [projekat.massage_data_base :as massage_db]
   [hiccup.table :as table]
   [projekat.reservation_database :as reservations_db]
   [ring.middleware.session :as session]
   [oz.core :as oz]
   ))



(defn index-admin []
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
      [:a.navbar-brand {:href "/index-admin"} "Spa center"]
      [:div.navbar-nav.ml-auto
       [:a.nav-item.nav-link {:href "/massages/new"} "New massage"]
       [:a.nav-item.nav-link {:href "/clients"} "Clients"]
       [:a.nav-item.nav-link {:href "/reservations"} "Reservations table"] 
       [:a.nav-item.nav-link {:href "/admin/massages"} "Massages"]
       [:a.nav-item.nav-link {:href "/admin/logout"} "Log out"]
       ]]]
    "Hello admin"
    ]))
(defn index-client [session]
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
      [:a.navbar-brand {:href "/index-client"} "Spa center"]
      [:div.navbar-nav.ml-auto
       [:a.nav-item.nav-link {:href (format "/client-edit/edit/%d" (:id session))} "Edit client"]
       [:a.nav-item.nav-link {:href "/client/client/news"} "News"]
       [:a.nav-item.nav-link {:href "/client-logout"} "Log out"]
       ]]]
    
    "Hello " (:name (db/get-client-by-id (:id session)))
    [:br]
    "Total amount: " (:amount (db/get-client-by-id (:id session)))
    [:hr]
     [:table {:style "border:1px solid black;margin-left:auto;margin-right:auto;"}
      [:tr
       [:th "Massages"]]
      [:tr
       [:th "Id"]
       [:th "Name"]
       [:th "Description"]
       [:th "Price [EUR]"]
       [:th "BUY"]]
      (for [n (range 0 (count (massage_db/massages)))]
        [:tr
         [:td (:id (nth (massage_db/massages) n))]
         [:td (:name (nth (massage_db/massages) n))]
         [:td (:description (nth (massage_db/massages) n))]
         [:td (:price (nth (massage_db/massages) n))]
         [:td (form/form-to [:post (format "/reservations/new/%d/%d" (:id session) (:id (nth (massage_db/massages) n)))]

                            (anti-forgery-field)

                            (form/submit-button "BUY"))]])]
    ]))

(defn index[]
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
     [:a.navbar-brand {:href "/index"} "Spa center-login"]
     [:div.navbar-nav.ml-auto

      [:a.nav-item.nav-link {:href "/admin/login"} "Log in as an admin"]
      [:a.nav-item.nav-link {:href "/client-login"} "Log in as a client"]
      [:a.nav-item.nav-link {:href "/client-sign-up"} "Sign up as a client"]]]]
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
       [:td (:price (nth (massage_db/massages) n))]])]
   ])
  )
(defn login-page []
  (html5
   [:head [:title "SPA Center-login"]
    [:link
     {:rel "stylesheet"
      :href "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
      :integrity "sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
      :crossorigin "anonymous"}]]
   [:body
    [:div.container
     [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
      [:a.navbar-brand {:href "/"} "Spa center-login"]
      [:div.navbar-nav.ml-auto

       [:a.nav-item.nav-link {:href "/admin/login"} "Log in as an admin"]
       [:a.nav-item.nav-link {:href "/client-login"} "Log in as a client"]
       [:a.nav-item.nav-link {:href "/client-sign-up"} "Sign up as a client"]
       [:a.nav-item.nav-link {:href "/index"} "Continue without account"]
       
       ]]]]))

(defn admin-massages[]
  (html5
   [:body 
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
        [:td (:price (nth (massage_db/massages) n))]
         [:td (form/form-to [:post (format "/admin/change-massage/%d"  (:id (nth (massage_db/massages) n)))]

                            (anti-forgery-field)

                            (form/submit-button "CHANGE MASSAGE"))]
        ])
     ] 
    [:hr] 
    ])
  )

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
  ;  (form/form-to
    ; [:post (str "/client-edit/edit/" id)]
    (anti-forgery-field)
    ; (form/submit-button  {:class "btn btn-primary"} "Edit"))
   [:hr]))

;GRAPH
(defn data []
  ;amount of reservation for every massage
  (for [n (range 0 (count (massage_db/get-massage-names)))]
    {:Name (nth (massage_db/get-massage-names) n) 
     :Amount_of_reservations (nth (reservations_db/total-number-of-reserv) n)
     }))

(def line-plot
  {:data {:values (data)}
   :encoding {:x {:field "Name" :type "nominal"}
              :y {:field "Amount_of_reservations" :type "quantitative"}}
   :mark "bar"})

(defn admin-massage-graph[]
  (oz/start-server!)

  (oz/view! line-plot)
  )


(defn clients-view [clients]
  (html5 
   (map  client-view clients) 
   [:a {:href "/admin/graph"} "See a massage reservations graph"] 
   ))

(defn edit-client [client]
  (html5
   [:body
    (form/form-to [:post (if client
                           (str "/client-edit/" (:id client))
                           "/clients")]
                  [:div.form-group
                   (form/label "name" "Name: ")
                   (form/text-field {:class "form-control"} "nametf" (:name client))]

                  [:div.form-group
                   (form/label "phone" "Phone: ")
                   (form/text-field {:class "form-control"} "phonetf" (:phone client))
                  ]
                  [:div.form-group
                   (form/label "password" "Password: ")
                   (form/password-field {:class "form-control"} "passwordtf" (:password client))]

                   (form/hidden-field "id" (:id client))
                  
                  
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

(defn client-sign-up [& [message]]
  (html5
   [:body
    (form/form-to [:post (str "/client-sign-up/" (db/get-next-id))]
  (when message
    [:div.alert.alert-danger message])
                  (form/label "namel" "Name and surname: ")
                  (form/text-field "nametf")
                  (form/label "phonel" "Phone: ")
                  (form/text-field "phonetf")

                  (form/label "passwordl" "Password: ")
                  (form/password-field "passwordtf")

                  (form/hidden-field "id" (db/get-next-id))
                  (anti-forgery-field)

                  (form/submit-button "Save changes"))]))


(defn new-massage-form [& [message]]
  (html5
   [:body
     (when message
       [:div.alert.alert-danger message])
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
(defn edit-massage-form [massage_id]
  (html5
   [:body
    (form/form-to [:post (str "/admin/change-massage/massage/" massage_id)]

                  (form/label "name" "Name: ")
                  (form/text-field "nametf" "")
                  (form/label "desription" "Description: ")
                  (form/text-field "descriptiontf" "")
                  (form/label "price" "Price: ")
                  (form/text-field "pricetf" "")
                  (form/hidden-field "id" massage_id)
                  (anti-forgery-field)

                  (form/submit-button "Save changes"))]))

(defn reservation-index-page []
  (html5
   [:table
    [:tr
     [:th "RESERVATIONS"]]
    [:tr
     [:th "Id"]
     [:th "Client_id"]
     [:th "Massage_id"]]
    (for [n (range 0 (count (reservations_db/reservations)))]
      [:tr
       [:td (:id (nth (reservations_db/reservations) n))]
       [:td (:client_id (nth (reservations_db/reservations) n))]
       [:td (:massage_id (nth (reservations_db/reservations) n))]])]))


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
(defn client-login [& [message]]
  (html5
   (when message
     [:div.alert.alert-danger message])
   [:body [:h1 "Client login"]
    (form/form-to [:post (str "/client-login")]

                  [:div.form-group
                   (form/label "phonel" "Phone: ")
                   (form/text-field {:class "form-control"} "phonetf")]

                  [:div.form-group
                   (form/label "passwordl" "Password: ")
                   (form/password-field {:class "form-control"} "passwordtf")]
                  (anti-forgery-field)
                  (form/submit-button {:class "btn btn-primary"} "Login"))]))
(defn prepare-news[]
  (html5
   [:body
    [:p
    [:i  (clojure.string/replace  (slurp "resources/news.txt" :encoding "UTF-8") #"\n" "<br>")]
     ]
    ]
   )
  )
