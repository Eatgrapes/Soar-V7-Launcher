package me.eldodebug.soarlauncher.management.settings.impl;

import me.eldodebug.soarlauncher.management.language.TranslateText;

public class Setting {

	private TranslateText nameTranslate;
	
	public Setting(TranslateText nameTranslate) {
		this.nameTranslate = nameTranslate;
	}

	public TranslateText getNameTranslate() {
		return nameTranslate;
	}
	
	public String getName() {
		return nameTranslate.getText();
	}
}
