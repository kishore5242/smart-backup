package com.kishore.sb.advice.impl;

import static com.kishore.sb.model.Job.BACKUP;
import static com.kishore.sb.model.Job.COPY;
import static com.kishore.sb.model.Job.DELETE;

import java.net.URLConnection;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import com.kishore.sb.advice.Advisor;
import com.kishore.sb.advice.AdvisorProvider;
import com.kishore.sb.model.Category;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.Decision;
import com.kishore.sb.model.Job;

@Component
public class CategoryAdvisorProvider implements AdvisorProvider {
	
	@Override
	public Set<Job> appliesTo() {
		return Set.of(COPY, BACKUP, DELETE);
	}

	@Override
	public Advisor getAdvisor(Command command) {
		return new CategoryAdvisor(command);
	}
	
	private class CategoryAdvisor implements Advisor {
		
		private Command command;

		public CategoryAdvisor(Command command) {
			this.command = command;
		}
		
		@Override
		public int order() {
			return 20;
		}

		@Override
		public boolean advise(Decision decision) {

			Set<Category> categories = command.getOperation().getCategories();
			
			String type = getMimeType(decision.getSource().getName());
			if (type == null) {
				return false;
			}

			// image filter
			if (categories.contains(Category.IMAGES) && type.startsWith("image")) {
				decision.setCategory(Category.IMAGES);
				return true;
			}
			// video filter
			else if (categories.contains(Category.VIDEOS) && type.startsWith("video")) {
				decision.setCategory(Category.VIDEOS);
				return true;
			}
			// document filter
			else if (categories.contains(Category.DOCUMENTS)) {
				String ext = getExtention(type);
				if (Category.DOCUMENT_EXTS.contains(ext)) {
					decision.setCategory(Category.DOCUMENTS);
					return true;
				}
			}
			// audio filter
			else if (categories.contains(Category.AUDIO) && type.startsWith("audio")) {
				decision.setCategory(Category.AUDIO);
				return true;
			}
			// others filter
			if (categories.contains(Category.OTHERS)) {
				decision.setCategory(Category.OTHERS);
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

	
}
