/*
 *  Utils.java
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

import java.util.Arrays;

public class Utils {
	
	// ====================
	// PUBLIC METHODS
	// ====================
	
	/**
	 * This method, given a [String] array and a starting index number, checks if all members of the input array are digits.
	 * 
	 * @param array, a [String] array.
	 * @param index, a [Integer] Object which is the starting point from where the function will start checking.
	 * 
	 * @return a [Boolean], true if all member of the array since the starting position are digits, false otherwise.
	 */
	public static Boolean allMembersAreDigits(String[] array, int index) {
		String[] newArray = Arrays.copyOfRange(array, index, array.length);
		
		for(String s : newArray) {
		    try{
		        Integer.parseInt(s);
		    }catch(NumberFormatException e){
		        return false;
		    }
		}
		
		return true;
	}
	
}
