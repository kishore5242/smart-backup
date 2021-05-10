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

import com.kishore.sb.GlobalData;
import com.kishore.sb.filter.SmartFileFilter;
import com.kishore.sb.jpa.SmartStore;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.CommandStatus;
import com.kishore.sb.model.Operation;
import com.kishore.sb.util.DateUtil;

@Component
public class SmartService {

	private static final Logger logger = LoggerFactory.getLogger(SmartService.class);

	@Autowired
	FileService fileService;

	@Autowired
	SmartStore store;
	
	@Autowired
	GlobalData data;
	
	public void runCommand(Command command) {
		try {

			logger.info("Begin operation - {}", command.getOperation().getName());
			data.setCommandInfo(command.getId(), CommandStatus.RUNNING, "Running...");

			Operation op = command.getOperation();

			if (op.getJobType().equals(JOB_TYPE_COPY)) {

				SmartFileFilter filter = new SmartFileFilter(command, data);

				if (op.getFileType().equals(FILE_TYPE_IMAGES)) {
					filter.setExtentions(EXTENTIONS_IMAGES);
				} else if (op.getFileType().equals(FILE_TYPE_VIDEOS)) {
					filter.setExtentions(EXTENTIONS_VIDEOS);
				} else if (op.getFileType().equals(FILE_TYPE_DOCUMENTS)) {
					filter.setExtentions(EXTENTIONS_DOCUMENTS);
				}

				fileService.copy(command.getLhs(), command.getRhs(), filter);

			} else if (op.getJobType().equals(JOB_TYPE_MOVE)) {
				// TODO

			} else if (op.getJobType().equals(JOB_TYPE_BACKUP)) {
				// TODO

			} else if (op.getJobType().equals(JOB_TYPE_DELETE)) {
				// TODO

			} else {
				logger.info("Unknown job! {}", op.getJobType());
				data.setCommandInfo(command.getId(), CommandStatus.FAILED, "Unknown job!");
				return;
			}

			data.setCommandInfo(command.getId(), CommandStatus.COMPLETED, "Last run - " + DateUtil.timeStamp());
			logger.info("completed operation - {}", command.getOperation().getName());

		} catch (Exception e) {
			logger.error("Command could not be run ", e);
			data.setCommandInfo(command.getId(), CommandStatus.FAILED, "Error " + DateUtil.timeStamp() + " - " + e.getMessage());
		} finally {
			store.saveCommand(command);
		}
	}

}
