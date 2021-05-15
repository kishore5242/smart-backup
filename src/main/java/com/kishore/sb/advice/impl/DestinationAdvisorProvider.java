package com.kishore.sb.advice.impl;

import static com.kishore.sb.model.Job.BACKUP;
import static com.kishore.sb.model.Job.COPY;

import java.io.File;
import java.net.URI;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.kishore.sb.advice.Advisor;
import com.kishore.sb.advice.AdvisorProvider;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.Decision;
import com.kishore.sb.model.Job;
import com.kishore.sb.util.DateUtil;

@Component
public class DestinationAdvisorProvider implements AdvisorProvider {

	@Override
	public Set<Job> appliesTo() {
		return Set.of(COPY, BACKUP);
	}

	@Override
	public Advisor getAdvisor(Command command) {
		return new DestinationAdvisor(command);
	}
	
	private class DestinationAdvisor implements Advisor {
		
		private Command command;
		private String destDir;
		private String currMonth;

		public DestinationAdvisor(Command command) {
			this.command = command;
			this.destDir = command.getRhs();
			this.currMonth = DateUtil.timeStamp("yyyyMMdd");
		}
		
		@Override
		public int order() {
			return 30;
		}

		@Override
		public boolean advise(Decision decision) {
			String destination;
			if(command.getOperation().getJob() == BACKUP) {
				destination = destDir + "/" + currMonth + "/" + decision.getCategory() + "/" + decision.getSource().getName();
			} else {
				destination = command.getRhs() + "/" + getRelativePath(decision.getSource(), command.getLhs());
			}
			decision.setDestination(new File(destination));
			return true;
		}
		
		private String getRelativePath(File file, String relativeTo) {
			URI fileUri = file.toURI();
			URI pathUri = new File(relativeTo).toURI();

			// create a relative path from the two paths
			URI relativePath = pathUri.relativize(fileUri);
			return relativePath.getPath();
		}

	}

}
