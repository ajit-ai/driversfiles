package com.driversfiles.www.core.data;

public enum DocumentType {
	
	DOC_TYPE_CDL("CDL"),
	DOC_TYPE_MED_CARD("Medical Card"),
	DOC_TYPE_PHYSICAL("Long Form Physical"),
	DOC_TYPE_SS_CARD("Social Securtiy Card");
	
	private String title;
	
	private DocumentType(String title) {
		this.title = title;
	}
	
	public String getName() {
		return this.name();
	}
	
	public String getTitle() {
		return this.title;
	}

}
