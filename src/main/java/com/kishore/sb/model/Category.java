package com.kishore.sb.model;

import java.util.Set;

public enum Category {
	IMAGES,
	VIDEOS,
	DOCUMENTS,
	AUDIO,
	OTHERS;
	public static final Set<String> DOCUMENT_EXTS = Set.of("pdf", "doc", "docx", "odt", "ods", "xls", "xlsx", "ppt", "pptx", "txt");
}
