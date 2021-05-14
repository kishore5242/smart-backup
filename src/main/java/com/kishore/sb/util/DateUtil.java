package com.kishore.sb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {
	
	private DateUtil() {}
	
	public static String timeStamp(String format) {
		//"dd-MM-yyyy hh:mm a"
	    SimpleDateFormat formatter = new SimpleDateFormat(format);  
	    Date date = new Date();  
	    return formatter.format(date);  
	}

}
