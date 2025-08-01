package me.eldodebug.soarlauncher.management.language;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import me.eldodebug.soarlauncher.logger.SoarLogger;
import me.eldodebug.soarlauncher.utils.FileUtils;

public class LanguageManager {

	private HashMap<String, String> translateMap = new HashMap<String, String>();
	
	private Language currentLanguage;
	
	public LanguageManager() {
		this.setCurrentLanguage(Language.ENGLISH);
	}
	
	private void loadMap(String language) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.getResource("soar/language/" + language + ".properties"), StandardCharsets.UTF_8))) {
			
			String s;
			
			while((s = reader.readLine()) != null) {
				
				if(!s.equals("") && !s.startsWith("#")) {
					String[] args = s.split("=");
					
					translateMap.put(args[0], args[1]);
				}
			}
			
		} catch(Exception e) {
			SoarLogger.error("Failed to load translate", e);
		}
	}
	
	public Language getCurrentLanguage() {
		return currentLanguage;
	}

	public void setCurrentLanguage(Language currentLanguage) {
		
		this.currentLanguage = currentLanguage;
		this.loadMap(currentLanguage.getId());
		
		for(TranslateText text : TranslateText.values()) {
			if(translateMap.containsKey(text.getKey())) {
				text.setText(translateMap.get(text.getKey()));
			}
		}
	}
	
	public void switchLanguage() {
		
		switch(currentLanguage) {
			case CHINESE:
				setCurrentLanguage(Language.ENGLISH);
				break;
			case ENGLISH:
				setCurrentLanguage(Language.JAPANESE);
				break;
			case JAPANESE:
				setCurrentLanguage(Language.FRENCH);
				break;
			case FRENCH:
				setCurrentLanguage(Language.CHINESE);
				break;
			default:
				break;
		}
	}
}
