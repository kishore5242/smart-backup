package com.kishore.sb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {
	
	private DateUtil() {}
	
	public static String timeStamp() {
	    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");  
	    Date date = new Date();  
	    return formatter.format(date);  
	}

}
