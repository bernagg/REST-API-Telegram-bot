/*
 *  TwitterPublisher.java
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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterPublisher implements Job {

	// ====================
	// ATTRIBUTES
	// ====================
	private final String CONSUMER_KEY = "NULL";
	private final String SECRET_KEY = "NULL";
	private final String ACCESS_TOKEN = "NULL";
	private final String ACCESS_TOKEN_SECRET = "NULL";

	Client client = ClientBuilder.newClient();

	// ====================
	// PUBLIC METHODS
	// ====================
	
	/**
	 * This function is responsible of all communication with Twitter's API and our Twitter account.
	 * It executes a HTTP GET request to our API to get current stations statistics, and selects the station with the lowest frequency (the fastest one running out of bikes).
	 * Then, we post a tweet composed of information of the selected station and its frequency.
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		TwitterFactory factory = new TwitterFactory();
		Twitter twitter = factory.getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, SECRET_KEY);
		twitter.setOAuthAccessToken(new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET));

		WebTarget targetGet = client.target("http://localhost:15000").path("/bicing/getStatistics");
		Response query = targetGet.request(MediaType.APPLICATION_JSON_TYPE).get();

		if (query.getStatus() == 200) {
			JsonArray freqs = Json.parse(query.readEntity(String.class)).asArray();
			JsonObject finalObject = null;
			String minutes = null;
			String seconds = null;
			
			for(int i = 0; i < freqs.size(); i++) {
				JsonObject obj = freqs.get(i).asObject();
				JsonObject st = obj.get("s").asObject();
				String[] stFreq = obj.get("freq").asString().split(" ");
				String m = stFreq[0];
				String s = stFreq[2];

				if (minutes == null && seconds == null){
					minutes = m;
					seconds = s;
					finalObject = obj;
				} else if(Integer.parseInt(m) < Integer.parseInt(minutes) || (m.equals(minutes) && Integer.parseInt(s) < Integer.parseInt(seconds))) {
					minutes = m;
					seconds = s;
					finalObject = obj;
				}
			}
			
			JsonObject station = finalObject.get("s").asObject();
			String stFreq = finalObject.get("freq").asString();

			if (!finalObject.isEmpty())
				try {
					twitter.updateStatus(
							station.get("streetName").asString() + " " + station.get("streetNumber").asString()
									+ " station frequency is " + stFreq.substring(1, stFreq.length()) + (Integer.parseInt(station.get("bikes").toString())>1 ? ". There are " + station.get("bikes") + " bikes left." : ". There is " + station.get("bikes") + " bike left."));
				} catch (TwitterException e) {
					e.printStackTrace();
				}
		}
	}
}
