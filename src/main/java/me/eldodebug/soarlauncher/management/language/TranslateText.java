package me.eldodebug.soarlauncher.management.language;

public enum TranslateText {

	MAIN_MENU_TITLE("text.mainmenu.title"), LAUNCH("text.launch"), DOWNLOADING("text.downloading"), CHECKING("text.checking"),
	LAUNCHING("text.launching"), MEMORY("text.memory"), LANGUAGE("text.language"), SETTINGS("text.settings"),
	LOCATION("text.location"), ENGLISH("text.english"), FRENCH("text.french"), JAPANESE("text.japanese"), CHINESE("text.chinese"),
	UPDATING("text.updating"), CHECKING_FOR_UPDATE("text.checkingforupdate");
	
	private String key, text;
	
	private TranslateText(String key) {
		this.key = key;
	}
	
	public String getText() {
		return text == null ? "null" : text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getKey() {
		return key;
	}
}
