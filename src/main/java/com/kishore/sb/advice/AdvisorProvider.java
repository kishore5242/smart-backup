package com.kishore.sb.advice;

import java.util.Set;

import com.kishore.sb.model.Command;
import com.kishore.sb.model.Job;

public interface AdvisorProvider {

	Set<Job> appliesTo();
	
	Advisor getAdvisor(Command command);

}
