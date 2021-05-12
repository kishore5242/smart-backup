package com.kishore.sb.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

public class DuplicationFilter implements IOFileFilter {

	private List<File> referenceFiles = new ArrayList<>();
	private List<String> referenceFileNames = new ArrayList<>();

	public DuplicationFilter(IOFileFilter preFilter, File referenceDirectory) {
		listReferenceFiles(preFilter, referenceDirectory);
		this.referenceFiles.stream().forEach(file -> referenceFileNames.add(file.getName()));
	}
	
    private void listReferenceFiles(FileFilter filter, final File folder) {
        for (final File f : folder.listFiles()) {
            if (f.isDirectory()) {
                listReferenceFiles(filter, f);
            }
            if (f.isFile() && filter.accept(f)) {
            	this.referenceFiles.add(f);
            }
        }
    }

	@Override
	public boolean accept(File file) {
		return !isDuplicate(file);
	}

	@Override
	public boolean accept(File dir, String name) {
		// TODO does this depende on directory ?
		return !isDuplicate(new File(dir.getAbsolutePath() + "/" + name));
	}

	
	private boolean isDuplicate(File file) {
		if(referenceFileNames.contains(file.getName())) {
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
			if(checksum1 == checksum2) {
				same = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return same;
	}


}
