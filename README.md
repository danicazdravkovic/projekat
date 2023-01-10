# projekat

## Application's goal

The goal of the application is to enable easier recalculation of the total debt of clients and to provide the admin an overview of the most frequently booked massages through a graph. It was interesting for me to conect admin's activities with clients, so I made a .txt file where some admin's changes are note down.

# ROUTES
## Index page "/"
Main route is "/" route. This route actually opens blank page with a menu for logging in, signing up or continuing without logging.
User can log in as an admin or as a client. 
User can sign up, too. When the user sign up as a client, he will be redirected to log in as a client. 
Logging in isn't necessary, so the user can continue without logging in.

## "/index" page
If the user wants to continue without logging in, he wiil be redirected to an index page, where he can see all massages for the spa center, their desctiptions and prices.

## Logging in examples 
In a project, there are 2 roles for now(admin and client).
 ##### Client credentials to log in for example: 
Phone: 0679089700
Password: nevena
or you can use your own credentials after you sign up as a client.

 ##### Admin credentials to log in are always:
Username: admin
Password: admin
## "/admin/login" and "/client-login"
These routes open log in forms, for an admin and a client. 
If credentials aren't appropriate, both forms will show message: "Invalid phone or password". If credentials are appropriate, client is redirected to "/index-client" page, and admin is redirected to "/index-admin" page.

## "/client-sign-up"
This route opens form for client's signing up. All fields in this form are required. 

"Name and surname" field must contain only letters, "Phone" field must contain only digits, and "Password" field can contain letters and fields.

If all fields aren't filled, the message "Fill all fields." will be shown.
If "name and surname" filled contains digits, the message "Name must contain only letters." will be shown.
If "Phone" filled contains letters, the message "Phone must contain only digits." will be shown.
********************
# Admin functions (available only when an admin is logged in):
Route "/index-admin" is main clients's route. This route shows plain text "Hello admin" and a menu with these items:

    1.New massage (route:"/massages/new")
    Admin can add a new masssage with its name, descrption and price. 
    All field are required. Name can't contain digit and price can't contain letter.
    If any condition isn't met, then these messages will be shown:
    "Fill all fields."
    "Name must contain only letters."
    "Price must contain only digits."

    2.Clients (route: "/clients")
    Admin can see the list of clients (with their names, phones and amounts),
    delete clients, and see a massage      reservations graph. 
    This graph shows amount of reservations every massage has.This graph can be useful because admin can see which massage is most popular.
    
    3.Reservation table (route:"/reservations")
    Admin can see reservation table, where is stored client's id and massage's id.
    
    4.Massages ("/admin/massages")
    Admin can see the list of massages (their names, descriptions and prices) and can change them. 
    If he changes any massage, all clients will be noticed that the change had happened. These changes are written into a text file resources/news.txt. This text file is available for clients, too.
    
    5.Log out (route: "/admin/logout")
    Admin can log out, and then user can log in as an admin or as a client again.

# Client functions (available only when a client is logged in):

Route "/index-client" is main client's route. This route shows plain text "Hello " and client's name, client's amount of bought massages and list of massages with massages' names, descriptions and price. When client clicks "BUY" button, the amount will increase and new reservation will happen. Reservation will be added to reservation table(visible for admin only) with client's and massage's id. Page "/index-client" contains a menu with these items:
    
    1.Edit client (route: "/client-edit/edit/:id")
    Current information will be shown when the form is opened. Client can change his information(name, phone, password). 
    
    2.News (route: "/client/client/news")
    Client can read news from resources/news.txt file, whenever admin makes a change. Admin can change some massage information, price for example, which can be important for a client, but also massage's name and description. Newness are written as a small messages with date and time when they are created and which changes had happened.

    3.Log out 
    Client can log out, and then user can log in as an admin or as a client again.

# Testing
All tests written for the app are in "test\projekat" file.

##### core_test.clj
Test "name-phone-password-id" checks if the text which is slurped from the body of HTTP request is divided into name, phone, password and id strings and saved as a map. This is important because :body is stored as an object in HTTP request, and I needed to acces that object somehow. I did it with a slurp function and I got text like this: "nametf=Nevena+Arsic&phonetf=0000&passwordtf=0000&id=000__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*") so I needed to take only those string important for me (those which refer to name, phone, password and id).

Test "validate-name-phone-password-id" checks if fields from map are correct. If all is right, fucntion (validate-name-phone-password-id) will return "".
If the name contains digits, function returns message "Name must contain only letters".
If the phone contains letters, function returns message "Phone must contain only digits". If all field aren't filled, function returns message "Fill all fields."
Messages can be combined if more conditions are fulfilled.

