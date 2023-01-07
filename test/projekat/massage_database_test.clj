(ns projekat.massage-database-test
  (:require [clojure.test :refer :all]
            [projekat.massage_data_base :refer :all]
            [midje.sweet :refer [facts =>]]))

(facts "get-massage-by-id"
       (get-massage-by-id 555) => "Exception method: massage_data_base/get-massage-by-id"
       (get-massage-by-id 1) => {:description "Gentle touch" :id 1 :name "Swedish" :price 77.0})
(facts "get-massage-id-by-name"
       (get-massage-id-by-name "Sport") => 4
       (get-massage-id-by-name "name") => "Exception method: massage_data_base/get-massage-id-by-name")
(facts "get-massage-price-by-id"
       (get-massage-price-by-id 1) => 77.0
       (get-massage-price-by-id 77) => "Exception method: massage_data_base/get-massage-price-by-id"
       )

