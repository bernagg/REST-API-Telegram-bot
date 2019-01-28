/*
 *  StationsServices.java
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
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

@Path("/bicing")
public class StationsServices {
	// ====================
	// API REST
	// ====================
	@POST
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Station addStation(Station station) {
		return BicingFreeSlotsNotifier.setStation(station);
	}

	@GET
	@Path("/getStations")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Station> getStations() {
		List<Station> sts = BicingFreeSlotsNotifier.getStations();
		if (sts != null)
			return sts;
		throw new WebApplicationException(204);
	}

	@GET
	@Path("/getStation/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Station getStation(@PathParam("id") int id) {
		List<Station> sts = BicingFreeSlotsNotifier.getStations();
		for (Station station : sts)
			if (station.getId() == id)
				return station;
		throw new WebApplicationException(204);
	}

	@GET
	@Path("/getNearbyStations/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Station> getNearbyStations(@PathParam("id") int id) {
		Stations sts = new Stations(BicingFreeSlotsNotifier.getStations());
		Station st = sts.getStationById(id);
		int[] nearbyStsID = Arrays.stream(st.getNearbyStations().toString().split(", ")).mapToInt(Integer::parseInt)
				.toArray();

		List<Station> nearbySts = new ArrayList<Station>();
		for (int i : nearbyStsID)
			nearbySts.add(sts.getStationById(i));

		return nearbySts;
	}

	@GET
	@Path("/getStatistics")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Statistic> getStatistics() {
		List<Statistic> statistics = new ArrayList<Statistic>();
		for (Statistic s : BicingFreeSlotsNotifier.getStatistics()) {
			if (!(s.getFreq().equals("")) && (s.getTimes().size() > 2))
				statistics.add(s);
		}
		if (statistics.size() > 0)
			return statistics;
		throw new WebApplicationException(204);
	}

	@GET
	@Path("/getStationsSubs/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getStationsSubs(@PathParam("id") int id) {
		Subscriptions subs = BicingFreeSlotsNotifier.getSubs();
		List<User> users = subs.getUsersSubToStationById(id);
		return users;
	}
}