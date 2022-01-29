/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.manianis;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

/**
 *
 * @author hp
 */
public class ApplicationsParameters {

    private Preferences prefs;
    private static ApplicationsParameters appsParams = null;
    private String workingDirectory;
    private String userName;
    private String lastUrlAddress;
    private String keyword;
    private String uploadUrl;

    private ApplicationsParameters() {
        prefs = Preferences.userRoot().node(this.getClass().getName());
        load();
    }

    public static ApplicationsParameters getInstance() {
        if (appsParams == null) {
            appsParams = new ApplicationsParameters();
        }
        return appsParams;
    }
    
    public void load() {
        workingDirectory = prefs.get("workingDirectory", "C:\\BAC2022");
        userName = prefs.get("userName", "Sallouha");
        lastUrlAddress = prefs.get("lastUrlAddress", "");
        keyword = prefs.get("keyword", "");
        uploadUrl = prefs.get("uploadUrl", "");
    }
    
    public void save() {
        prefs.put("workingDirectory", workingDirectory);
        prefs.put("userName", userName);
        prefs.put("lastUrlAddress", lastUrlAddress);
        prefs.put("keyword", keyword);
        prefs.put("uploadUrl", uploadUrl);
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public ApplicationsParameters setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public ApplicationsParameters setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getLastUrlAddress() {
        return lastUrlAddress;
    }

    public ApplicationsParameters setLastUrlAddress(String lastUrlAddress) {
        this.lastUrlAddress = lastUrlAddress;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public ApplicationsParameters setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
    
    public String getUserFolder() {
        Path userPath = Paths.get(workingDirectory, userName);
        return userPath.toFile().getAbsolutePath();
    }
    
    public String getResourceUrl() {
        if (lastUrlAddress.isEmpty() || keyword.isEmpty()) {
            return "";
        }
        return lastUrlAddress + "/?clef=" + keyword;
    }
    
    public String getUploadUrl() {
		return uploadUrl;
	}
    
    public ApplicationsParameters setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
		return this;
	}
}
