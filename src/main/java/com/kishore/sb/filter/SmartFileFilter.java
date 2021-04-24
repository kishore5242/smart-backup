package com.kishore.sb.filter;

import java.io.File;
import java.io.FileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kishore.sb.controller.CommandController;
import com.kishore.sb.jpa.SmartStore;
import com.kishore.sb.model.Command;

public class SmartFileFilter implements FileFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(SmartFileFilter.class);
	
    private String[] extentions;

    private Integer totalCount;
    
    private Integer processedCount;
    
    private SmartStore store;
    
    private Command cmd;
    
    public SmartFileFilter(SmartStore store, Command cmd) {
		this.processedCount = 0;
		this.store = store;
		this.cmd = cmd;
	}

	public String[] getExtentions() {
		return extentions;
	}

	public void setExtentions(String[] extentions) {
		this.extentions = extentions;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getProcessedCount() {
		return processedCount;
	}

	public void setProcessedCount(Integer processedCount) {
		this.processedCount = processedCount;
	}

	public boolean accept(File file) {
        for (String extension : extentions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
            	processedCount ++;
            	logger.info("copying {}/{}", processedCount, totalCount);
            	int percentage = (processedCount / totalCount ) * 100;
            	if(percentage % 10 == 0) {
            		cmd.setStatus(processedCount + "/" + totalCount);
            		store.saveCommand(cmd);
            	}
                return true;
            }
        }
        return false;
    }
}