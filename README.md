# CompNetworkProject

Computer networks assignment, showcasing use of client to server connection, allowing multiple clients to simultaneously update data held on a server and to retrieve the state of its data at any moment.

# How to run

in the compNetworkProject folder there is a src folder containing the following .java files:

BBoard.java - used for the board that contains notes sent from clients
client.java - contains the runnable client program that opens up a gui that allows the user to do multiple functions such as:
  1. connect/disconnect - will send a connection request based off entered server ip and port info
  2. post - will send a note to a server that will be kept safe as a note object on the board
  3. get - will send a request for a note to the server based off certain requirements (colour of note, location, what words it contains)
  4. clear - will send a request to server to clear all notes on board
  5. pin - will send a request to pin a given note/s
  6. shake - will clear all notes NOT pinned on board
  7. unpin - will request to unpin a note on board based off entered requirements
note.java - used for the note class
server.java - contains runnable server program that waits for client connections and provides responses and data based off requests from clients

note multiple clients can run at the same time and connect to 1 server
make sure to have server.java running before attempting to connect with client.java (otherwise will restult in an error)
