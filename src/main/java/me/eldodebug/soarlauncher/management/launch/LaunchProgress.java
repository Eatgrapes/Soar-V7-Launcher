package me.eldodebug.soarlauncher.management.launch;

import me.eldodebug.soarlauncher.management.language.TranslateText;

public enum LaunchProgress {
	LAUNCH(TranslateText.LAUNCH),
	DOWNLOADING(TranslateText.DOWNLOADING), 
	CHECKING(TranslateText.CHECKING),
	UPDATING(TranslateText.UPDATING),
	LAUNCHING(TranslateText.LAUNCHING);
	
	private TranslateText progressTranslate;
	
	private LaunchProgress(TranslateText progressTranslate) {
		this.progressTranslate = progressTranslate;
	}
	
	public String getProgress() {
		return progressTranslate.getText();
	}
}
