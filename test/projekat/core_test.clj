(ns projekat.core-test
  (:require [clojure.test :refer :all]
            [projekat.core :refer :all]
            [midje.sweet :refer [facts =>]]))

(facts "name-surname"
       (name-surname 2) => "2"
       
       )
