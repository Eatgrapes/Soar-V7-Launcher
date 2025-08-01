package me.eldodebug.soarlauncher.management.settings.impl;

import me.eldodebug.soarlauncher.management.language.TranslateText;

public class BooleanSetting extends Setting {

	private boolean toggled;
	
	public BooleanSetting(TranslateText nameTranslate, boolean toggled) {
		super(nameTranslate);
		this.toggled = toggled;
	}

	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}
}
