package me.eldodebug.soarlauncher.management.settings;

import me.eldodebug.soarlauncher.SoarLauncher;
import me.eldodebug.soarlauncher.management.language.TranslateText;
import me.eldodebug.soarlauncher.management.settings.impl.FileSetting;
import me.eldodebug.soarlauncher.management.settings.impl.NumberSetting;

public class SettingsManager {

	private NumberSetting memorySetting = new NumberSetting(TranslateText.MEMORY, 4, 1, 16, true);
	private FileSetting locationSetting = new FileSetting(TranslateText.LOCATION, SoarLauncher.getInstance().getFileManager().getMainDir());
	
	public NumberSetting getMemorySetting() {
		return memorySetting;
	}

	public FileSetting getLocationSetting() {
		return locationSetting;
	}
}