Test "prepare-massage" checks if function (prepare-massage) returns map with appropriate name, description, price and id. Function should split the text from request's body, and get only those strings where name, description, price and id are stored. This test does similar thing to "name-phone-password-id" test.

Tests "prepare-admin" and "prepare-client" are very similar to "prepare-massage" and "name-phone-password-id" tests. They are used to get text from form input fields in a certain format good enough for further usage.

Test "validate-massage" checks if fields from map are correct. If all is right, fucntion (validate-massage) will return "".
If the price is nill, function returns message "Fill all fields. Price must contain only digits."If the name contains digits, function returns message "Name must contain only letters". Messages can be combined if more conditions are fulfilled.
  
##### database_test.clj

Test "get-client-by-id" shows that when non-existent id in a database is a parametar of (get-client-by-id) function, it returns message: "Exception method: database/get-client-by-id".

Test "get-id-by-phone" shows that when non-existent phone a database is a parametar of ("get-id-by-phone") function, it returns message: "Exception method: database/get-id-by-phone", and when phone is existant, it returns required id.

Test "check-login " shows that when non-existent login credentials are parametars of (check-login) function, it returns message: "Exception method: database/check-login", and when credentials are existant, it returns required client's information as a map.

##### massage_database_test.clj

Test "get-massage-by-id" shows that when non-existent id in a massage_database is a parametar of (get-massage-by-id) function, it returns message: "Exception method: massage_data_base/get-massage-by-id". When id is existant, function returns required massage's information as a map.

Test "get-massage-id-by-name" shows that when non-existent name in a massage_database is a parametar of (get-massage-id-by-name) function, it returns message: "Exception method: massage_data_base/get-massage-id-by-name", and when name is existant, it returns required id.

Test "get-massage-price-by-id" shows that when non-existent id is a parametar of (get-massage-price-by-id) function, it returns message: "Exception method: massage_data_base/get-massage-price-by-id", and when id is existant, it returns required price.

##### reservation_database_test.clj

Test "client-reservations" shows that when non-existent id is a parametar of (client-reservations) function, it returns empty list.

Test "client-amounts" shows that when non-existent id is a parametar of (client-amounts) function, it returns empty list.

Test "total-client-amount" shows that when non-existent id is a parametar of (total-client-amount) function, it returns 0.

Test "number-of-reservations" shows that when non-existent id is a parametar of (number-of-reservations) function, it returns 0.

# Databases
All databases are stored in resources folder as sqlite databases. 
Client database stores client's information (id, name, phone, amount, password, role).
Massage database stores massage's information (name, description, price).
Reservation database stores reservation's information (id, client_id, massage_id).

Functions for working with these databases are in files: src\database, src\massage_data_base, src\reservation_database.

# Problems I ran into
One of biggest problems I ran into was problem with POST method, because I couldn't find way to read data from request body. Data were stored as an object, because I didn't construct http response at all, which I found out later. I was searching for weeks, and finally this post: https://stackoverflow.com/questions/37397531/ring-read-body-of-a-http-request-as-string solved my problem.
Second problem was connecting to sqlite database, because I have never used any other database except MySql. I fount this post https://stackoverflow.com/questions/613929/how-do-i-connect-to-a-mysql-database-from-clojure really useful and as soon as possible implemented that into my app.
When I was thinking about roles, I had no idea how to implement that. Then professor told me to research sessions, middlewares and so on... Sessions were familiar to me, but middlewares were too abstract. Step by step, reading articles about middlewares like https://luminusweb.com/docs/sessions_cookies.html I learnt enough for my needs. Then it took time to make app's context, I couldn't find way to separate app's routes. Firstly I had imagined my app as a single user app, and when I added new roles the whole context had to change. It was hard to decide which routes are for client and which are for an admin. It was on me to arrange them, and after few days I did it.

Besides the hard stuff, there are easy ones. Tests were easy to write, database functions too. Working on html elements was known to me and relatively easy. As these things were easy, I finished them in a few days.

### Future work

It were two months of hard work, and it paid off. I learnt new programming language with different rules. My future ambitions are to make more effective, more readable and shorter code. I know that it's not that perfect, but I am satisfied that I made something by myself.

# References
Clojure For the Brave and True, Daniel Higginbotham 
https://www.baeldung.com/clojure-ring
https://functionalhuman.medium.com/compojure-clout-tutorial-cf2f644abc71
https://github.com/johngrib/example-clojure-sqlite/blob/main/src/step01/simple_query.clj
https://clojars.org/metasoarous/oz for making graph.
Plenty of Stack overflow articles
Plenty of GitHub repositories
Youtube
Web Development with Clojure, Dmitri Sotnikov

## License

Copyright © 2022 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
