package com.kishore.sb.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kishore.sb.filter.SmartFileFilter;
import com.kishore.sb.jpa.SmartStore;

@Service
public class FileService {
	
	@Autowired
	SmartStore store;

	public void copyAll(String sourceDir, String DestDir, SmartFileFilter filter) throws IOException {
		
		File source = new File(sourceDir);
		File destination = new File(DestDir);
		
		filter.setExtentions(null);
		Integer size = countFiles(source, null);
		filter.setTotalCount(size);
		filter.setProcessedCount(0);
		
		
		FileUtils.copyDirectory(source, destination, filter);
	}
	
	public void copyImages(String sourceDir, String DestDir, SmartFileFilter filter) throws IOException {
		
		File source = new File(sourceDir);
		File destination = new File(DestDir);
		
		filter.setExtentions(new String[] { "jpg", "jpeg", "png", "gif"});
		Integer size = countFiles(source, filter.getExtentions());
		filter.setTotalCount(size);
		filter.setProcessedCount(0);
		
		
		FileUtils.copyDirectory(source, destination, filter);
	}

	private Integer countFiles(File source, String[] extentions) {
		Iterator<File> allImages = FileUtils.iterateFiles(source, extentions, true);
		Integer size = 0;
		while(allImages.hasNext()) {
			allImages.next();
			size ++;
		}
		return size;
	}

	public List<String> getAllLines(File file) throws IOException {
		return FileUtils.readLines(file, StandardCharsets.UTF_8);
	}

}
