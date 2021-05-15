package com.kishore.sb.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kishore.sb.GlobalData;
import com.kishore.sb.advice.Advisor;
import com.kishore.sb.advice.AdvisorProvider;
import com.kishore.sb.jpa.SmartStore;
import com.kishore.sb.model.Action;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.CommandStatus;
import com.kishore.sb.model.Decision;
import com.kishore.sb.util.AdvisorUtil;
import com.kishore.sb.util.DateUtil;

@Component
public class SmartService {

	private static final Logger logger = LoggerFactory.getLogger(SmartService.class);
	
	private final Map<Integer, CompletableFuture<String>> runningCommands = new ConcurrentHashMap<>();
	
	@Autowired
	Collection<AdvisorProvider> advisorProviders;

	@Autowired
	SmartStore store;
	
	@Autowired
	GlobalData data;
	
	public void run(Command command) {
		
		CompletableFuture<String> future = new CompletableFuture<String>();
		CompletableFuture<String> existing = runningCommands.putIfAbsent(command.getId(), future);
		if(existing != null) {
			logger.info("Command {} already running", command.getId());
			data.setCommandInfo(command.getId(), CommandStatus.RUNNING, "Command already running!");
		} else {
			CompletableFuture.runAsync(() -> runSync(command, future));
		}
	}
	
	public void cancel(Command command) {
		CompletableFuture<String> existing = runningCommands.get(command.getId());
		if(existing != null) {
			logger.info("Cancelling command {} ...", command.getId());
			existing.cancel(true);
			runningCommands.remove(command.getId());		
		}
	}
	
	private void runSync(Command command, CompletableFuture<String> future) {
		try {
			logger.info("Begin operation - {}", command.getOperation().getName());
			data.setCommandInfo(command.getId(), CommandStatus.RUNNING, "Running...");
			
			File source = new File(command.getLhs());
			
			Collection<File> sourceFiles = FileUtils.listFiles(source, null, true);
			Stream<Decision> decisionStream = sourceFiles.stream().map(Decision::new);
			
			Set<Advisor> advisors = advisorProviders.stream()
					.filter(provider -> provider.appliesTo().contains(command.getOperation().getJob()))
					.map(provider -> provider.getAdvisor(command))
					.collect(Collectors.toSet());
			
			Advisor advisor = AdvisorUtil.combine(advisors);
			
			Set<Decision> decisions = decisionStream
					.filter(advisor::advise)
					.collect(Collectors.toSet());
			
			executeDecisions(command, decisions, future);

		} catch (Exception e) {
			logger.error("Command could not be run ", e);
			data.setCommandInfo(command.getId(), CommandStatus.FAILED, "Error " + DateUtil.timeStamp("dd-MM-yyyy hh:mm a") + " - " + e.getMessage());
			future.completeExceptionally(e);
		} finally {
			store.saveCommand(command);
			runningCommands.remove(command.getId());
		}
	}
	
	private void executeDecisions(Command command, Set<Decision> decisions, CompletableFuture<String> future) throws IOException {
		
		int total = decisions.size();
		int processed = 0;
		
		for(Decision decision: decisions) {
			
			if(future.isCancelled()) {
				break;
			}
			
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
			String comment = progress + "% - " + decision.getAction() + " - " + decision.getSource().getAbsolutePath() + " ---> " + decision.getDestination();
			data.setCommandInfo(command.getId(), CommandStatus.RUNNING, comment, progress);
			logger.info(comment);
		}
		
		String comment = "";
		if(future.isCancelled()) {
			comment = "Command cancelled after processing " + processed + " files";
			data.setCommandInfo(command.getId(), CommandStatus.CANCELLED, comment, 100);
		} else {
			comment = "Completed! Last run - " + DateUtil.timeStamp("dd-MM-yyyy hh:mm a");
			data.setCommandInfo(command.getId(), CommandStatus.COMPLETED, comment, 100);
		}
		logger.info(comment);
	}

}
