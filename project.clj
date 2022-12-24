(defproject projekat "0.1.0-SNAPSHOT"
  
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [compojure "1.7.0"];dep za defroutes fju za definisanje ruta
                 [ring/ring-jetty-adapter "1.9.6"];dep za pokretanje servera u sopstvenom browseru i podrzava http req
                 [hiccup "1.0.5"];dep za renderovanje html sadrazaja preko clojure-a 
                 [org.clojure/java.jdbc "0.7.12"];dep za rad sa bazom 
                 [mysql/mysql-connector-java "5.1.18"];treba mi ovaj dep jer gornji dep ne ucitava konektor
                 [ring/ring-anti-forgery "1.3.0"];returns the HTML for the anti-forgery field
                 [ring/ring-defaults "0.3.4"]
                 [ring/ring-devel "1.8.0"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [hiccup-table "0.2.0"]

                 ]
  :plugins [[lein-ring "0.12.6"]]
  :main projekat.server-config
  :ring {:handler projekat.core/app-handler};konfigurisem server a pozivam ga naredbom lein ring server
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                   [ring/ring-mock "0.3.2"]]}}
  )
