# projekat
Main route is "/" route where user can log in as an admin or as a client, or sign up as a client.
In a project, there are 2 roles for now(admin and client).
Client credentials to log in for example: 
Phone: 0679089700
Password: nevena
or you can use your own credentials after you sign up as a client.
Admin credentials to log in are always:
Username: admin
Password: admin
********
# Admin functions:
Admin can add a new masssage with its name, descrption and price. All field are required.
Name can't contain digit and price can't contain letter.
Admin can see the list of clients, delete clients, and see a massage reservations graph. This graph shows amount of reservations every massage has.
Admin can see reservation table, where is stored client's id and massage's id.
Admin can see the list of massages and can change them. If he change any massage, all clients will be noticed that the change had happened. News are stored in a .txt file resources/news.txt.
Admin can log out, and then user can log in as an admin or as a client.

# Client functions
Client's name and amount are written on main page /index-client.
Client can buy any massage. This action will increase total amount of a client.
Client can change his information(name, phone, password).
Client can read news from resources/news.txt file, when admin makes a change.
Client can log out, and then user can log in as an admin or as a client. 
When client sign up, all field are required, name can't contain digits, and phone can't contain letters.
# Testing
All tests are in test\projekat file

# Problems I ran into
One of biggest problems I ran into was problem with POST method, because I couldn't find way to read data from request body. Also, it took time to make app's context, I couldn't find way to separate app's route. Learning about middlewares and handlers was a hard job. It was a little bit boring making HTML elements, too.

# References
Clojure For the Brave and True, Daniel Higginbotham 
https://www.baeldung.com/clojure-ring
https://functionalhuman.medium.com/compojure-clout-tutorial-cf2f644abc71
https://github.com/johngrib/example-clojure-sqlite/blob/main/src/step01/simple_query.clj
https://clojars.org/metasoarous/oz
Stack overflow
Youtube
Web Development with Clojure, Dmitri Sotnikov
## Installation

Download from http://example.com/FIXME.

## Usage

FIXME: explanation

    $ java -jar projekat-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

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
