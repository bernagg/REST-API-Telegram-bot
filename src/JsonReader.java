/*
 *  JsonReader.java
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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JsonReader implements Job {
	// ====================
	// ATTRIBUTES
	// ====================
	Client client = ClientBuilder.newClient();

	/**
	 * This Job downloads a JSon Object from http://wservice.viabicing.cat/v2/stations, after that 
	 * sets the content using a Stations class to the attribute [List<Station> stations] inside [BicingFreeSlotsNotifier] class.
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		WebTarget target = client.target("http://wservice.viabicing.cat/").path("v2/stations");
		Stations stations = target.request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<Stations>() {
		});
		BicingFreeSlotsNotifier.setStations(stations.getStations());
	}
}
