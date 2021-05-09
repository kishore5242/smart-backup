package com.kishore.sb.service;

import static com.kishore.sb.model.OperationData.EXTENTIONS_DOCUMENTS;
import static com.kishore.sb.model.OperationData.EXTENTIONS_IMAGES;
import static com.kishore.sb.model.OperationData.EXTENTIONS_VIDEOS;
import static com.kishore.sb.model.OperationData.FILE_TYPE_DOCUMENTS;
import static com.kishore.sb.model.OperationData.FILE_TYPE_IMAGES;
import static com.kishore.sb.model.OperationData.FILE_TYPE_VIDEOS;
import static com.kishore.sb.model.OperationData.JOB_TYPE_BACKUP;
import static com.kishore.sb.model.OperationData.JOB_TYPE_COPY;
import static com.kishore.sb.model.OperationData.JOB_TYPE_DELETE;
import static com.kishore.sb.model.OperationData.JOB_TYPE_MOVE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kishore.sb.filter.SmartFileFilter;
import com.kishore.sb.jpa.SmartStore;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.Operation;
import com.kishore.sb.util.DateUtil;

@Component
public class SmartService {

	private static final Logger logger = LoggerFactory.getLogger(SmartService.class);

	@Autowired
	FileService fileService;

	@Autowired
	SmartStore store;

	public void runCommand(Command cmd) {
		try {
			
			logger.info("Begin operation - {}", cmd.getOperation().getName());
			
			Operation op = cmd.getOperation();
			
			
			if (op.getJobType().equals(JOB_TYPE_COPY)) {
				
				SmartFileFilter filter = new SmartFileFilter(store, cmd);
				
				if(op.getFileType().equals(FILE_TYPE_IMAGES)) {
					filter.setExtentions(EXTENTIONS_IMAGES);
				} 
				else if(op.getFileType().equals(FILE_TYPE_VIDEOS)) {
					filter.setExtentions(EXTENTIONS_VIDEOS);
				} 
				else if(op.getFileType().equals(FILE_TYPE_DOCUMENTS)) {
					filter.setExtentions(EXTENTIONS_DOCUMENTS);
				} 
								
				fileService.copy(cmd.getLhs(), cmd.getRhs(), filter);
				
			} else if (op.getJobType().equals(JOB_TYPE_MOVE)) {
				// TODO
				
			} else if (op.getJobType().equals(JOB_TYPE_BACKUP)) {
				// TODO
				
			} else if (op.getJobType().equals(JOB_TYPE_DELETE)) {
				// TODO
				
			} else {
				logger.info("Unknown command! {}", cmd.getComment());
			}
			
			logger.info("completed operation - {}", cmd.getOperation().getName());
			
		} catch (Exception e) {
			logger.error("Command could not be run ", e);
		} finally {
			cmd.setStatus("Completed " + DateUtil.timeStamp());
			store.saveCommand(cmd);
		}
	}
	
}
