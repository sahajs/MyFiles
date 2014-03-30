
package com.trupt.myfiles.model.enums;

public enum FileTypeEnum {
	TXT("TXT"), ZIP("ZIP"), RAR("RAR"),
	/*"Documents "*/
	PDF("PDF"), DOC("DOC"), DOCX("DOCX"), XLS("XLS"), XLSX("XLSX"), PPT("PPT"), PPTX("PPTX"), 
	HTML("HTML"), ODF("ODF"), ODT("ODT"), ODS("ODS"), RTF("RTF"), XML("XML"),
	/*"Audio WAV, OGG, IMY, OTA, RTTTL, XMF, MXMF, MP3, FLAC, GP, MP4, M4A, AAC"*/
	WAV("WAV"), OGG("OGG"), IMY("IMY"), OTA("OTA"), RTTL("RTTL"), XMF("XMF"), MXMF("MXMF"), 
	MP3("MP3"), FLAC("FLAC"), GP("3GP"), MP4("MP4"), M4A("M4A"), AAC("AAC"),
	/*"Video 3GP, MP4, WEBM, MKV, FLV, WMV"*/
	WEBM("WEBM"), MKV("MKV"), FLV("FLV"), AVI("AVI"), WMV("WMV"),
	/*Image JPG, JPEG, GIF, PNG, BMP, WEBP*/
	JPG("JPG"), JPEG("JPEG"), GIF("GIF"), PNG("PNG"), 
	
	APK("APK"),

	UNKNOWN("UNKNOWN");

	private String	fileExtension;

	private FileTypeEnum() {
	}

	private FileTypeEnum(String fileExt) {
		fileExtension = fileExt;
	}

	@Override
	public String toString() {
		return fileExtension;
	}

}
