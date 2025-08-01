package me.eldodebug.soarlauncher.management.nanovg.font;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class Font {

	private String name;
	private InputStream inputStream;
	private boolean loaded;
	private ByteBuffer buffer;
	
	public Font(String name, InputStream inputStream) {
		this.name = name;
		this.inputStream = inputStream;
		this.loaded = false;
		this.buffer = null;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getName() {
		return name;
	}
}