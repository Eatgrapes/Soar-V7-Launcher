package me.eldodebug.soarlauncher;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import me.eldodebug.soarlauncher.gui.GuiEmpty;
import me.eldodebug.soarlauncher.gui.GuiLoading;
import me.eldodebug.soarlauncher.gui.GuiScreen;
import me.eldodebug.soarlauncher.management.config.ConfigManager;
import me.eldodebug.soarlauncher.management.file.FileManager;
import me.eldodebug.soarlauncher.management.language.LanguageManager;
import me.eldodebug.soarlauncher.management.launch.LaunchManager;
import me.eldodebug.soarlauncher.management.nanovg.NanoVGManager;
import me.eldodebug.soarlauncher.management.nanovg.font.Fonts;
import me.eldodebug.soarlauncher.management.nanovg.font.Icon;
import me.eldodebug.soarlauncher.management.settings.SettingsManager;
import me.eldodebug.soarlauncher.management.update.UpdateManager;
import me.eldodebug.soarlauncher.utils.DialogUtils;
import me.eldodebug.soarlauncher.utils.MouseUtils;
import me.eldodebug.soarlauncher.utils.TimerUtils;
import me.eldodebug.soarlauncher.utils.animation.normal.Animation;
import me.eldodebug.soarlauncher.utils.animation.normal.Direction;
import me.eldodebug.soarlauncher.utils.animation.normal.other.SmoothStepAnimation;

public class SoarLauncher {

	private static SoarLauncher instance = new SoarLauncher();
	
	private TimerUtils fpsTimer = new TimerUtils();
	
	private Animation introAnimation;
	
	private String name, version;
	private long window;
	private int width, height;
	
	private FileManager fileManager;
	private ConfigManager configManager;
	private NanoVGManager nanoVGManager;
	private LanguageManager languageManager;
	private LaunchManager launchManager;
	private SettingsManager settingsManager;
	private UpdateManager updateManager;

	private GuiScreen currentScreen;
	
	private int mouseX, mouseY, draggingX, draggingY;
	private boolean dragging, changed;
	
	public SoarLauncher() {
		
		name = "Soar Launcher";
		version = "2.0.2";
		width = 332;
		height = 482;
		
		this.setScreen(new GuiEmpty());
	}
	
	public void start() {
		
		fileManager = new FileManager();
		languageManager = new LanguageManager();
		launchManager = new LaunchManager();
		settingsManager = new SettingsManager();
		configManager = new ConfigManager();
		updateManager = new UpdateManager();
		
		init();
		loop();
		cleanup();
		System.exit(0);
	}
	
	public void close() {
		configManager.save();
		introAnimation.setDirection(Direction.BACKWARDS);
	}
	
	private void init() {
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		if(!GLFW.glfwInit()) {
			DialogUtils.error("Error", "Failed to create a window. \n "
					+ "It cannot be executed on your computer.");
			System.exit(0);
		}
		
		GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_ALPHA_BITS, 8);
        GLFW.glfwWindowHint(GLFW.GLFW_TRANSPARENT_FRAMEBUFFER, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, GLFW.GLFW_DONT_CARE);
        GLFW.glfwWindowHint(GLFW.GLFW_DOUBLEBUFFER, GLFW.GLFW_TRUE);
        
        window = GLFW.glfwCreateWindow(width, height, name, 0, 0);
        
