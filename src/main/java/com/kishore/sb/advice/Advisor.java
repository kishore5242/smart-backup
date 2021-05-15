package com.kishore.sb.advice;

import com.kishore.sb.model.Decision;

public interface Advisor {
	
	public int order();
	
	public boolean advise(Decision decision);

}
