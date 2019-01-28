/*
 *  Stations.java
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

import java.util.List;

public class Stations {
	 // ====================
    // ATTRIBUTES
    // ====================
	private List<Station> stationsList;

	// ====================
    // CONSTRUCTORS
    // ====================
	public Stations() {
	}

	public Stations(List<Station> stations) {
		this.stationsList = stations;
	}

	// ====================
	// GETTERS & SETTERS
	// ====================
	public List<Station> getStations() {
		return stationsList;
	}

	public void setStations(List<Station> stations) {
		this.stationsList = stations;
	}
	
	public Station getStationById(int id){
		for (Station s : BicingFreeSlotsNotifier.getStations())
			if (s.getId() == id)	return s;
		return null;
	}

	@Override
	public String toString() {
		return "Stations [stationsList=" + stationsList + "]";
	}
}
