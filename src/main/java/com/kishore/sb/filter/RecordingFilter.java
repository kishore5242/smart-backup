package com.kishore.sb.filter;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kishore.sb.GlobalData;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.CommandStatus;

public class RecordingFilter implements IOFileFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(RecordingFilter.class);
	
	private Integer totalCount = 0;
	private Integer processedCount = 0;
	private IOFileFilter filter;
	private Command command;
	private GlobalData data;

	public RecordingFilter(File source, IOFileFilter filter, Command command, GlobalData data) {
		this.filter = filter;
		countFiles(filter, source);
		this.command = command;
		this.data = data;
	}

	@Override
	public boolean accept(File file) {
		if(filter.accept(file)) {
			if(file.isFile()) {
				record();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean accept(File dir, String name) {
		if(filter.accept(dir, name)) {
			return true;
		}
		return false;
	}
	
    private void countFiles(FileFilter filter, final File directory) {
        for (final File f : directory.listFiles()) {
            if (f.isDirectory()) {
            	countFiles(filter, f);
            }
            if (f.isFile() && filter.accept(f)) {
            	this.totalCount++;
            }
        }
    }
    
	private void record() {
		processedCount++;
		int percentage = (processedCount / totalCount) * 100;
		if (percentage % 10 == 0) {
			String comment = command.getOperation().getJobType() + " - " + processedCount + "/" + totalCount;
			data.setCommandInfo(command.getId(), CommandStatus.RUNNING, comment);
			logger.info(comment);
		}
	}

}
