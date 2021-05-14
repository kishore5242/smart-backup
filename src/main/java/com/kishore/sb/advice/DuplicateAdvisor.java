package com.kishore.sb.advice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.kishore.sb.model.Command;
import com.kishore.sb.model.Decision;

public class DuplicateAdvisor extends Advisor {

	private List<File> referenceFiles = new ArrayList<>();
	private List<String> referenceFileNames = new ArrayList<>();

	public DuplicateAdvisor(Command command) {
		super(command);
		if(command.getOperation().getAvoidDuplication()) {
			File referenceDir = new File(command.getRhs());
			listReferenceFiles(referenceDir);
			this.referenceFiles.stream().forEach(file -> referenceFileNames.add(file.getName()));
		}
	}

	@Override
	public boolean advise(Decision decision) {
		if(command.getOperation().getAvoidDuplication()) {
			return !isDuplicate(decision.getSource());
		}
		return true;
	}

	private void listReferenceFiles(final File folder) {
		for (final File f : folder.listFiles()) {
			if (f.isDirectory()) {
				listReferenceFiles(f);
			}
			if (f.isFile()) {
				this.referenceFiles.add(f);
			}
		}
	}

	private boolean isDuplicate(File file) {
		if (referenceFileNames.contains(file.getName())) {
			List<File> existingFiles = findByName(this.referenceFiles, file.getName());
			return existingFiles.stream().anyMatch(existingFile -> areFilesSame(existingFile, file));
		}
		return false;
	}

	private List<File> findByName(List<File> among, String name) {
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
