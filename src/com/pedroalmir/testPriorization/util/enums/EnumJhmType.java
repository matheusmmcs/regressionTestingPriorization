package com.pedroalmir.testPriorization.util.enums;

public enum EnumJhmType {
	CLASS_MAPPING("<class-mapping","class-mapping"), FLOW("<flow","flow"), REPORT("<report","report");

	private String xml;
	private String description;

	EnumJhmType(String xml, String description) {
		this.xml = xml;
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
	
	public String getXml() {
		return this.xml;
	}
}