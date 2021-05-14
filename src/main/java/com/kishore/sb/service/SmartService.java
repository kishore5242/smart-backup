package com.kishore.sb.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kishore.sb.GlobalData;
import com.kishore.sb.advice.ActionAdvisor;
import com.kishore.sb.advice.DestinationAdvisor;
import com.kishore.sb.advice.DuplicateAdvisor;
import com.kishore.sb.advice.MimeTypeAdvisor;
import com.kishore.sb.jpa.SmartStore;
import com.kishore.sb.model.Action;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.CommandStatus;
import com.kishore.sb.model.Decision;
import com.kishore.sb.util.DateUtil;

@Component
public class SmartService {

	private static final Logger logger = LoggerFactory.getLogger(SmartService.class);

	@Autowired
	SmartStore store;
	
	@Autowired
	GlobalData data;
	
	public void runCommand(Command command) {
		try {

			logger.info("Begin operation - {}", command.getOperation().getName());
			data.setCommandInfo(command.getId(), CommandStatus.RUNNING, "Running...");
			
			File source = new File(command.getLhs());
			
			Collection<File> sourceFiles = FileUtils.listFiles(source, null, true);
			Stream<Decision> decisionStream = sourceFiles.stream().map(Decision::new);
			
			ActionAdvisor actionAdvisor = new ActionAdvisor(command);
			MimeTypeAdvisor mimeTypeAdvisor = new MimeTypeAdvisor(command);
			DestinationAdvisor destinationAdvisor = new DestinationAdvisor(command);
			DuplicateAdvisor duplicateAdvisor = new DuplicateAdvisor(command);
			
			Set<Decision> decisions = decisionStream
					.filter(actionAdvisor::advise)
					.filter(mimeTypeAdvisor::advise)
					.filter(destinationAdvisor::advise)
					.filter(duplicateAdvisor::advise)
					.collect(Collectors.toSet());
			
			executeDecisions(command, decisions);

			data.setCommandInfo(command.getId(), CommandStatus.COMPLETED, "Last run - " + DateUtil.timeStamp("dd-MM-yyyy hh:mm a"), 100);
			logger.info("completed operation - {}", command.getOperation().getName());

		} catch (Exception e) {
			logger.error("Command could not be run ", e);
			data.setCommandInfo(command.getId(), CommandStatus.FAILED, "Error " + DateUtil.timeStamp("dd-MM-yyyy hh:mm a") + " - " + e.getMessage());
		} finally {
			store.saveCommand(command);
		}
	}
	
	private void executeDecisions(Command command, Set<Decision> decisions) throws IOException {
		
		int total = decisions.size();
		int processed = 0;
		
		for(Decision decision: decisions) {
			
			if(decision.getAction() == Action.COPY) {
				FileUtils.copyFile(decision.getSource(), decision.getDestination(), true);
				
			} else if(decision.getAction() == Action.MOVE) {
				FileUtils.copyFile(decision.getSource(), decision.getDestination(), true);
				FileUtils.forceDelete(decision.getSource());
				
			} else if(decision.getAction() == Action.DELETE) {
				FileUtils.forceDelete(decision.getSource());
			}

			processed ++;
			int progress = ( processed * 100 ) / total;
			data.setCommandInfo(command.getId(), CommandStatus.RUNNING, command.getOperation().getJob() + " - " + processed + "/" + total, progress);
		}
	}

}
