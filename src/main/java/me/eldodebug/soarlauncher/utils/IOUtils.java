package me.eldodebug.soarlauncher.utils;

import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import me.eldodebug.soarlauncher.logger.SoarLogger;

public class IOUtils {

	public static ByteBuffer resourceToByteBuffer(InputStream stream) {
		
		try {
			
			byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(stream);
			
	        ByteBuffer data = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder()).put(bytes);
	        ((Buffer) data).flip();
	        
	        return data;
		} catch (Exception e) {
			SoarLogger.error("Failed to load resource", e);
		}
		
		return null;
	}
}
