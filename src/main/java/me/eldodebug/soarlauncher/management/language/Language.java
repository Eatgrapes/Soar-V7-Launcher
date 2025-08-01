package me.eldodebug.soarlauncher.management.language;

public enum Language {

	JAPANESE("ja", TranslateText.JAPANESE),
	CHINESE("cn", TranslateText.CHINESE),
	ENGLISH("en", TranslateText.ENGLISH),
	FRENCH("fr", TranslateText.FRENCH);
	
	private String id;
	private TranslateText nameTranslate;
	
	private Language(String id, TranslateText nameTranslate) {
		this.id = id;
		this.nameTranslate = nameTranslate;
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return nameTranslate.getText();
	}
	
	public static Language getLanguageById(String id) {
		
		for(Language lg : Language.values()) {
			if(lg.getId().equals(id)) {
				return lg;
			}
		}
		
		return Language.ENGLISH;
	}
}
