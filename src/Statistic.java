/*
 *  Statistic.java
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

public class Statistic {
	 // ====================
    // ATTRIBUTES
    // ====================
	private Station s;
	private ArrayList<Long> times;
	private String freq;

	// ====================
    // CONSTRUCTORS
    // ====================
	public Statistic() {
	}
	
	public Statistic(Station s) {
		this.s = s;
		this.times = new ArrayList<Long>();
		this.freq = "";
	}

	// ====================
	// GETTERS & SETTERS
	// ====================
	public Station getS() {
		return s;
	}

	public void setS(Station s) {
		this.s = s;
	}

	public ArrayList<Long> getTimes() {
		return times;
	}

	public void setTimes(ArrayList<Long> times) {
		this.times = times;
	}

	public void addTime(Long time) {
		this.times.add(time);
	}

	

	@Override
	public String toString() {
		return "Statistic [s=" + s + ", times=" + times + ", freq=" + freq + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((s == null) ? 0 : s.hashCode());
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
		Statistic other = (Statistic) obj;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		return true;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	

}
