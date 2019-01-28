/*
 *  User.java
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

public class User {
	 // ====================
    // ATTRIBUTES
    // ====================
	private String name;
	private String phoneNumber;
	private String telegramToken;
	private ArrayList<Integer> stations;

	// ====================
	// PUBLIC METHODS
	// ====================
	public User() {
	}

	public User(String name, String phoneNumber, String telegramToken, ArrayList<Integer> stations) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.telegramToken = telegramToken;
		this.stations = stations;
	}
	
	public boolean addStation(Integer i) {
		return stations.add(i);
	}

	// ====================
	// GETTERS & SETTERS
	// ====================
	public ArrayList<Integer> getStations() {
		return stations;
	}

	public void setStations(ArrayList<Integer> stations) {
		this.stations = stations;
	}

	public String getTelegramToken() {
		return telegramToken;
	}

	public void setTelegramToken(String telegramToken) {
		this.telegramToken = telegramToken;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", phoneNumber=" + phoneNumber + ", telegramToken=" + telegramToken
				+ ", stations=" + stations + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}

}
