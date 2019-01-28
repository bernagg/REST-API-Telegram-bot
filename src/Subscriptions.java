/*
 *  Subscriptions.java
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subscriptions {
	 // ====================
    // ATTRIBUTES
    // ====================
	private Map<Station, ArrayList<User>> subscriptions;

	// ====================
    // CONSTRUCTORS
    // ====================
	public Subscriptions() {
		subscriptions = new HashMap<Station, ArrayList<User>>();
	}
	
	// ====================
	// PUBLIC METHODS
	// ====================
	
	/**
	 * This method adds a Subscriber Object to the attribute [Map<Station, ArrayList<User>> subscriptions].
	 * 
	 * Because of the Map type, it takes care about if a Station Object exists or not yet.
	 * 
	 * @param idStation, an attribute Id from [Station] Object.
	 * @param user, an [User] Object.
	 */
	public void addSubscriptor(int idStation, User user) {
		Stations stations = new Stations(BicingFreeSlotsNotifier.getStations());
		Station s = stations.getStationById(idStation);
		
		if (subscriptions.containsKey(s)) {
			if(!subscriptions.get(s).contains(user)) {
				subscriptions.get(s).add(user);
			};
		} else {
			ArrayList<User> listUsers = new ArrayList<User>();
			listUsers.add(user);
			subscriptions.put(s, listUsers);
		}
	}

	public ArrayList<User> getUsersSubToStationById(int id) {
		Stations sts = new Stations(BicingFreeSlotsNotifier.getStations());
		Station s = sts.getStationById(id);
		ArrayList<User> u = subscriptions.get(s);
		return u;
	}
	
	/**
	 * This method returns an ArrayList<Station>, represents those Stations where some Users are subscribed.
	 * Using an attribute [phoneNumber] from [User] object like a key.
	 * 
	 * @param phoneNumber, a String.
	 * @return an ArrayList<Station>.
	 */
	public ArrayList<Station> getStationsByPhone(String phoneNumber) {
		List<User> users = BicingFreeSlotsNotifier.getUsers();
		String userName = null;
		for (User u : users) {
			if (u.getPhoneNumber().equals(phoneNumber)) userName = u.getName();
			break;
		}
		ArrayList<Station> sts = new ArrayList();
		for (Map.Entry<Station, ArrayList<User>> entry : subscriptions.entrySet()) {
			Station s = entry.getKey();
			ArrayList<User> usersEntry = entry.getValue();
			for (User u : usersEntry) {
				if (u.getPhoneNumber().equals(phoneNumber)) sts.add(s);
			}
		}
		return sts;
	}
	

	public Map<Station, ArrayList<User>> getSubscriptions() {
		return subscriptions;
	}

	@Override
	public String toString() {
		return "Subscriptions [subscriptions=" + subscriptions + "]";
	}
}
