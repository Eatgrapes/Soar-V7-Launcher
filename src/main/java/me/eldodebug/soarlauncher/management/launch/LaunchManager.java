package me.eldodebug.soarlauncher.management.launch;

import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import me.eldodebug.soarlauncher.SoarLauncher;
import me.eldodebug.soarlauncher.logger.SoarLogger;
import me.eldodebug.soarlauncher.management.file.FileManager;
import me.eldodebug.soarlauncher.management.launch.assets.Assets;
import me.eldodebug.soarlauncher.management.launch.assets.linux.LinuxAssets;
import me.eldodebug.soarlauncher.management.launch.assets.windows.WindowsAssets;
import me.eldodebug.soarlauncher.management.launch.file.DownloadFile;
import me.eldodebug.soarlauncher.management.launch.file.DownloadZipFile;
import me.eldodebug.soarlauncher.management.settings.SettingsManager;
import me.eldodebug.soarlauncher.management.update.UpdateManager;
import me.eldodebug.soarlauncher.utils.FileUtils;
import me.eldodebug.soarlauncher.utils.Multithreading;
import me.eldodebug.soarlauncher.utils.OSType;
import me.eldodebug.soarlauncher.utils.network.HttpUtils;

public class LaunchManager {

	private boolean debugMode = false;

	private ArrayList<DownloadFile> downloadFiles = new ArrayList<DownloadFile>();
	private LaunchProgress progress;
	
	public LaunchManager() {
		progress = LaunchProgress.LAUNCH;
	}
	
	public void launch() {
		
		SoarLauncher instance = SoarLauncher.getInstance();
		FileManager fileManager = instance.getFileManager();
		
		if(fileManager.getMinecraftDir() == null) {
			fileManager.createMinecraftDir(instance.getSettingsManager().getLocationSetting().getFile());
		}
		
		Assets assets = null;
		OSType os = OSType.getType();
		
		if(os.equals(OSType.WINDOWS)) {
			assets = new WindowsAssets();
		} else if(os.equals(OSType.LINUX)) {
			assets = new LinuxAssets();
		}
		
		if(assets != null) {
			assets.load();
			downloadFiles = assets.getDownloadFiles();
		}
		
		Multithreading.runAsync(() -> startDownloads());
	}
	
	private void startDownloads() {
		
		progress = LaunchProgress.DOWNLOADING;
		
		for(DownloadFile df : downloadFiles) {
			
			if(!df.getOutputDir().exists()) {
				df.getOutputDir().mkdirs();
			}
			
			if(df instanceof DownloadZipFile) {
				
				DownloadZipFile dzf = (DownloadZipFile) df;
				
				if(FileUtils.getDirectorySize(dzf.getOutputDir()) < dzf.getUnzippedSize()) {
					
					File outputFile = new File(dzf.getOutputDir(), dzf.getFileName());
					HttpUtils.downloadFile(dzf.getUrl(), outputFile);
					FileUtils.unzip(outputFile, dzf.getOutputDir());
					outputFile.delete();
				}
			} else {
				
				File outputFile = new File(df.getOutputDir(), df.getFileName());
				
				if(outputFile.length() != df.getSize()) {
					HttpUtils.downloadFile(df.getUrl(), outputFile);
				}
			}
		}
		
		checkFiles();
	}
	
	private void checkFiles() {
		
		progress = LaunchProgress.CHECKING;
		
		for(DownloadFile df : downloadFiles) {
			
			if(df instanceof DownloadZipFile) {
				
				DownloadZipFile dzf = (DownloadZipFile) df;
				
				if(FileUtils.getDirectorySize(dzf.getOutputDir()) < dzf.getUnzippedSize()) {
					startDownloads();
				}
			} else {
				
				File outputFile = new File(df.getOutputDir(), df.getFileName());
				
				if(outputFile.length() != df.getSize()) {
					startDownloads();
				}
			}
		}
		
		removeOldLibraries();
		
		downloadSoar();
	}
	
	private void downloadSoar() {
		
		progress = LaunchProgress.UPDATING;
		
		SoarLauncher instance = SoarLauncher.getInstance();
		FileManager fileManager = instance.getFileManager();
		UpdateManager updateManager = instance.getUpdateManager();
		int size = updateManager.getClientSize();
		File soarFile = new File(fileManager.getMinecraftDir(), "SoarClient.jar");
		File versionFile = new File(fileManager.getGameDir(), "soar/cache/version/" + updateManager.getClientVersion() + ".tmp");

		if(!debugMode) {
			if(!soarFile.exists() || soarFile.length() != size || !versionFile.exists()) {
				HttpUtils.downloadFile("https://files.soarclient.com/v1/data/client/SoarClient.jar", soarFile);
			}
		}
		
		startClient();
	}
	
