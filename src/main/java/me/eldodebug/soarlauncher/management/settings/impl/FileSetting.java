package me.eldodebug.soarlauncher.management.settings.impl;

import java.io.File;

import me.eldodebug.soarlauncher.management.language.TranslateText;

public class FileSetting extends Setting {

	private File file;
	
	public FileSetting(TranslateText nameTranslate, File file) {
		super(nameTranslate);
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
