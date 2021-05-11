package com.kishore.sb.filter;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.kishore.sb.GlobalData;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.CommandStatus;

public class SmartFileFilter implements FileFilter {

	private static final Logger logger = LoggerFactory.getLogger(SmartFileFilter.class);

	private Set<String> extentions = new HashSet<>();

	private Integer totalCount;

	private Integer processedCount;

	private GlobalData data;

	private Command cmd;

	public SmartFileFilter(Command cmd, GlobalData data) {
		this.processedCount = 0;
		this.data = data;
		this.cmd = cmd;
	}

	public Set<String> getExtentions() {
		return extentions;
	}

	public void setExtentions(Set<String> extentions) {
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

		if (CollectionUtils.isEmpty(extentions)) {
			if(file.isFile()) {
				record();
			}
			return true;
		}

		for (String extension : extentions) {
			if (file.getName().toLowerCase().endsWith(extension)) {
				record();
				return true;
			}
		}

		return false;
	}

	private void record() {
		processedCount++;
		logger.info("copying {}/{}", processedCount, totalCount);
		int percentage = (processedCount / totalCount) * 100;
		if (percentage % 10 == 0) {
			data.setCommandInfo(cmd.getId(), CommandStatus.RUNNING, cmd.getOperation().getJobType() + " - " + processedCount + "/" + totalCount);
		}
	}
}