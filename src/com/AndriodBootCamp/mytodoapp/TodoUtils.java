package com.AndriodBootCamp.mytodoapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class TodoUtils {
	
	 public static Date ConvertToDateTime(String dateTimeString) {
		//need to check and change the date format, iso8601
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    Date convertedDate = null;
	    if (dateTimeString != null){
		    try {
		        convertedDate = dateFormat.parse(dateTimeString);
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
	    }
	    
	    return convertedDate;
	  }
		  
	  //Input HH::MM::SS in today's ToDo list
	  //Return 12-hour 00:00:00 AM/PM format
	  public static String GetDueTimeStringForDisplay(String dueDate){
	      dueDate = dueDate.split("\\s+")[1].trim(); //get the HH::MM::SS
	      String[] dTime = dueDate.split("\\:");
	      int hours = 0 ;
	      try {
	    	  hours = Integer.parseInt(dTime[0]);
	      } catch(NumberFormatException nfe) {
	         System.out.println("Could not parse " + nfe);
	      } 
	      String ampm = hours < 12 ? "AM" : "PM";
	      
	      return (hours%12 + ":" + dTime[1] + " " + ampm);
	  }

}
