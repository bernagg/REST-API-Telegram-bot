/*
 *  Station.java
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

public class Station {
	// ====================
	// ATTRIBUTES
	// ====================
	private int id;
	private String type;
	private float latitude;
	private float longitude;
	private String streetName;
	private String streetNumber;
	private int altitude;
	private int slots;
	private int bikes;
	private String nearbyStations;
	private String status;

	// ====================
	// CONSTRUCTORS
	// ====================
	public Station() {
	}

	public Station(int id, String type, float latitude, float longitude, String streetName, String streetNumber,
			int altitude, int slots, int bikes, String nearbyStations, String status) {
		this.id = id;
		this.type = type;
		this.latitude = latitude;
		this.longitude = longitude;
		this.streetName = streetName;
		this.streetNumber = streetNumber;
		this.altitude = altitude;
		this.slots = slots;
		this.bikes = bikes;
		this.nearbyStations = nearbyStations;
		this.status = status;
	}

	// ====================
	// PUBLIC METHODS
	// ====================
	
	/**
	 * This method prepares a String to use it in [TelegramSender class].
	 * 
	 * @return a String.
	 */
	public String telegramText() {
		return "*Station " + id + "* - " + streetName + " " + streetNumber + "\n\n";
	}

	// ====================
	// GETTERS & SETTERS
	// ====================
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public int getSlots() {
		return slots;
	}

	public void setSlots(int slots) {
		this.slots = slots;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getBikes() {
		return bikes;
	}

	public void setBikes(int bikes) {
		this.bikes = bikes;
	}

	public String getNearbyStations() {
		return nearbyStations;
	}

	public void setNearbyStations(String nearbyStations) {
		this.nearbyStations = nearbyStations;
	}

	@Override
	public String toString() {
		return "Station [id=" + id + ", type=" + type + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", streetName=" + streetName + ", streetNumber=" + streetNumber + ", altitude=" + altitude
				+ ", slots=" + slots + ", bikes=" + bikes + ", nearbyStations=" + nearbyStations + ", status=" + status
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Station other = (Station) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
