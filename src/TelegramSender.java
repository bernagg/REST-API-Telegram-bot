/*
 *  TelegramSender.java
 *  
 *  This file is part of DAD project.
 *  
 *  Bernabe Gonzalez Garcia <bernabegoga@gmail.com>
 *  Aitor Cubeles Torres <mail@aitorct.me>
 *
 *   DAD project is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   DAD project is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with DAD project.  If not, see <http://www.gnu.org/licenses/>. 
 */

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class TelegramSender implements Job {

	// ====================
	// ATTRIBUTES
	// ====================
	static Client client = ClientBuilder.newClient();	
	static String TOKEN = "NULL";
	Offset offset = new Offset(0);

	// ====================
	// PUBLIC METHODS
	// ====================
	
	/**
	 * This method sends a message (the input text) to a determined chat_id (a.k.a user).
	 * 
	 * @param chat_id, attribute telegramToken from [User] Object or parsing from an incoming Telegram message and getting user information.
	 * @param text, [String] Object that contains the text message to be sent.
	 * 
	 * @return [Response] object
	 */
	static Response sendMessage(long chat_id, String text) {
		Message message = new Message(chat_id, text);
		WebTarget targetSendMessage = client.target("https://api.telegram.org").path("/bot" + TOKEN + "/sendMessage");
		Response response = targetSendMessage.request().post(Entity.entity(message, MediaType.APPLICATION_JSON_TYPE));
		return response;
	}

	/**
	 * This method checks for new messages received by our bot using Telegram's API
	 * 
	 * @return [Response] object
	 */
	Response receiveUpdates() {
		WebTarget targetGetUpdates = client.target("https://api.telegram.org").path("/bot" + TOKEN + "/getUpdates");
		Response response = targetGetUpdates.request().post(Entity.entity(offset, MediaType.APPLICATION_JSON_TYPE));
		return response;
	}

	/**
	 * This is the core function of our Telegram bot: it's always listening for new messages, processing them and establishing communication with users.
	 * We parse the whole Json given by Telegram to receive updates, which includes the sent messages or users' information, like name or Telegram token.
	 * It processes the messages sent by users to our bot, checking if either command help, start, searchStation or subscribe has to be triggered.
	 * 
	 *	/help: sends usage information to the user
	 *	/join: lets the user register its account in our app in order to be able to use other bot commands
	 *	/start: it's the first message sent, when the user starts the bot for the first time. Contains bot's description and brief usage instructions.
	 *	/searchStation: the user is able to search for a specific station finding all its information just by typing the street name and number, the whole address or just part of it. We'll send all matches.
	 *	/subscribe: we create a new [User] object, filling it with the user's Telegram profile information and subscribing it to the desired stations.
	 *	/subscriptions: sends a notification to the user which shows updated information about users' subscribed stations.
	 * 
	 * We have to keep updating the offset number to let Telegram know up to which message we've read, avoiding reading these again. 
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		while (true) {
			Response response = receiveUpdates();
			if (response.getStatus() == 200) {
				JsonObject json = Json.parse(response.readEntity(String.class)).asObject();
				JsonArray results = json.get("result").asArray();
				if (!results.isEmpty()) {
					for (int i = 0; i < (results.size()); i++) {
						System.out.println(i + " - " + results.get(i));
						JsonObject userMessage;
						if (results.get(i).asObject().get("message") == null) {
							userMessage = results.get(i).asObject().get("edited_message").asObject();
						} else {
							userMessage = results.get(i).asObject().get("message").asObject();
						}

						JsonObject userInfo = userMessage.get("from").asObject();
						int user_id = Integer.parseInt(userInfo.get("id").toString());
						String text = userMessage.get("text").toString();
						String[] split = text.substring(1, text.length() - 1).split(" ");
						String selection = split[0];
						switch (selection) {
						case ("/help"):
							sendMessage(user_id,
									"ℹ️️ I can help you staying up to date of your favourite Bicing stations with real-time information and statistics.\n\n"
											+ "You can control me by sending these commands:\n\n"
											+ "/start - Bot description and basic usage\n"
											+ "/join [phoneNumber] - Register your account to be able to use other commands\n"
											+ "/subscribe [stationID] - Subscribe to stations you want to be up to date\n"
											+ "/searchStation [street] - Search and get station's info, you might need this when subscribing to new stations\n"
											+ "/subscriptions - Get up to date information of your subscribed stations\n");
							break;
						case ("/start"):
							sendMessage(user_id,
									"🔴 *Welcome to Bicing Stats* 🚲\n\n"
											+ "Bicing Stats provides you with information 📋 of your favourite stations, like free slots or available bikes 📈\n"
											+ "Subscribe to stations and we will keep you updated 📨 with info and some cool statistics 🙃\n\n"
											+ "🚦 Start by joining us! Just type /join [phoneNumber] and you'll be ready to use all our functionalities!\n"
											+ "Then, search the station you use the most or the closest one 🏠 by typing /searchStation [street]\n"
											+ "Once you find your station, note the station ID and subscribe to it using the command /subscribe [stationID]\n\n"
											+ "🔃 Get real-time information of your subscribed stations by executing the command /subscriptions\n\n"
											+ "ℹ️️ Use command /help for more information\n");
							break;

						case ("/searchStation"):
							if (split.length > 1) {
								if (split[1] != null) {
									List<Station> sts = BicingFreeSlotsNotifier.getStations();
									String query, message;
									query = message = "";

									for (int j = 1; j < split.length; j++) {
										query += split[j];
										if (split.length > 2 && j != (split.length) - 1)
											query += " ";
									}

									for (Station s : sts)
										if (s.getStreetName().toLowerCase().contains(query.toLowerCase()))
											message += s.telegramText();

									sendMessage(user_id, message);
								}
							}

							break;

						case ("/subscribe"):
							if (split.length < 2)
								sendMessage(user_id,
										"⚠️ A station ID is mandatory. Please, use the command as follows: /subscribe [stationID]");
							else if (!Utils.allMembersAreDigits(split, 1))
								sendMessage(user_id,
										"⚠️ The station ID is a number that identifies the station. Please, use the command /searchStation [street] to get the proper station ID and then execute again this command as follows: /subscribe [stationID]");
							else {
								String firstName = userInfo.get("first_name").toString();
								String lastName = "non-lastname";
								if (userInfo.get("last_name") != null)
									lastName = userInfo.get("last_name").toString();
								String name = firstName.substring(1, firstName.length() - 1) + " "
										+ lastName.substring(1, lastName.length() - 1);
								String phoneNumber = null;
								List<Station> sts = BicingFreeSlotsNotifier.getStations();
								while (sts.isEmpty())
									sts = BicingFreeSlotsNotifier.getStations();

								ArrayList<Integer> finalStations = new ArrayList<Integer>();
								String message = "";
								for (int k = 1; k < split.length; k++) {
									finalStations.add(Integer.parseInt(split[k]));
									message += split[k];
									if (split.length > 3 && k < (split.length) - 2)
										message += ", ";
									else if (k == (split.length) - 2)
										message += " and ";
								}
								
								WebTarget targetGet = client.target("http://localhost:15000").path("/users/getUsers");
								Response query = targetGet.request(MediaType.APPLICATION_JSON_TYPE).get();
								JsonArray users = Json.parse(query.readEntity(String.class)).asArray();
								JsonObject finalUser = null;
								
								for(int j = 0; j < users.size(); j++) {
									JsonObject user = users.get(j).asObject();
									if(user.get("telegramToken").asString().equals(Integer.toString(user_id))) {
										phoneNumber = user.get("phoneNumber").asString();
										finalUser = user;
									}	
								}
								
								if(finalUser == null)
									sendMessage(user_id, "⚠️ ERROR: You are not yet registered. Use command /join [phoneNumber]");
								else {
								User newUser = new User(name, phoneNumber, Integer.toString(user_id), finalStations);

								WebTarget target = client.target("http://localhost:15000").path("users/add");
								Response addUser = target.request()
										.post(Entity.entity(newUser, MediaType.APPLICATION_JSON_TYPE));
								if (addUser.getStatus() == 200)
									sendMessage(user_id, (finalStations.size()>1 ? "✅ SUBSCRIBED TO STATIONS " : "✅ SUBSCRIBED TO STATION ") + message);
								else
									sendMessage(user_id, "⚠️ ERROR: Please, try again later.");
								}
							}
							break;
							
						case ("/subscriptions"):
							WebTarget targetGet = client.target("http://localhost:15000").path("/users/getUsers");
							Response query = targetGet.request(MediaType.APPLICATION_JSON_TYPE).get();
							JsonArray users = Json.parse(query.readEntity(String.class)).asArray();
							boolean userFound = false;
							
							for(int j = 0; j < users.size(); j++) {
								JsonObject user = users.get(j).asObject();
								if(!user.get("stations").toString().equals("[]"))
									if(user.get("telegramToken").asString().equals(Integer.toString(user_id))) {
										WebTarget targetNotify = client.target("http://localhost:15000").path("/users/notify/"+user.get("phoneNumber").asString());
										targetNotify.request(MediaType.APPLICATION_JSON_TYPE).get();
										userFound = true;
									}	
							}
							
							if(!userFound)
								sendMessage(user_id, "⚠️ ERROR: You are not subscribed to any station. Use command /subscribe [stationID]");
							
							break;
						case ("/join"):
							if (split.length > 1) {
								if (split[1] != null) {
									String firstName = userInfo.get("first_name").toString();
									String lastName = "non-lastname";
									if (userInfo.get("last_name") != null)
										lastName = userInfo.get("last_name").toString();
									String name = firstName.substring(1, firstName.length() - 1) + " "
											+ lastName.substring(1, lastName.length() - 1);
									String phoneNumber = split[1];
									ArrayList<Integer> finalStations = new ArrayList<Integer>();

									User newUser = new User(name, phoneNumber, Integer.toString(user_id), finalStations);

									WebTarget target = client.target("http://localhost:15000").path("users/add");
									Response addUser = target.request()
											.post(Entity.entity(newUser, MediaType.APPLICATION_JSON_TYPE));
									if (addUser.getStatus() == 200)
										sendMessage(user_id, "✅ Welcome to Bicing Stations! You are ready to use the bot. ");
									else
										sendMessage(user_id, "⚠️ ERROR: Please, try again later.");
								}
							}
							break;
						}

					}
					
					offset.setOffset(
							Integer.parseInt(results.get(results.size() - 1).asObject().get("update_id").toString())
									+ 1);
					System.out.println("NEW OFFSET: " + offset.getOffset());
				}

			}
		}
	}

}
