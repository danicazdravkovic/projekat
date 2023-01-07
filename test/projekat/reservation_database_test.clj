(ns projekat.reservation-database-test
    (:require [clojure.test :refer :all]
              [projekat.reservation_database :refer :all]
              [midje.sweet :refer [facts =>]])
  )

(facts "client-reservations"
       (client-reservations 11) => ())
(facts "client-amounts"
       (client-amounts 43) => ())
(facts "total-client-amount"
       (total-client-amount 43) => 0)
(facts "number-of-reservations"
       (number-of-reservations 43) => 0)