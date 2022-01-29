/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.manianis;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

/**
 *
 * @author hp
 */
public class FileDownloader {

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
	
	public static boolean fileExists(String filePath) {
		Path file = Path.of(filePath);
		return Files.exists(file);
	}

	public static void openFolder(String path) throws IOException {
		Runtime.getRuntime().exec("explorer.exe /select," + path);
	}

	public static void loadFileFromUrl(String theUrl, String filePath) throws MalformedURLException, IOException {
		URL url = new URL(theUrl);
		File destination_file = new File(filePath);
		FileUtils.copyURLToFile(url, destination_file);
	}

	public static String getHttpResponse(String theUrl) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(theUrl)).build();
		HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() >= 200 && response.statusCode() < 300) {
			return response.body().toString();
		}
		throw new IOException("HTTP ERROR: " + response.statusCode() + "\nResources not found!");
	}

	public static String[] getFilesList(String theUrl, String theKeyword) throws IOException, InterruptedException {
		String resUrl = theUrl + "/?op=filename&clef=" + theKeyword;
		String response = getHttpResponse(resUrl);
		String[] res = response.split("\n");
		int count = 0;
		for (int i = 0; i < res.length; i++) {
			if (!res[i].isEmpty()) {
				res[count++] = res[i];
			}
		}
		if (count == res.length) {
			return res;
		}
		return Arrays.copyOf(res, count);
	}
	
	public static void downloadFile(String theUrl, String theKeyword, String theFilename, String localFilepath) throws MalformedURLException, IOException {
		String resUrl = theUrl + "/?op=download&clef=" + theKeyword + "&filename=" + theFilename;
		loadFileFromUrl(resUrl, localFilepath);
	}
}
