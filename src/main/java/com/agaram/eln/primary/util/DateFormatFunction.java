package com.agaram.eln.primary.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This class holds method to format date objects.
 * @author ATE153
 * @version 1.0.0
 * @since   22- Jan- 2020
 *
 */
public class DateFormatFunction {

	 /**
     * This method is used to format the string date based on specified time zone
     * and locale.
     * @param stringDate [String] string date to format
     * @param localeCode [String] locale to convert
     * @param timeZone [String] TimeZone to convert
     * @return timezone formatted string date
     */
    public String zoneFormattedDateString(final String stringDate,
    		final String localeCode, final String timeZone)
    {
    	DateFormat parseFormat = null;
    	if (stringDate.endsWith("Z"))
    	{
    		parseFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss'Z'"); 
    	}
    	else
    	{
    		parseFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss"); 
    	}
    	
    	Date dt = null;
    	try {
    	  dt = parseFormat.parse(stringDate);
    	}catch (ParseException e) {
    	  e.printStackTrace();
    	} 

    	final DateFormat returnFormat =  SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, 
				SimpleDateFormat.LONG, new Locale(localeCode));
    	returnFormat.setTimeZone(TimeZone.getTimeZone(timeZone)); 
    	final String newDateString = returnFormat.format(dt);
    	return newDateString;
    }
}
