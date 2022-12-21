(ns projekat.server-config
  (:require 
   [ring.adapter.jetty :as jetty]
   [compojure.handler :as handler]
   [projekat.core :as core]
   )
  )
;MORAMO MAIN DA DODAMO U PROJECT FAJLU
;APLIKACIJA SE POKRECE KOMANDOM lein run
;u terimnalu kucamo SERVER_PORT=3035 lein run AKO NECEMO DA BUDE NA 3000
(defn -main [& args]
  
   
    (jetty/run-jetty (handler/site #'core/wrapping)
                     {:port 3000
                      :join? false})
    
  )
