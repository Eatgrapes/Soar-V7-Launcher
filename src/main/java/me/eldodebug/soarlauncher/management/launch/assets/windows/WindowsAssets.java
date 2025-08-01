package me.eldodebug.soarlauncher.management.launch.assets.windows;

import java.io.File;

import me.eldodebug.soarlauncher.SoarLauncher;
import me.eldodebug.soarlauncher.management.file.FileManager;
import me.eldodebug.soarlauncher.management.launch.assets.Assets;

public class WindowsAssets extends Assets {

	@Override
	public void load() {
		super.load();
		
		SoarLauncher instance = SoarLauncher.getInstance();
		FileManager fileManager = instance.getFileManager();
		
		String baseUrl = "https://files.soarclient.com/v1/data/";
		
		String nativesUrl = baseUrl + "natives/";
		File nativesDir = fileManager.getNativesDir();
		
		add(nativesUrl + "windows.zip", "windows.zip", nativesDir, 8584675, 25323707);
	}
}
