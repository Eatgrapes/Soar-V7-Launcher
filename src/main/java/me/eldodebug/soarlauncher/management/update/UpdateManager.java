package me.eldodebug.soarlauncher.management.update;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import com.google.gson.JsonObject;

import me.eldodebug.soarlauncher.SoarLauncher;
import me.eldodebug.soarlauncher.management.file.FileManager;
import me.eldodebug.soarlauncher.utils.DialogUtils;
import me.eldodebug.soarlauncher.utils.FileUtils;
import me.eldodebug.soarlauncher.utils.Multithreading;
import me.eldodebug.soarlauncher.utils.network.HttpUtils;
import me.eldodebug.soarlauncher.utils.network.UserAgents;

public class UpdateManager {

	private static final Logger LOGGER = Logger.getLogger(UpdateManager.class.getName());

	private String apiUrl;
	private boolean localHost;
	private String clientVersion, launcherVersion;
	private int clientSize, launcherSize;
	private boolean done, needUpdate;

	public UpdateManager() {
		setupLogger();

		SoarLauncher instance = SoarLauncher.getInstance();

		localHost = false;
		apiUrl = localHost ? "http://localhost:8080/v1" : "https://api.soarclient.com/v1";
		done = false;
		needUpdate = false;

		LOGGER.info("Initializing UpdateManager");

		Multithreading.runAsync(() -> {
			LOGGER.info("Starting update check");
			postClientInfo();
			postLauncherInfo();

			if(launcherVersion != null && !launcherVersion.equals(instance.getVersion())) {
				LOGGER.info("Update needed. Current version: " + instance.getVersion() + ", Latest version: " + launcherVersion);
				needUpdate = true;
			} else {
				LOGGER.info("No update needed. Current version: " + instance.getVersion());
			}

			done = true;
			LOGGER.info("Update check completed");
		});
	}

	private void setupLogger() {
		try {
			FileHandler fileHandler = new FileHandler("updater.log", true);
			fileHandler.setFormatter(new SimpleFormatter());
			LOGGER.addHandler(fileHandler);
			LOGGER.setLevel(Level.ALL);
		} catch (IOException e) {
			System.err.println("Failed to set up logger: " + e.getMessage());
		}
	}

	public void updateLauncher() {
		LOGGER.info("updateLauncher method called");
		Multithreading.runAsync(() -> startLauncherUpdate());
	}

	private void startLauncherUpdate() {
		LOGGER.info("Starting launcher update process");

		FileManager fileManager = SoarLauncher.getInstance().getFileManager();
		File tempDir = fileManager.getTempDir();

		if(tempDir.exists()) {
			LOGGER.info("Deleting existing temp directory");
			FileUtils.deleteDirectory(tempDir);
			fileManager.createDir(tempDir);
		}

		downloadUpdater();
	}

	private void downloadUpdater() {
		LOGGER.info("Downloading updater");

		FileManager fileManager = SoarLauncher.getInstance().getFileManager();
		File updaterFile = new File(fileManager.getTempDir(), "Updater.jar");

		HttpUtils.downloadFile("https://files.soarclient.com/v1/data/launcher/Updater.jar", updaterFile);

		if(!updaterFile.exists() || updaterFile.length() != 1756) {
			LOGGER.warning("Updater download failed or incorrect size. Retrying.");
			downloadUpdater();
		} else {
			LOGGER.info("Updater downloaded successfully");
			downloadLauncher();
		}
	}

	private void downloadLauncher() {
		LOGGER.info("Downloading launcher");

		FileManager fileManager = SoarLauncher.getInstance().getFileManager();
		File launcherFile = new File(fileManager.getTempDir(), "launchertemp.exe");

		HttpUtils.downloadFile("https://files.soarclient.com/v1/data/launcher/Soar%20Launcher.exe", launcherFile);

		if(!launcherFile.exists() || (launcherSize != 0 && launcherFile.length() != launcherSize)) {
			LOGGER.warning("Launcher download failed or incorrect size. Retrying.");
			downloadLauncher();
		} else {
			LOGGER.info("Launcher downloaded successfully");
			moveLauncher();
		}
	}

	private void moveLauncher() {
		LOGGER.info("Moving launcher");

		FileManager fileManager = SoarLauncher.getInstance().getFileManager();

		File launcherFile = new File(fileManager.getTempDir(), "launchertemp.exe");
		File destLauncherFile = new File(fileManager.getMainDir(), "Soar Client.exe");
		File javaFile = new File(fileManager.getJavaDir(), "bin/javaw.exe");
		File updaterFile = new File(fileManager.getTempDir(), "Updater.jar");

		ArrayList<String> launchCommands = new ArrayList<String>();

		launchCommands.add(javaFile.getAbsolutePath());
		launchCommands.add("-jar");
		launchCommands.add(updaterFile.getAbsolutePath());
		launchCommands.add(launcherFile.getAbsolutePath());
		launchCommands.add(destLauncherFile.getAbsolutePath());

		LOGGER.info("Launching updater with commands: " + String.join(" ", launchCommands));

		try {
			new ProcessBuilder(launchCommands).start();
			LOGGER.info("Updater process started. Exiting current process.");
			System.exit(0);
		} catch (IOException e) {
			LOGGER.severe("Error launching updater: " + e.toString());
			DialogUtils.error("Error", e.toString());
		}
	}

	private void postClientInfo() {
		LOGGER.info("Posting client info");

		JsonObject jsonObject = HttpUtils.readJson(apiUrl + "/version/getClientInfo", null, UserAgents.SOAR);

		if(!jsonObject.isJsonObject()) {
			LOGGER.warning("Invalid JSON response for client info");
			return;
		}

		if(jsonObject.get("success").getAsBoolean()) {
			clientVersion = jsonObject.get("version").getAsString();
			clientSize = jsonObject.get("size").getAsInt();
			LOGGER.info("Client info received. Version: " + clientVersion + ", Size: " + clientSize);
		} else {
			LOGGER.warning("Failed to get client info");
		}
	}

	private void postLauncherInfo() {
		LOGGER.info("Posting launcher info");

		JsonObject jsonObject = HttpUtils.readJson(apiUrl + "/version/getLauncherInfo", null, UserAgents.SOAR);

		if(!jsonObject.isJsonObject()) {
			LOGGER.warning("Invalid JSON response for launcher info");
			return;
		}

		if(jsonObject.get("success").getAsBoolean()) {
			launcherVersion = jsonObject.get("version").getAsString();
			launcherSize = jsonObject.get("size").getAsInt();
			LOGGER.info("Launcher info received. Version: " + launcherVersion + ", Size: " + launcherSize);
		} else {
			LOGGER.warning("Failed to get launcher info");
		}
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public String getLauncherVersion() {
		return launcherVersion;
	}

	public int getClientSize() {
		return clientSize;
	}

	public int getLauncherSize() {
		return launcherSize;
	}

	public boolean isDone() {
		return done;
	}

	public boolean isNeedUpdate() {
		return needUpdate;
	}
}