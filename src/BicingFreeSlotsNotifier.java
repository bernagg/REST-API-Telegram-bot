
/*
 *  BicingFreeSlotsNotifier.java
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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.sun.net.httpserver.HttpServer;

public class BicingFreeSlotsNotifier {
	// ====================
	// ATTRIBUTES
	// ====================
	private static List<Station> stations;
	private static List<User> users;
	private static Subscriptions subs;
	private static Long lastAccess;
	private static List<Statistic> statistics;

	/**
	 * Main Class of the system.
	 * 
	 * Contains a local REST API server. Also different Jobs like TelegramJob,
	 * JsonReader and TwitterPublisher.
	 * 
	 * JsonReader Job, will be executed every 20 seconds. TelegramSender Job,
	 * always listening. TwitterPublisher, sends a message to a Twitter account
	 * every 300 seconds.
	 * 
	 */
	public static void main(String[] args) throws SchedulerException {
		stations = new ArrayList<Station>();
		users = new ArrayList<User>();
		subs = new Subscriptions();
		statistics = new ArrayList<Statistic>();
		lastAccess = System.currentTimeMillis();

		URI baseUri = UriBuilder.fromUri("http://localhost/").port(15000).build();
		ResourceConfig config = new ResourceConfig(StationsServices.class, UserServices.class);
		HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
		System.out.println("Server started...");

		// CALL TO JSONREADER JOB EVERY 20 SECONDS
		JobDetail job = JobBuilder.newJob(JsonReader.class).withIdentity("JsonReaderJob").build();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(20).repeatForever()).build();
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.start();
		sched.scheduleJob(job, trigger);

		// CALL TO TWITTER PUBLISHER JOB EVERY 300 SECONDS
		JobDetail TwitterJob = JobBuilder.newJob(TwitterPublisher.class).withIdentity("TwitterPublisherJob").build();
		Trigger TwitterTrigger = TriggerBuilder.newTrigger()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(300).repeatForever())
				.build();
		SchedulerFactory sfTwitter = new StdSchedulerFactory();
		Scheduler schedTwitter = sfTwitter.getScheduler();
		schedTwitter.start();
		schedTwitter.scheduleJob(TwitterJob, TwitterTrigger);

		// CALL TO TELEGRAM BOT
		JobDetail TelegramJob = JobBuilder.newJob(TelegramSender.class).withIdentity("TelegramSenderJob").build();
		Trigger TelegramTrigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.simpleSchedule())
				.build();
		SchedulerFactory sfTelegram = new StdSchedulerFactory();
		Scheduler schedTelegram = sfTelegram.getScheduler();
		schedTelegram.start();
		schedTelegram.scheduleJob(TelegramJob, TelegramTrigger);

	}

	// ====================
	// PUBLIC METHODS
	// ====================

	/**
	 * This method puts a new Subscriber to the list [Subscriptions subs]
	 * 
	 * @param s,
	 *            a Station.
	 * @param u,
	 *            an User.
	 */
	public static void addSubscription(Station s, User u) {
		subs.addSubscriptor(s.getId(), u);
	}

	/**
	 * Adds a new User, keeping in mind if the user exists in the list [List <User> users]
	 * 
	 * @param user, an User.
	 * 
	 * @return a boolean, true means success, false otherwise.
	 */
	public static boolean addUser(User user) {
		if (!users.contains(user)) {
			return users.add(user);
		} else {
			for (User u : users) {
				if (u.equals(user)) {
					User newOne = new User(u.getName(), user.getPhoneNumber(), user.getTelegramToken(), u.getStations());
					for (Integer numsU : user.getStations()) {
						boolean repeated = false;
						
						for(int j = 0; j < u.getStations().size(); j++) {
							if(u.getStations().get(j) == numsU) {
								repeated = true;
								break;
							}
						}
						
						if(!repeated)
							newOne.addStation(numsU);
					}
					BicingFreeSlotsNotifier.deleteUser(u.getPhoneNumber());
					return BicingFreeSlotsNotifier.users.add(newOne);
				}
			}
		}
		return false;
	}

	/**
	 * This method puts to the list [List<Station> stations] every time the
	 * JsonReader gets a refreshed Stations list.
	 * 
	 * Inside this method, we refresh the variable [Long lastAccess] if there is
	 * an update on the bikes/slots list after comparing each station with the
	 * more recent.
	 * 
	 * Also, we add/update a new Statistic object to the list [List
	 * <Statistic> statistics]
	 * 
	 * @param stations
	 *            a List of Stations.
	 */
	static public void setStations(List<Station> stations) {
		if (BicingFreeSlotsNotifier.getStations().size() == 0) {
			BicingFreeSlotsNotifier.setLastAccess(System.currentTimeMillis());
			BicingFreeSlotsNotifier.stations = stations;
		} else {
			// This loop could have a java.util.ConcurrentModificationException, thats why 
			// we iterate it in the traditional way.
			for (int i = 0; i < BicingFreeSlotsNotifier.stations.size(); i++) {
				Station s = BicingFreeSlotsNotifier.stations.get(i);
				for (Station ss : stations) {
					if (s.equals(ss)) {
						if (s.getBikes() != ss.getBikes() || s.getSlots() != ss.getSlots()) {
							Long timeActual = System.currentTimeMillis();
							Long timeDiff = timeActual - BicingFreeSlotsNotifier.lastAccess;
							if (BicingFreeSlotsNotifier.existsStatistic(ss)) {
								Statistic statistic = getStatisticByStationId(ss.getId());
								statistic.addTime(timeDiff);
								Long sumTimes = 0L;
								for (Long l : statistic.getTimes()) {
									sumTimes += l;
								}
								sumTimes = sumTimes / statistic.getTimes().size();
								String dateStamp = String.format("%02d min, %02d sec",
										TimeUnit.MILLISECONDS.toMinutes(sumTimes),
										TimeUnit.MILLISECONDS.toSeconds(sumTimes) - TimeUnit.MINUTES
												.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sumTimes)));
								statistic.setFreq(dateStamp);
								BicingFreeSlotsNotifier.statistics.remove(statistic);
								BicingFreeSlotsNotifier.statistics.add(statistic);
								BicingFreeSlotsNotifier.setStation(ss);
							} else {
								Statistic statistic = new Statistic(s);
								BicingFreeSlotsNotifier.statistics.add(statistic);
							}
						}
					}
				}
			}
		}

	}

	/**
	 * We search an User Object by the String @param name.
	 * 
	 * @param name,
	 *            a String.
	 * @return an User.
	 */
	public static User getUserByName(String name) {
		for (User u : users)
			if (u.getName().equals(name))
				return u;
		return null;
	}

	/**
	 * We erase an User by his attribute [PhoneNumber] using @param phoneNumber.
	 * 
	 * @param phoneNumber,
	 *            a String.
	 * @return a boolean. True if the user was deleted with success. False
	 *         otherwise.
	 */
	public static boolean deleteUser(String phoneNumber) {
		for (User u : users) {
			if (u.getPhoneNumber().equals(phoneNumber)) {
				users.remove(u);
				return true;
			}
		}
		return false;
	}

	/**
	 * This method checks if a Statistic exists using an Station Object.
	 * 
	 * @param s,
	 *            a Station object.
	 * 
	 * @return a boolean. True if the @param s exists in the list [List<Statistic>
	 *         statistics], false otherwise.
	 */
	public static boolean existsStatistic(Station s) {
		for (Statistic st : BicingFreeSlotsNotifier.statistics) {
			if (st.getS().getId() == s.getId())
				return true;
		}
		return false;
	}

	/**
	 * This method returns a Statistic looking by @param id.
	 * 
	 * @param id,
	 *            an Integer.
	 * @return a Statistic Object or null if no coincidence.
	 */
	public static Statistic getStatisticByStationId(int id) {
		for (Statistic s : BicingFreeSlotsNotifier.statistics) {
			if (s.getS().getId() == id)
				return s;

		}
		return null;
	}

	// ====================
	// GETTERS & SETTERS
	// ====================
	public static Subscriptions getSubs() {
		return subs;
	}

	public static void setSubs(Subscriptions subs) {
		BicingFreeSlotsNotifier.subs = subs;
	}

	public static void setUsers(List<User> users) {
		BicingFreeSlotsNotifier.users = users;
	}

	static public List<Station> getStations() {
		return stations;
	}

	static public List<User> getUsers() {
		return users;
	}

	public static void addStation(Station station) {
		System.out.println(station.toString());
		System.out.println(stations.add(station));
	}

	public static Long getLastAccess() {
		return lastAccess;
	}

	public static void setLastAccess(Long lastAccess) {
		BicingFreeSlotsNotifier.lastAccess = lastAccess;
	}

	public static List<Statistic> getStatistics() {
		return statistics;
	}

	public static void setStatistics(List<Statistic> statistics) {
		BicingFreeSlotsNotifier.statistics = statistics;
	}

	public static Station setStation(Station st) {
		List<Station> sts = BicingFreeSlotsNotifier.getStations();
		for (Station station : sts) {
			if (station.getId() == st.getId()) {
				BicingFreeSlotsNotifier.stations.remove(station);
				BicingFreeSlotsNotifier.stations.add(st);
				return st;
			}
		}
		BicingFreeSlotsNotifier.stations.add(st);
		return st;
	}

}
