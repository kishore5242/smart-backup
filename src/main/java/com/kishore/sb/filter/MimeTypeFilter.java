package com.kishore.sb.filter;

import java.io.File;
import java.net.URLConnection;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

public class MimeTypeFilter implements IOFileFilter {

	private final boolean image;
	private final boolean video;
	private final boolean document;
	private final boolean audio;

	private final Set<String> docExtentions = Set.of("pdf", "doc", "docx", "odt", "ods", "xls", "xlsx", "ppt", "pptx",
			"txt");

	public MimeTypeFilter(boolean image, boolean video, boolean document, boolean audio) {
		this.image = image;
		this.video = video;
		this.document = document;
		this.audio = audio;
	}

	@Override
	public boolean accept(File pathname) {
		if(pathname.isDirectory()) {
			return true;
		}
		return filterByName(pathname.getName());
	}

	@Override
	public boolean accept(File dir, String name) {
		// TODO does the depend on where the file exists?
		return filterByName(name);
	}
	
	private boolean filterByName(String fileName) {
		String type = getMimeType(fileName);
		if(type == null) {
			return false;
		}
		
		// image filter
		if (this.image && type.startsWith("image")) {
			return true;
		} 
		// video filter
		else if (this.video && type.startsWith("video")) {
			return true;
		}
		// document filter
		else if (this.document) {
			String ext = getExtention(type);
			if (docExtentions.contains(ext)) {
				return true;
			}
		} 
		// audio filter
		else if (this.audio && type.startsWith("audio")) {
			return true;
		}
		return false;
	}
	
	private String getMimeType(String path) {
		return URLConnection.guessContentTypeFromName(path);
	}

	private String getExtention(String path) {
		return FilenameUtils.getExtension(path).toLowerCase();
	}

}