        if(window == 0) {
			DialogUtils.error("Error", "Failed to create a window. \n "
					+ "It cannot be executed on your computer.");
			System.exit(0);
        }
        
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        
        GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {

			@Override
			public void invoke(long window, int button, int action, int mods) {
				
	            double[] xpos = new double[1];
	            double[] ypos = new double[1];
	            
	            GLFW.glfwGetCursorPos(window, xpos, ypos);
	            
	            int winMouseX = (int) xpos[0];
	            int winMouseY = (int) ypos[0];
	            
				if (action == GLFW.GLFW_PRESS) {
					
		            int[] wxPos = new int[1];
		            int[] wyPos = new int[1];
		            GLFW.glfwGetWindowPos(window, wxPos, wyPos);
		            
					if(MouseUtils.isInside(winMouseX, winMouseY, 0, 0, instance.getWidth(), 28)) {
						dragging = true;
						draggingX = wxPos[0] - mouseX;
						draggingY = wyPos[0] - mouseY;
					}
					
					currentScreen.mouseClicked(winMouseX, winMouseY, button);
				} else if(action == GLFW.GLFW_RELEASE) {
					currentScreen.mouseReleased(winMouseX, winMouseY, button);
					dragging = false;
				}
				
				if(MouseUtils.isInside(winMouseX, winMouseY, width - 16 - 10, 9, 16, 16) && action == 0) {
					SoarLauncher.getInstance().close();
				}
				
				if(MouseUtils.isInside(winMouseX, winMouseY, width - 16 - 10 - 22, 9, 16, 16) && action == 0) {
					GLFW.glfwIconifyWindow(window);
				}
			}
        };
        
        GLFW.glfwSetMouseButtonCallback(window, mouseButtonCallback);
        
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        
        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            GL11.glViewport(0, 0, width, height);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, width, height, 0, 1, -1);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        });
        
		nanoVGManager = new NanoVGManager();
		
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(window);
	}
	
	private void loop() {
		
		introAnimation = new SmoothStepAnimation(340, 1.0F);
		introAnimation.reset();
		changed = false;
		
		while (!GLFW.glfwWindowShouldClose(window)) {
			
			if(fpsTimer.delay(1000 / 60)) {
				
				Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
				mouseX = (int) mouseLocation.getX();
				mouseY = (int) mouseLocation.getY();
	            
		        GLFW.glfwSetWindowOpacity(window, introAnimation.getValueFloat());
		        
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				
				nanoVGManager.setupAndDraw(vg -> {
					
		            double[] xpos = new double[1];
		            double[] ypos = new double[1];
		            
		            GLFW.glfwGetCursorPos(window, xpos, ypos);
		            
		            int winMouseX = (int) xpos[0];
		            int winMouseY = (int) ypos[0];
		            
					nanoVGManager.drawRoundedImage("soar/background.png", 0, 0, width, height, 20);
					nanoVGManager.drawCenteredText(name, width / 2, 9, Color.WHITE, 18, Fonts.MEDIUM);
					nanoVGManager.drawText(Icon.SOAR, 78, 7.5F, Color.WHITE, 18, Fonts.ICON);
					nanoVGManager.drawRoundedRect(width - 16 - 10, 9, 16, 16, 8, new Color(255, 40, 40));
					nanoVGManager.drawRoundedRect(width - 16 - 10 - 22, 9, 16, 16, 8, new Color(255, 136, 0));
					currentScreen.drawScreen(winMouseX, winMouseY);
				});
				
				if(dragging) {
					GLFW.glfwSetWindowPos(window, mouseX + draggingX, mouseY + draggingY);
				}
				
				GLFW.glfwSwapBuffers(window);
				GLFW.glfwPollEvents();
				
				if(introAnimation.isDone(Direction.BACKWARDS)) {
					GLFW.glfwSetWindowShouldClose(window, true);
				}
				
				if(introAnimation.isDone(Direction.FORWARDS) && !changed) {
					changed = true;
					this.setScreen(new GuiLoading());
				}
				
				fpsTimer.reset();
			}
		}
	}
	
	private void cleanup() {
		GLFW.glfwSetMouseButtonCallback(window, null);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}
	
	public void setScreen(GuiScreen screen) {
		
		if(currentScreen != null) {
			currentScreen.onGuiClosed();
		}
		
		currentScreen = screen;
		
		if(currentScreen != null) {
			currentScreen.initGui();
		}
	}

	public static SoarLauncher getInstance() {
		return instance;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public FileManager getFileManager() {
		return fileManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public NanoVGManager getNanoVGManager() {
		return nanoVGManager;
	}

	public LanguageManager getLanguageManager() {
		return languageManager;
	}

	public LaunchManager getLaunchManager() {
		return launchManager;
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public UpdateManager getUpdateManager() {
		return updateManager;
	}
}
