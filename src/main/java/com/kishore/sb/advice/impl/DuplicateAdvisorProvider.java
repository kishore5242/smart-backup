package com.kishore.sb.advice.impl;

import static com.kishore.sb.model.Job.BACKUP;
import static com.kishore.sb.model.Job.COPY;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.kishore.sb.advice.Advisor;
import com.kishore.sb.advice.AdvisorProvider;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.Decision;
import com.kishore.sb.model.Job;

@Component
public class DuplicateAdvisorProvider implements AdvisorProvider {
	
	@Override
	public Set<Job> appliesTo() {
		return Set.of(COPY, BACKUP);
	}

	@Override
	public Advisor getAdvisor(Command command) {
		return new DuplicateAdvisor(command);
	}
	
	private class DuplicateAdvisor implements Advisor {

		private Command command;
		private Collection<File> referenceFiles;
		private List<String> referenceFileNames = new ArrayList<>();

		public DuplicateAdvisor(Command command) {
			this.command = command;
			if(command.getOperation().getAvoidDuplication()) {
				File referenceDir = new File(command.getRhs());
				referenceFiles = FileUtils.listFiles(referenceDir, null, true);
				this.referenceFiles.stream().forEach(file -> referenceFileNames.add(file.getName()));
			}
		}
		
		@Override
		public int order() {
			return 40;
		}

		@Override
		public boolean advise(Decision decision) {
			if(command.getOperation().getAvoidDuplication()) {
				return !isDuplicate(decision.getSource());
			}
			return true;
		}

		private boolean isDuplicate(File file) {
			if (referenceFileNames.contains(file.getName())) {
				List<File> existingFiles = findByName(this.referenceFiles, file.getName());
				return existingFiles.stream().anyMatch(existingFile -> areFilesSame(existingFile, file));
			}
			return false;
		}

		private List<File> findByName(Collection<File> among, String name) {
			return among.stream().filter(file -> file.getName().equals(name)).collect(Collectors.toList());
		}

		private boolean areFilesSame(File file1, File file2) {
			boolean same = false;
			try {
				long checksum1 = FileUtils.checksumCRC32(file1);
				long checksum2 = FileUtils.checksumCRC32(file2);
				if (checksum1 == checksum2) {
					same = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return same;
		}

	}


}
