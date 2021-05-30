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
			Category category = getCategory(decision.getSource().getName());

			// filter if selected
			if (categories.contains(category)) {
				decision.setCategory(category);
				return true;
			}

			return false;
		}

		private Category getCategory(String path) {

			String mimeType = URLConnection.guessContentTypeFromName(path);

			// check if directory
			if(mimeType == null) {
				return Category.OTHERS;
			}
			// check if img, videos, audio
			else if (mimeType.startsWith("image")) {
				return Category.IMAGES;
			}
			else if (mimeType.startsWith("video")) {
				return Category.VIDEOS;
			}
			else if (mimeType.startsWith("audio")) {
				return Category.AUDIO;
			}
			// check if document
			String ext = getExtension(mimeType);
			if (Category.DOCUMENT_EXTS.contains(ext)) {
				return Category.DOCUMENTS;
			}
			// others
			return Category.OTHERS;
		}

		private String getExtension(String path) {
			return FilenameUtils.getExtension(path).toLowerCase();
		}

	}

	
}
