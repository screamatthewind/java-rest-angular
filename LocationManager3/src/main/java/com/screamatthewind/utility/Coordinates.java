package com.screamatthewind.utility;

public class Coordinates {

	public static String getFormattedLocationInDegree(double latitude, double longitude) {
	    try {
	        int latSeconds = (int) Math.round(latitude * 3600);
	        int latDegrees = latSeconds / 3600;
	        latSeconds = Math.abs(latSeconds % 3600);
	        int latMinutes = latSeconds / 60;
	        latSeconds %= 60;

	        int longSeconds = (int) Math.round(longitude * 3600);
	        int longDegrees = longSeconds / 3600;
	        longSeconds = Math.abs(longSeconds % 3600);
	        int longMinutes = longSeconds / 60;
	        longSeconds %= 60;
	        String latDegree = latDegrees >= 0 ? "N" : "S";
	        String lonDegrees = longDegrees >= 0 ? "E" : "W";

	        return  Math.abs(latDegrees) + "o" + latMinutes + "'" + latSeconds
	                + "\"" + latDegree +" "+ Math.abs(longDegrees) + "o" + longMinutes
	                + "'" + longSeconds + "\"" + lonDegrees;
	    } catch (Exception e) {
	        return ""+ String.format("%8.5f", latitude) + "  "
	                + String.format("%8.5f", longitude) ;
	    }
	}
	
}
