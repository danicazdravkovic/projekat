(ns projekat.database-test
  (:require [clojure.test :refer :all]
            [projekat.database :refer :all]
            [midje.sweet :refer [facts =>]]))

(facts "get-client-by-id"
       (get-client-by-id 555) => "Exception method: database/get-client-by-id")
(facts "get-id-by-phone"
       (get-id-by-phone "xxx") => "Exception method: database/get-id-by-phone"
       (get-id-by-phone "0652010059") => 7)
(facts "check-login "
       (check-login {:phone "090" :password "pass"}) => "Exception method: database/check-login"
       (check-login {:phone "0652010059" :password "dana"}) => {:id 7, :name "Danica Zdravkovic", :phone "0652010059", :amount 0.0, :password "dana", :role "client"}
       )