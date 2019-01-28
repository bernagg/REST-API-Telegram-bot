/*
 *  UserServices.java
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

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

@Path("/users")
public class UserServices {
	// ====================
	// API REST
	// ====================
	
	@GET
	@Path("/getUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {
		return BicingFreeSlotsNotifier.getUsers();
	}

	@GET
	@Path("/getSubsByName/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Station> getUsers(@PathParam("name") String name) {
		User user = BicingFreeSlotsNotifier.getUserByName(name);
		List<Station> sts = BicingFreeSlotsNotifier.getStations();
		ArrayList<Integer> userIdSts = user.getStations();
		ArrayList<Station> stFinal = new ArrayList<Station>();

		for (Station s : sts)
			for (int i = 0; i < userIdSts.size(); i++)
				if (s.getId() == userIdSts.get(i))
					stFinal.add(s);

		return stFinal;
	}

	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public User addUser(User user) {
		if (BicingFreeSlotsNotifier.addUser(user)) {
			ArrayList<Integer> idStations = user.getStations();
			for (Integer i : idStations) {
				Stations st = new Stations(BicingFreeSlotsNotifier.getStations());
				Station s = st.getStationById(i);
				BicingFreeSlotsNotifier.addSubscription(s, user);
			}
			return user;
		}
		return null;
	}

	@GET
	@Path("/notify/{phoneNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public Notification notify(@PathParam("phoneNumber") String phoneNumber) {
		ArrayList<Station> sts = BicingFreeSlotsNotifier.getSubs().getStationsByPhone(phoneNumber);
		System.out.println(sts.size());
		User user = null;
		List<User> users = BicingFreeSlotsNotifier.getUsers();
		for (User u : users) {
			if (u.getPhoneNumber().equals(phoneNumber))
				user = u;
		}
		Notification n = null;
		String m = "";
		if (user != null) {
			
			Client client = ClientBuilder.newClient();
			WebTarget targetGet = client.target("http://localhost:15000").path("/bicing/getStatistics");
			Response query = targetGet.request(MediaType.APPLICATION_JSON_TYPE).get();
			JsonArray freqs = null;
			if (query.getStatus() == 200)
				freqs = Json.parse(query.readEntity(String.class)).asArray();
			
			for (Station s : sts) {
				m = m + "*Station " + s.getId() + "*\nAddress: " + s.getStreetName() + " " + s.getStreetNumber() + "\nAvailable bikes: " + s.getBikes() + "\nFree slots: " + s.getSlots() + " \n";
				if (freqs != null) {
					for(int i = 0; i < freqs.size(); i++) {
						JsonObject obj = freqs.get(i).asObject();
						JsonObject st = obj.get("s").asObject();
						if(st.get("id").toString().equals(Integer.toString(s.getId()))) {
							String[] stFreq = obj.get("freq").asString().split(" ");
							String min = stFreq[0];
							String sec = stFreq[2];
							m = m + "Frequency: " + min + "min, " + sec + "sec\n";
							break;
						}
					}
				}
				m = m + "\n";
			}
				n = new Notification(m);
		}
		TelegramSender.sendMessage(Long.valueOf(user.getTelegramToken()), m);
		return n;
	}
}
