package org.manianis.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class FolderUtil {
	
	private static Map<String, String> specialFolders = null;
	
	private static class StreamReader extends Thread {
		private InputStream is;
		private StringWriter sw = new StringWriter();

		public StreamReader(InputStream is) {
			this.is = is;
		}

		public void run() {
			try {
				int c;
				while ((c = is.read()) != -1)
					sw.write(c);
			} catch (IOException e) {
			}
		}

		public String getResult() {
			return sw.toString();
		}
	}
	
	private static final Map<String, String> readSpecialFolders() {
		try {
			// Run reg query, then read output with StreamReader (internal class)
			Process process = Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders\"");
			StreamReader reader = new StreamReader(process.getInputStream());
			reader.start();
			process.waitFor();
			reader.join();
			String result = reader.getResult();
			String[] lines = result.split("\r\n");
			Map<String, String> specialFolders = new HashMap<>();
			for (String line : lines) {
				String[] lineFrags = line.split("\s{2,}");
				if (lineFrags.length < 4 || !lineFrags[2].equalsIgnoreCase("REG_EXPAND_SZ")) {
					continue;
				}
				specialFolders.put(lineFrags[1], lineFrags[3].replace("%USERPROFILE%", getHomeFolder()));
			}
			return specialFolders;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Map<String, String> getSpecialFolders() {
		if (specialFolders == null) {
			specialFolders = readSpecialFolders();
		}
		return specialFolders;
	}
	
	public static String getSpecialFolder(String name) {
		return getSpecialFolders().get(name);
	}
	
	
	/**
	 * Open the specified folder in the Explorer
	 * @param path
	 * @throws IOException
	 */
	public static void openFolder(String path) throws IOException {
		Runtime.getRuntime().exec("explorer.exe /select," + path);
	}
	
	/**
	 * Creates the folders recursively to create the specified folder.
	 * @param folderPath
	 * @return true if the folder is created, false otherwise
	 */
	public static boolean createFolder(String folderPath) {
		Path path = Path.of(folderPath);
		if (Files.exists(path)) {
			return Files.isDirectory(path);
		}
		try {
			Files.createDirectories(path);
			return true;
		} catch (IOException ex) {
			return false;
		}
	}
	
	/**
	 * Returns if the filePath exists or not.
	 * @param filePath
	 * @return
	 */
	public static boolean fileExists(String filePath) {
		Path file = Path.of(filePath);
		return Files.exists(file);
	}
	
	/**
	 * Return the user home folder.
	 * @return
	 */
	public static String getHomeFolder() {
		return System.getProperty("user.home");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDesktopFolder() {
		return getSpecialFolder("Desktop");
	}
	
	public static String getDownloadsFolder() {
		return getSpecialFolder("{374DE290-123F-4565-9164-39C4925E467B}");
	}
	
	public static String getDocumentsFolder() {
		return getSpecialFolder("{F42EE2D3-909F-4907-8871-4C22FC0BF756}");
	}
}
