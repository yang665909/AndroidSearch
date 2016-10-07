package com.example.androidsearch;

public class ImageSearchCriterion {
	private String query;
	private String fileType;
	private String fileSize;
	private String numLimit;
	private String relatedSite;
	
	public ImageSearchCriterion(String query, String fileType, String fileSize, String numLimit
		, String relatedSite) {
		this.query = query;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.numLimit = numLimit;
		this.relatedSite = relatedSite;
	}
	
	public String getQuery() {
		return query;
	}
	
	public String getFileType() {
		return fileType;
	}
	
	public String getFileSize() {
		return fileSize;
	}
	
	public String getNumLimit() {
		return numLimit;
	}

	public String getRelatedSite() {
		return relatedSite;
	}
}
