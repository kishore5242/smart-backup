package com.kishore.sb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.kishore.sb.jpa.SmartStore;
import com.kishore.sb.model.Operation;
import com.kishore.sb.model.Task;
import com.kishore.sb.service.SmartService;

/**
 * Created by JavaDeveloperZone on 27-07-2017.
 */
@Component
@Order(value = 1)
public class TasksRunner implements ApplicationRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(TasksRunner.class);
	
	@Autowired
	SmartService smartService;
	
	@Autowired
	SmartStore store;
	
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
    	
//    	File taskDirectory = new File("tasks");
//    	logger.info("Getting tasks...");
//    	
//    	List<Task> tasks = smartService.getTasks(taskDirectory);
//    	logger.info("Found {} tasks", tasks.size());
//    	
//    	for(Task task: tasks) {
//    		logger.info("Running task {}", task.getName());
//    		smartService.runTask(task);
//    	}
    	
    	logger.info("Init data...");
    	initData();
    	
    }
	
	private void initData() {
		Operation copyOp = new Operation();
		copyOp.setId(100);
		copyOp.setName("Copy all");
		copyOp.setOrganize(false);
		copyOp.setExtensions("all");
		
		Operation copyImgOp = new Operation();
		copyImgOp.setId(200);
		copyImgOp.setName("Copy images");
		copyImgOp.setOrganize(false);
		copyImgOp.setExtensions("jpg,jpeg,png,gif");
		
		store.saveOperation(copyOp);
		store.saveOperation(copyImgOp);
	}
}