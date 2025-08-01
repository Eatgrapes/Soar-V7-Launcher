package me.eldodebug.soarlauncher.management.nanovg.font;

import java.util.ArrayList;
import java.util.Arrays;

import me.eldodebug.soarlauncher.utils.FileUtils;

public class Fonts {

	private static final String PATH = "soar/fonts/";
	
	public static final Font REGULAR = new Font("regular", FileUtils.getResource(PATH + "MiSans-Regular.ttf"));
	public static final Font MEDIUM = new Font("medium", FileUtils.getResource(PATH + "MiSans-Medium.ttf"));
	public static final Font ICON = new Font("icon", FileUtils.getResource(PATH + "Icon.ttf"));
	
	public static ArrayList<Font> getFonts(){
		return new ArrayList<Font>(Arrays.asList(REGULAR, MEDIUM, ICON));
	}
}
