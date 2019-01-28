/*
 *  Notification.java
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

public class Notification {
	// ====================
	// ATTRIBUTES
	// ====================
	private String text;

	// ====================
	// CONSTRUCTORS
	// ====================
	public Notification() {
	}

	public Notification(String text) {
		super();
		this.text = text;
	}
	
	// ====================
	// GETTERS & SETTERS
	// ====================
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
