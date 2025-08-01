package me.eldodebug.soarlauncher.management.file;

import java.io.File;
import java.io.IOException;

import me.eldodebug.soarlauncher.logger.SoarLogger;

public class FileManager {
	
	private File mainDir, minecraftDir, librariesDir, nativesDir, 
					assetsDir, gameDir, javaDir, tempDir;
	
	private File configFile;
	
	public FileManager() {
		
		mainDir = new File(".");
		configFile = new File(mainDir, "Config.json");
		tempDir = new File(mainDir, "temp");
		javaDir = new File(mainDir, "jre1.8.0_333");
		
		createDir(mainDir);
		createFile(configFile);
		createDir(tempDir);
		createDir(javaDir);
	}
	
	public void createMinecraftDir(File dir) {
		
		minecraftDir = dir;
		librariesDir = new File(minecraftDir, "libraries");
		nativesDir = new File(minecraftDir, "natives");
		assetsDir = new File(minecraftDir, "assets");
		gameDir = new File(minecraftDir, "game");
		
		createDir(minecraftDir);
		createDir(librariesDir);
		createDir(nativesDir);
		createDir(assetsDir);
		createDir(gameDir);
	}
	
	public void createDir(File file) {
		if(!file.exists()) {
			file.mkdir();
		}
	}
	
	public void createFile(File file) {
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				SoarLogger.error("Failed to create new fille", e);
			}
		}
	}

	public File getMainDir() {
		return mainDir;
	}

	public File getMinecraftDir() {
		return minecraftDir;
	}

	public File getConfigFile() {
		return configFile;
	}

	public File getLibrariesDir() {
		return librariesDir;
	}

	public File getNativesDir() {
		return nativesDir;
	}

	public File getAssetsDir() {
		return assetsDir;
	}

	public File getGameDir() {
		return gameDir;
	}

	public File getJavaDir() {
		return javaDir;
	}

	public File getTempDir() {
		return tempDir;
	}
}
