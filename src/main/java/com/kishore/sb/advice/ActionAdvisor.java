package com.kishore.sb.advice;

import com.kishore.sb.model.Action;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.Decision;
import com.kishore.sb.model.Job;

public class ActionAdvisor extends Advisor {
	
	public ActionAdvisor(Command command) {
		super(command);
	}

	@Override
	public boolean advise(Decision decision) {
		
		Job job = command.getOperation().getJob();
		if(job == Job.COPY || job == Job.BACKUP_COPY ) {
			decision.setAction(Action.COPY);
			return true;
			
		} else if (job == Job.MOVE) {
			decision.setAction(Action.MOVE);
			return true;
			
		} else if (job == Job.DELETE) {
			decision.setAction(Action.DELETE);
			return true;
		}
		
		return false;
	}

}
