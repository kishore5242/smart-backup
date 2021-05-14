package com.kishore.sb.advice;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import com.kishore.sb.model.Command;
import com.kishore.sb.model.Decision;
import com.kishore.sb.model.Job;
import com.kishore.sb.util.DateUtil;

public class DestinationAdvisor extends Advisor {
	
	String destDir;
	String currMonth = DateUtil.timeStamp("yyyyMMdd");

	public DestinationAdvisor(Command command) {
		super(command);
		destDir = command.getRhs();
	}

	@Override
	public boolean advise(Decision decision) {
		if(command.getOperation().getJob() == Job.BACKUP_COPY || command.getOperation().getJob() == Job.BACKUP_MOVE) {
			String destination = destDir + "/" + currMonth + "/" + decision.getCategory() + "/" + decision.getSource().getName();
			decision.setDestination(new File(destination));
		} else if (command.getOperation().getJob() == Job.DELETE) {
			return true;
		} else {
			String relativeToLhs = getRelativePath(decision.getSource(), command.getLhs());
			decision.setDestination(new File(command.getRhs() + "/" + relativeToLhs));
		}
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
