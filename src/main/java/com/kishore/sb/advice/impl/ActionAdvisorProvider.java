package com.kishore.sb.advice.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.kishore.sb.advice.Advisor;
import com.kishore.sb.advice.AdvisorProvider;
import com.kishore.sb.model.Action;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.Decision;
import com.kishore.sb.model.Job;

@Component
public class ActionAdvisorProvider implements AdvisorProvider {

	@Override
	public Set<Job> appliesTo() {
		return new HashSet<>(Arrays.asList(Job.values()));
	}

	@Override
	public Advisor getAdvisor(Command command) {
		return new ActionAdvisor(command);
	}

	private class ActionAdvisor implements Advisor {
		
		private Command command;

		public ActionAdvisor(Command command) {
			this.command = command;
		}
		
		@Override
		public int order() {
			return 10;
		}

		@Override
		public boolean advise(Decision decision) {

			Job job = command.getOperation().getJob();
			if (job == Job.COPY || job == Job.BACKUP) {
				decision.setAction(Action.COPY);
				if(command.getOperation().getDeleteFromSource()) {
					decision.setAction(Action.MOVE);
				}
				return true;

			} else if (job == Job.DELETE) {
				decision.setAction(Action.DELETE);
				return true;
			}

			return false;
		}

	}

}
