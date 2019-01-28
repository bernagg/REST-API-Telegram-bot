# Rest-API-Telegram-Bot
## Main Description
API Rest server implementation with a telegram bot client to interact with it. A small twitter bot has also been implemented.
## Project description
* BicingFreeSlotsNotifier: it’s the core of our app, since it’s the class that controls execution and manages every other single class of our application. It has the main function and several attributes and methods to manage all elements of our application, like Stations, Subscriptions, Users...
* JsonReader: executes an HTTP GET request to Bicing’s API to get the Json file containing the information of all the stations and then updates the Stations list of BicingFreeSlotsNotifier. It’s used by the Job defined in BicingFreeSlotsNotifier.
* Message: this class describes a message:  has a receiver (which is the ID of the destinatary user) and the text to be sent. It also describes which format is used to parse the text, in this case, Markdown is used. This class is used by TelegramSender when sending messages.
* Notification: this class is used to send notifications. It consists of a String named text, and its getters and setters as methods. Used by UserServices when sending a Telegram notification.
* Offset: objects of this class are used by TelegramSender. Offset consists of a unique attribute, an integer, plus getter and setters as methods. It’s needed to let Telegram know up to which message we have read. So, we have to update and post an Offset object every single time we execute a HTTP request to receive new messages.
* Station: One of the key classes for this project. Describes Station’s information, like id, street name, free slots, available bikes… it has two constructors, getters and setters, and a method to convert Station’s information to a String making them visually attractive for a reader.
* Stations: Groups station objects in its array. Two constructors, getters and setters, and a method that returns a Station given an ID.
* Statistics: class used to deal with statistics data. Here we process information to generate statistical information. Used by TwitterPublisher and TelegramSender.
* Subscriptions: manages users’ subscriptions. Adds, returns and has some other methods to filter results.
* TelegramSender: it’s the responsible of all communication between users and our app, using Telegram’s API. Sends, receives and processes user messages, parsing them and triggering some commands if it’s the case.
* TwitterPublisher: this class posts Tweets to our Twitter profile, using Twitter’s API. It processes the statistics Json file, and posts the station which has the lowest frequency value, together with some information like station full address and free bikes left.
User: all users have a name, phone number, Telegram token and also stations which they are subscribed to. We have these attributes plus getters and setters.
* Utils: extra methods that are used in some functions as small tools or utilities to make our functions look clearer.
* StationsServices: class that defines a REST API that works exclusively with stations data. You can find @POST and @GET methods which are used to process, add, and manage Stations. For more details, check BFSN REST API section from this report.
* UserServices: defines another REST API as well, but this time this one works exclusively with User objects.

## Rest API 
We have implemented the BFSN REST API using what we learned in the last seminars. We have two main classes called StationsServices and UsersServices. All the inputs and outputs are JSONObjects.

Their respectives paths are /bicing and /users:

In the API REST /bicing we can find the next methods:
* /Add: to add a new Station. ```http://localhost:15000/bicing/add```
* /getStations: to get all the stations. ```http://localhost:15000/bicing/getStations```
* /getStation/{id}: to get a station searching by its id as a parameter. ```http://localhost:15000/bicing/getStation/66```
* /getNearbyStations/{id}: to get the nearby stations from a concrete station with its id as a parameter. ```http://localhost:15000/bicing/getNearbyStations/66```
* /getStatistics: to get all the frequency activity from all the stations. ```http://localhost:15000/bicing/getStatistics```
* /getStationsSubs/{id}: get all subscribers from a concrete station by its concrete id as a parameter.```http://localhost:15000/bicing/getStationsSubs/2```

In /users we can find the next methods:
* /getUsers: get all users. ```http://localhost:15000/users/getUsers```
* /getSubsByName/{name}: get the Station list from an user. ```http://localhost:15000/users/getSubsByName/Berna```
* /add: add a new user. ```http://localhost:15000/users/add```
* /notify/{phoneNumber}: notify through Telegram to a specific phone number, which is a parameter. ```http://localhost:15000/users/notify/666666666```

## Telegram bot
/help: sends usage information to the user.

/start: it's the first message sent, when the user starts the bot for the first time. Contains bot's description and brief usage instructions.

/join: the user registers its phone number in our system. After that, the user is ready to use other commands.

/searchStation: the user is able to search for a specific station finding all its information just by typing the street name and number, the whole address or just part of it. We'll send all matches.

/subscribe: we create a new [User] object, filling it with the user's Telegram profile information and subscribing it to the desired stations.

/subscriptions: sends a notification to the user which shows updated information about users' subscribed stations.
