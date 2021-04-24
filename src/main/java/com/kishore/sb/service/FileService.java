package com.kishore.sb.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.kishore.sb.filter.ImageFileFilter;
import com.kishore.sb.model.Command;

@Service
public class FileService {

	public void copyAll(File sourceDir, File DestDir) throws IOException {
		FileUtils.copyDirectory(sourceDir, DestDir);
	}
	
	public void copyImages(File sourceDir, File DestDir) throws IOException {
		FileUtils.copyDirectory(sourceDir, DestDir, new ImageFileFilter());
	}

	public List<String> getAllLines(File file) throws IOException {
		return FileUtils.readLines(file, StandardCharsets.UTF_8);
	}

}
