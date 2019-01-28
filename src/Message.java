/*
 *  Message.java
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

public class Message {
	
	// ====================
	// ATTRIBUTES
	// ====================
	private long chat_id;
	private String text;
	private String parse_mode = "Markdown";
	
	// ====================
	// CONSTRUCTORS
	// ====================
	public Message() {
	}
	
	public Message(long chat_id, String text) {
		this.chat_id = chat_id;
		this.text = text;
	}

	// ====================
	// GETTERS & SETTERS
	// ====================
	public long getChat_id() {
		return chat_id;
	}

	public void setChat_id(long chat_id) {
		this.chat_id = chat_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getParse_mode() {
		return parse_mode;
	}

	public void setParse_mode(String parse_mode) {
		this.parse_mode = parse_mode;
	}
	

}