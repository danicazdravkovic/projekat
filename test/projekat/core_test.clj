(ns projekat.core-test
  (:require [clojure.test :refer :all]
            [projekat.core :refer :all]
            [midje.sweet :refer [facts =>]]))

(facts "name-phone-password-id"
       (name-phone-password-id
        "nametf=Nevena+Arsic&phonetf=0000&passwordtf=0000&id=0000&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*")
       => {:name "Nevena Arsic" :phone "0000" :password "0000" :id "0000"})

(facts "validate-name-phone-password-id"
       (validate-name-phone-password-id {:name "Nevena Arsic" :phone "0000" :password "0000" :id "0000"})
       => ""
       (validate-name-phone-password-id {:name "Nevena1 Arsic" :phone "0000" :password "0000" :id "0000"})
       => "Name must contain only letters. "
       (validate-name-phone-password-id {:name "Nevena Arsic" :phone "000a0" :password "0000" :id "0000"})
       => "Phone must contain only digits. "
       (validate-name-phone-password-id {:name "Neven1a Arsic" :phone "000a0" :password "0000" :id "0000"})
       => "Name must contain only letters. Phone must contain only digits. "
       (validate-name-phone-password-id {:name "" :phone "" :password "" :id ""})
       => "Fill all fields. "
       (validate-name-phone-password-id {:name "Danica Zdravkovic1" :phone "" :password "" :id ""})
       => "Fill all fields. Name must contain only letters. ")

; (prepare-massage "nametf=Relax&descriptiontf=Relax&pricetf=33&id=8&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*")

(facts "prepare-massage"
       (prepare-massage "nametf=Relax&descriptiontf=Relaxing&pricetf=44&id=8&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*")
       => {:name "Relax", :description "Relaxing", :price 44.0, :id "8"})
(facts "validate-massage"
       (validate-massage {:name "Relax" :description "Relaxing" :price 44.0 :id "8"})
       => ""
       (validate-massage {:name "Relax" :description "Relaxing" :price nil :id "8"})
       => "Fill all fields. Price must contain only digits. "
       (validate-massage {:name "Rel1ax" :description "Relaxing" :price "44" :id "8"})
       => "Name must contain only letters. ")
(facts "prepare-admin"
       (prepare-admin  "logintf=admin&passwordtf=admin&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*" )
       =>{:login "admin" :pass "admin"}
       )
(facts "prepare-client"
       (prepare-client  "phonetf=0652010059&passwordtf=dana&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*")
       => {:password "dana" :phone "0652010059"})