	private void startClient() {
		
		progress = LaunchProgress.LAUNCHING;
		
		SoarLauncher instance = SoarLauncher.getInstance();
		FileManager fileManager = instance.getFileManager();
		SettingsManager settingsManager = instance.getSettingsManager();

		ArrayList<String> launchCommands = new ArrayList<String>();
		ArrayList<String> classPaths = new ArrayList<String>();
		
		String username = "Soar User";
		String accessToken = "0";
		String uuid = "0";
		String version = "1.8.9";
		
		int memory = settingsManager.getMemorySetting().getValueInt();
		
		File javaFile = null;
		
		switch(OSType.getType()) {
			case LINUX:
				javaFile = new File(fileManager.getJavaDir(), "bin/java");
				javaFile.setExecutable(true);
				break;
			case MAC:
				break;
			case UNKNOWN:
				break;
			case WINDOWS:
				javaFile = new File(fileManager.getJavaDir(), "bin/javaw");
				break;
			default:
				break;
		}
		
		if(javaFile != null) {
			launchCommands.add(javaFile.getAbsolutePath());
		}
		
        launchCommands.add("-Xmx" + memory + "G");
        launchCommands.add("-Xms" + memory + "G");
        launchCommands.add("-XX:+DisableExplicitGC");
        launchCommands.add("-XX:+UseConcMarkSweepGC");
        launchCommands.add("-XX:+UseParNewGC");
        launchCommands.add("-XX:+UseNUMA");
        launchCommands.add("-XX:+CMSParallelRemarkEnabled");
        launchCommands.add("-XX:MaxTenuringThreshold=15");
        launchCommands.add("-XX:MaxGCPauseMillis=30");
        launchCommands.add("-XX:GCPauseIntervalMillis=150");
        launchCommands.add("-XX:+UseAdaptiveGCBoundary");
        launchCommands.add("-XX:-UseGCOverheadLimit");
        launchCommands.add("-XX:+UseBiasedLocking");
        launchCommands.add("-XX:SurvivorRatio=8");
        launchCommands.add("-XX:TargetSurvivorRatio=90");
        launchCommands.add("-XX:MaxTenuringThreshold=15");
        launchCommands.add("-Dfml.ignorePatchDiscrepancies=true");
        launchCommands.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
        launchCommands.add("-XX:+UseFastAccessorMethods");
        launchCommands.add("-XX:+UseCompressedOops");
        launchCommands.add("-XX:+OptimizeStringConcat");
        launchCommands.add("-XX:+AggressiveOpts");
        launchCommands.add("-XX:ReservedCodeCacheSize=2048m");
        launchCommands.add("-XX:+UseCodeCacheFlushing");
        launchCommands.add("-XX:SoftRefLRUPolicyMSPerMB=10000");
        launchCommands.add("-XX:ParallelGCThreads=10");
        launchCommands.add("-Djava.library.path=" + fileManager.getNativesDir().getAbsolutePath());
        
        for(File f : fileManager.getLibrariesDir().listFiles()) {
    		classPaths.add(f.getAbsolutePath());
        }
        
        classPaths.add(new File(fileManager.getMinecraftDir(), "1.8.9.jar").getAbsolutePath());
        classPaths.add(new File(fileManager.getMinecraftDir(), "SoarClient.jar").getAbsolutePath());
        
        launchCommands.add("-cp");
        launchCommands.add(String.join(File.pathSeparator, classPaths));
        launchCommands.add("net.minecraft.launchwrapper.Launch");
        launchCommands.add("--accessToken");
        launchCommands.add(accessToken);
        launchCommands.add("--version");
        launchCommands.add(version);
        launchCommands.add("--username");
        launchCommands.add(username);
        launchCommands.add("--uuid");
        launchCommands.add(uuid);
        launchCommands.add("--gameDir");
        launchCommands.add(fileManager.getGameDir().getAbsolutePath());
        launchCommands.add("--assetsDir");
        launchCommands.add(fileManager.getAssetsDir().getAbsolutePath());
        launchCommands.add("--assetIndex");
        launchCommands.add("1.8");
        launchCommands.add("--tweakClass");
        launchCommands.add("me.eldodebug.soar.injection.mixin.SoarTweaker");

		try {

			new ProcessBuilder(launchCommands).start();
			/*

			System.out.println("Launch commands: " + String.join(" ", launchCommands));

        	Process process = new ProcessBuilder(launchCommands).start();

            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

			*/

			instance.close();
		} catch(Exception e) {
			SoarLogger.error("Failed to launch Soar Client: " + e);
		}
	}
	
	private void removeOldLibraries() {
		
		FileManager fileManager = SoarLauncher.getInstance().getFileManager();
		File libDir = fileManager.getLibrariesDir();
		
		File nanoVgWin = new File(libDir, "lwjgl-nanovg-natives-windows.jar");
		File lwjgl = new File(libDir, "lwjgl-natives-windows.jar");
		File patched = new File(libDir, "LWJGL-Patched.jar");
		File stbFile = new File(libDir, "lwjgl-stb-natives-windows.jar");
		
		if(nanoVgWin.exists()) {
			nanoVgWin.delete();
		}
		
		if(lwjgl.exists()) {
			lwjgl.delete();
		}
		
		if(patched.exists()) {
			patched.delete();
		}
		
		if(stbFile.exists()) {
			stbFile.delete();
		}
	}

	public LaunchProgress getProgress() {
		return progress;
	}
}
