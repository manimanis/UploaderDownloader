package org.manianis.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileEntry implements Comparable<FileEntry> {
	String fileName;
	String filePath;
	
	public FileEntry(String filePath) {
		setFilePath(filePath);
	}
	
	public void setFilePath(String filePath) {
		Path path = Paths.get(filePath);
		this.filePath = path.toAbsolutePath().toString();
		this.fileName = path.getFileName().toString();
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	@Override
	public String toString() {
		return fileName;
	}

	@Override
	public int compareTo(FileEntry otherFileEntry) {
		return filePath.compareTo(otherFileEntry.filePath);
	}

	@Override
	public int hashCode() {
		return Objects.hash(filePath);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof FileEntry))
			return false;
		FileEntry other = (FileEntry) obj;
		return Objects.equals(filePath, other.filePath);
	}
	
}
