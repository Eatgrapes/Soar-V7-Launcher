package me.eldodebug.soarlauncher.management.launch.assets;

import java.io.File;
import java.util.ArrayList;

import me.eldodebug.soarlauncher.SoarLauncher;
import me.eldodebug.soarlauncher.management.file.FileManager;
import me.eldodebug.soarlauncher.management.launch.file.DownloadFile;
import me.eldodebug.soarlauncher.management.launch.file.DownloadZipFile;

public class Assets {

	private ArrayList<DownloadFile> downloadFiles = new ArrayList<DownloadFile>();
	
	public void load() {
		
		SoarLauncher instance = SoarLauncher.getInstance();
		FileManager fileManager = instance.getFileManager();
		
		String baseUrl = "https://files.soarclient.com/v1/data/";
		
		String librariesUrl = baseUrl + "libraries/shared/";
		File librariesDir = fileManager.getLibrariesDir();
		
		add(librariesUrl + "asm-all-5.0.3.jar", "asm-all-5.0.3.jar", librariesDir, 241639);
		add(librariesUrl + "authlib-1.5.21.jar", "authlib-1.5.21.jar", librariesDir, 64412);
		add(librariesUrl + "codecjorbis-20101023.jar", "codecjorbis-20101023.jar", librariesDir, 103871);
		add(librariesUrl + "codecwav-20101023.jar", "codecwav-20101023.jar", librariesDir, 5618);
		add(librariesUrl + "commons-codec-1.9.jar", "commons-codec-1.9.jar", librariesDir, 263965);
		add(librariesUrl + "commons-compress-1.8.1.jar", "commons-compress-1.8.1.jar", librariesDir, 365552);
		add(librariesUrl + "commons-io-2.4.jar", "commons-io-2.4.jar", librariesDir, 185140);
		add(librariesUrl + "commons-lang3-3.3.2.jar", "commons-lang3-3.3.2.jar", librariesDir, 412739);
		add(librariesUrl + "commons-logging-1.1.3.jar", "commons-logging-1.1.3.jar", librariesDir, 62050);
		add(librariesUrl + "gson-2.2.4.jar", "gson-2.2.4.jar", librariesDir, 190432);
		add(librariesUrl + "guava-17.0.jar", "guava-17.0.jar", librariesDir, 2243036);
		add(librariesUrl + "httpclient-4.3.3.jar", "httpclient-4.3.3.jar", librariesDir, 589512);
		add(librariesUrl + "httpcore-4.3.2.jar", "httpcore-4.3.2.jar", librariesDir, 282269);
		add(librariesUrl + "icu4j-core-mojang-51.2.jar", "icu4j-core-mojang-51.2.jar", librariesDir, 1634692);
		add(librariesUrl + "Java-WebSocket-1.5.4.jar", "Java-WebSocket-1.5.4.jar", librariesDir, 139344);
		add(librariesUrl + "jfxrt.jar", "jfxrt.jar", librariesDir, 18211499);
		add(librariesUrl + "jfxswt.jar", "jfxswt.jar", librariesDir, 33917);
		add(librariesUrl + "jinput-2.0.5.jar", "jinput-2.0.5.jar", librariesDir, 208338);
		add(librariesUrl + "jna-3.4.0.jar", "jna-3.4.0.jar", librariesDir, 1008730);
		add(librariesUrl + "jopt-simple-4.6.jar", "jopt-simple-4.6.jar", librariesDir, 62477);
		add(librariesUrl + "jutils-1.0.0.jar", "jutils-1.0.0.jar", librariesDir, 7508);
		add(librariesUrl + "launchwrapper-1.12.jar", "launchwrapper-1.12.jar", librariesDir, 32999);
		add(librariesUrl + "libraryjavasound-20101123.jar", "libraryjavasound-20101123.jar", librariesDir, 21679);
		add(librariesUrl + "librarylwjglopenal-20100824.jar", "librarylwjglopenal-20100824.jar", librariesDir, 18981);
		add(librariesUrl + "log4j-api-2.0-beta9.jar", "log4j-api-2.0-beta9.jar", librariesDir, 108161);
		add(librariesUrl + "log4j-core-2.0-beta9.jar", "log4j-core-2.0-beta9.jar", librariesDir, 681134);
		add(librariesUrl + "lwjgl_util-2.9.4-nightly-20150209.jar", "lwjgl_util-2.9.4-nightly-20150209.jar", librariesDir, 173887);
		add(librariesUrl + "lwjgl-2.9.4-nightly-20150209.jar", "lwjgl-2.9.4-nightly-20150209.jar", librariesDir, 1047168);
		add(librariesUrl + "lwjgl-platform-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.4-nightly-20150209.jar", librariesDir, 22);
		add(librariesUrl + "netty-1.8.8.jar", "netty-1.8.8.jar", librariesDir, 15966);
		add(librariesUrl + "netty-all-4.0.23.Final.jar", "netty-all-4.0.23.Final.jar", librariesDir, 1779991);
		add(librariesUrl + "optifine.jar", "optifine.jar", librariesDir, 2366273);
		add(librariesUrl + "oshi-core-1.1.jar", "oshi-core-1.1.jar", librariesDir, 30973);
		add(librariesUrl + "platform-3.4.0.jar", "platform-3.4.0.jar", librariesDir, 913436);
		add(librariesUrl + "realms-1.7.59.jar", "realms-1.7.59.jar", librariesDir, 1198123);
		add(librariesUrl + "slf4j-api-2.0.7.jar", "slf4j-api-2.0.7.jar", librariesDir, 63635);
		add(librariesUrl + "soundsystem-20120107.jar", "soundsystem-20120107.jar", librariesDir, 65020);
		add(librariesUrl + "twitch-6.5.jar", "twitch-6.5.jar", librariesDir, 55977);

		add(librariesUrl + "lwjgl-soar.jar", "lwjgl-soar.jar", librariesDir, 1384013);
		add(librariesUrl + "lwjgl-soar-natives.jar", "lwjgl-soar-natives.jar", librariesDir, 1935011);
		add(librariesUrl + "jcef.jar", "jcef.jar", librariesDir, 173068);
		add(librariesUrl + "snakeyaml-2.0.jar", "snakeyaml-2.0.jar", librariesDir, 334803);
		add(librariesUrl + "ViaBackwards-4.7.0-1.20-pre1-SNAPSHOT.jar", "ViaBackwards-4.7.0-1.20-pre1-SNAPSHOT.jar", librariesDir, 935976);
		add(librariesUrl + "ViaRewind-2.0.4-SNAPSHOT.jar", "ViaRewind-2.0.4-SNAPSHOT.jar", librariesDir, 456659);
		add(librariesUrl + "ViaVersion-4.7.0-1.20-pre2-SNAPSHOT.jar", "ViaVersion-4.7.0-1.20-pre2-SNAPSHOT.jar", librariesDir, 3984988);
		
		String assetsUrl = baseUrl + "assets/";
		File assetsDir = fileManager.getAssetsDir();
		
		add(assetsUrl + "assets.zip", "assets.zip", assetsDir, 107482521, 114787997);
		
		add(librariesUrl + "1.8.9.jar", "1.8.9.jar", fileManager.getMinecraftDir(), 8461484);
	}
	
	public void add(String url, String fileName, File outputDir, long size) {
		downloadFiles.add(new DownloadFile(url, fileName, outputDir, size));
	}
	
	public void add(String url, String fileName, File outputDir, long size, long unzippedSize) {
		downloadFiles.add(new DownloadZipFile(url, fileName, outputDir, size, unzippedSize));
	}

	public ArrayList<DownloadFile> getDownloadFiles() {
		return downloadFiles;
	}
}
