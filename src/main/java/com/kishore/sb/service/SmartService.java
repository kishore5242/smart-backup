package com.kishore.sb.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kishore.sb.TasksRunner;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.Task;

@Component
public class SmartService {

	private static final Logger logger = LoggerFactory.getLogger(SmartService.class);

	@Autowired
	FileService fileService;

	public List<Task> getTasks(File taskDirectory) throws IOException {
		Collection<File> taskFiles = FileUtils.listFiles(taskDirectory, null, false);
		List<Task> tasks = new ArrayList<>();
		for (File taskFile : taskFiles) {
			tasks.add(getTask(taskFile));
		}
		return tasks;
	}

	public void runTask(Task task) throws IOException {
		for (Command cmd : task.getCommands()) {
			runCommand(cmd);
		}
	}

	private void runCommand(Command cmd) throws IOException {
		if (cmd.getOperation().equals("1")) {
			logger.info(cmd.getComment());
			fileService.copyAll(cmd.getLhs(), cmd.getRhs());
			logger.info("Done.");
		} else if (cmd.getOperation().equals("2")) {
			logger.info(cmd.getComment());
			fileService.copyImages(cmd.getLhs(), cmd.getRhs());
			logger.info("Done.");
		} else if (cmd.getOperation().equals("#")) {
			logger.info(cmd.getComment());
		} else {
			logger.info("Unknown command! {}", cmd.getComment());
		}
	}

	private Task getTask(File taskFile) throws IOException {
		List<Command> commands = new ArrayList<>();
		List<String> lines = fileService.getAllLines(taskFile);

		lines.forEach(line -> {
			commands.add(getCommand(line));
		});
		return new Task(taskFile.getName(), commands);
	}

	private Command getCommand(String line) {

		line = line.trim();

		// check if print command
		if (line.startsWith("#")) {
			return new Command(null, "#", null, line);
		}

		// other command
		String[] tokens = line.split(">");
		File lhs = new File(tokens[0].trim());
		String op = tokens[1].trim();
		File rhs = new File(tokens[2].trim());
		return new Command(lhs, op, rhs, line);
	}

}
