package com.kishore.sb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.kishore.sb.jpa.SmartStore;
import com.kishore.sb.model.Operation;
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
    	//logger.info("loading operations...");
    	//loadOperations();
    	
    	//TODO logger.info("checking last run status...");
    }
	
	private void loadOperations() {
//		Operation copyOp = new Operation();
//		copyOp.setId(100);
//		copyOp.setName("Copy all");
//		
//		Operation copyImgOp = new Operation();
//		copyImgOp.setId(200);
//		copyImgOp.setName("Copy images");
//		
//		Operation backupDeviceOp = new Operation();
//		backupDeviceOp.setId(300);
//		backupDeviceOp.setName("Backup device");
//		
//		store.saveOperation(copyOp);
//		store.saveOperation(copyImgOp);
	}
	
	
}