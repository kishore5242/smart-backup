package com.kishore.sb.model;

import java.util.Set;

public final class OperationData {
	
	public static final String JOB_TYPE_COPY = "Copy";
	public static final String JOB_TYPE_MOVE = "Move";
	public static final String JOB_TYPE_BACKUP = "Backup";
	public static final String JOB_TYPE_DELETE = "Delete";
	
	public static final String FILE_TYPE_IMAGES = "Images";
	public static final String FILE_TYPE_VIDEOS = "Videos";
	public static final String FILE_TYPE_DOCUMENTS = "Documents";
	public static final String FILE_TYPE_ALL = "All";

	public static final Set<String> JOB_TYPES = Set.of(JOB_TYPE_COPY, JOB_TYPE_MOVE, JOB_TYPE_BACKUP, JOB_TYPE_DELETE);
	public static final Set<String> FILE_TYPES = Set.of(FILE_TYPE_IMAGES, FILE_TYPE_VIDEOS, FILE_TYPE_DOCUMENTS, FILE_TYPE_ALL);
	
	public static final Set<String> EXTENTIONS_IMAGES = Set.of("png", "jpg", "jpeg", "gif");
	public static final Set<String> EXTENTIONS_VIDEOS = Set.of("mp4", "amw", "mpeg");
	public static final Set<String> EXTENTIONS_DOCUMENTS = Set.of("doc", "docx", "pdf");
	
	private OperationData() {
	}
}
