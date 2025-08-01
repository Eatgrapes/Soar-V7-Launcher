package me.eldodebug.soarlauncher.management.nanovg.asset;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.stb.STBImage;

import me.eldodebug.soarlauncher.utils.FileUtils;
import me.eldodebug.soarlauncher.utils.IOUtils;

public class AssetManager {

	private HashMap<String, NVGAsset> imageCache = new HashMap<String, NVGAsset>();
	
	public boolean loadImage(long nvg, String path) {
		
		if(!imageCache.containsKey(path)) {
			
			int[] width = {0};
			int[] height = {0};
			int[] channels = {0};
			
			ByteBuffer image = IOUtils.resourceToByteBuffer(FileUtils.getResource(path));
			
			if(image == null) {
				return false;
			}
			
			ByteBuffer buffer = STBImage.stbi_load_from_memory(image, width, height, channels, 4);
			
			if(buffer == null) {
				return false;
			}
			
			imageCache.put(path, new NVGAsset(NanoVG.nvgCreateImageRGBA(nvg, width[0], height[0], NanoVG.NVG_IMAGE_REPEATX | NanoVG.NVG_IMAGE_REPEATY | NanoVG.NVG_IMAGE_GENERATE_MIPMAPS, buffer), width[0], height[0]));
			
			return true;
		}
		
		return true;
	}
	
	public int getImage(String path) {
		return imageCache.get(path).getImage();
	}
}
