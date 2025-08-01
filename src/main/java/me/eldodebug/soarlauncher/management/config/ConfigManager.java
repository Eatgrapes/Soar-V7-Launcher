package me.eldodebug.soarlauncher.management.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import me.eldodebug.soarlauncher.SoarLauncher;
import me.eldodebug.soarlauncher.logger.SoarLogger;
import me.eldodebug.soarlauncher.management.language.Language;
import me.eldodebug.soarlauncher.management.settings.SettingsManager;
import me.eldodebug.soarlauncher.utils.JsonUtils;

public class ConfigManager {

	public ConfigManager() {
		load();
	}
	
	public void save() {
		
		SoarLauncher instance = SoarLauncher.getInstance();
		SettingsManager settingsManager = instance.getSettingsManager();
		File configFile = instance.getFileManager().getConfigFile();
		
		if(configFile == null || !configFile.exists()) {
			return;
		}
		
		try(FileWriter writer = new FileWriter(configFile)) {
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			
			JsonObject jsonObject = new JsonObject();
			
			jsonObject.addProperty("Location", settingsManager.getLocationSetting().getFile().getAbsolutePath());
			jsonObject.addProperty("Memory", settingsManager.getMemorySetting().getValueInt());
			jsonObject.addProperty("Language", instance.getLanguageManager().getCurrentLanguage().getId());
			
			gson.toJson(jsonObject, writer);
		} catch(Exception e) {
			SoarLogger.error("Failed to save config", e);
		}
	}
	
	public void load() {
		
		SoarLauncher instance = SoarLauncher.getInstance();
		SettingsManager settingsManager = instance.getSettingsManager();
		File configFile = instance.getFileManager().getConfigFile();
		
		if(configFile == null || !configFile.exists()) {
			return;
		}
		
		try(FileReader reader = new FileReader(configFile)) {
			
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
			
			settingsManager.getLocationSetting().setFile(new File(JsonUtils.getStringProperty(jsonObject, "Location", instance.getFileManager().getMainDir().getAbsolutePath())));
			settingsManager.getMemorySetting().setValue(JsonUtils.getIntProperty(jsonObject, "Memory", 4));
			instance.getLanguageManager().setCurrentLanguage(Language.getLanguageById(JsonUtils.getStringProperty(jsonObject, "Language", Language.ENGLISH.getId())));
		} catch(Exception e) {
			SoarLogger.error("Failed to load config", e);
		}
	}
}
