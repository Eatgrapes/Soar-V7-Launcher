package me.eldodebug.soarlauncher.management.nanovg;

import java.awt.Color;
import java.util.function.LongConsumer;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL2;
import org.lwjgl.opengl.GL11;

import me.eldodebug.soarlauncher.SoarLauncher;
import me.eldodebug.soarlauncher.management.nanovg.asset.AssetManager;
import me.eldodebug.soarlauncher.management.nanovg.font.Font;
import me.eldodebug.soarlauncher.management.nanovg.font.FontManager;
import me.eldodebug.soarlauncher.utils.DialogUtils;

public class NanoVGManager {

	private long nvg;
	
	private FontManager fontManager;
	private AssetManager assetManager;
	
	public NanoVGManager() {
		
		nvg = NanoVGGL2.nvgCreate(NanoVGGL2.NVG_ANTIALIAS);
		
		if(nvg == 0) {
			DialogUtils.error("Error", "Failed to create a window. \n "
					+ "It cannot be executed on your computer.");
			System.exit(0);
		}
		
		fontManager = new FontManager();
		fontManager.init(nvg);
		
		assetManager = new AssetManager();
	}
	
	public void setupAndDraw(LongConsumer consumer) {
		
		SoarLauncher instance = SoarLauncher.getInstance();
		
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		
		NanoVG.nvgBeginFrame(nvg, instance.getWidth(), instance.getHeight(), 1);
		
		consumer.accept(nvg);
		
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        NanoVG.nvgEndFrame(nvg);
        GL11.glPopAttrib();
	}
	
	public void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {
		
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
		
		NVGColor nvgColor = getColor(color);
		
		NanoVG.nvgFillColor(nvg, nvgColor);
		NanoVG.nvgFill(nvg);
	}
	
	public void drawRoundedRectVarying(float x, float y, float width, float height, float topLeftRadius, float topRightRadius, float bottomLeftRadius, float bottomRightRadius, Color color) {
		
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgRoundedRectVarying(nvg, x, y, width, height, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius);
		
		NVGColor nvgColor = getColor(color);
		
		NanoVG.nvgFillColor(nvg, nvgColor);
		NanoVG.nvgFill(nvg);
	}
	
	public void drawText(String text, float x, float y, Color color, float size, Font font) {

		y+=size / 2;
		
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgFontSize(nvg, size);
		NanoVG.nvgFontFace(nvg, font.getName());
		NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_MIDDLE);
		
		NVGColor nvgColor = getColor(color);
		
		NanoVG.nvgFillColor(nvg, nvgColor);
		NanoVG.nvgText(nvg, x, y, text);
	}
	
	public void drawImage(String path, float x, float y, float width, float height) {
		
		if(assetManager.loadImage(nvg, path)) {
			
			NVGPaint imagePaint = NVGPaint.calloc();
			
			int image = assetManager.getImage(path);
			
			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, image, 1, imagePaint);
			
			NanoVG.nvgRect(nvg, x, y, width, height);
			NanoVG.nvgFillPaint(nvg, imagePaint);
			NanoVG.nvgFill(nvg);
			
			imagePaint.free();
		}
	}
	
	public void drawRoundedImage(String path, float x, float y, float width, float height, float radius, float alpha) {
		
		if(assetManager.loadImage(nvg, path)) {
			
			NVGPaint imagePaint = NVGPaint.calloc();
			
			int image = assetManager.getImage(path);
			
			NanoVG.nvgBeginPath(nvg);
			NanoVG.nvgImagePattern(nvg, x, y, width, height, 0, image, alpha, imagePaint);
			
			NanoVG.nvgRoundedRect(nvg, x, y, width, height, radius);
			NanoVG.nvgFillPaint(nvg, imagePaint);
			NanoVG.nvgFill(nvg);
			
			imagePaint.free();
		}
	}
	
	public void drawRoundedImage(String path, float x, float y, float width, float height, float radius) {
		drawRoundedImage(path, x, y, width, height, radius, 1.0F);
	}
	
	public void drawArc(float x, float y, float radius, float startAngle, float endAngle, float strokeWidth, Color color) {
		
		NVGColor nvgColor = getColor(color);
		
		NanoVG.nvgBeginPath(nvg);
		NanoVG.nvgArc(nvg, x, y, radius, (float) Math.toRadians(startAngle), (float) Math.toRadians(endAngle), NanoVG.NVG_CW);
		NanoVG.nvgStrokeWidth(nvg, strokeWidth);
		NanoVG.nvgStrokeColor(nvg, nvgColor);
		NanoVG.nvgStroke(nvg);
	}
	
	public void drawCenteredText(String text, float x, float y, Color color, float size, Font font) {
		
		int textWidth = (int) getTextWidth(text, size, font);
		
		drawText(text, x - (textWidth >> 1), y, color, size, font);
	}
	
	public float getTextWidth(String text, float size, Font font) {
		
	    float[] bounds = new float[4];
	    
	    NanoVG.nvgFontSize(nvg, size);
	    NanoVG.nvgFontFace(nvg, font.getName());
	    NanoVG.nvgTextBounds(nvg, 0, 0, text, bounds);
	    NanoVG.nvgTextAlign(nvg, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_MIDDLE);
	    
	    return bounds[2] - bounds[0];
	}
	
	public float getTextHeight(String text, float size, Font font) {
		
	    float[] bounds = new float[4];
	    
	    NanoVG.nvgFontSize(nvg, size);
	    NanoVG.nvgFontFace(nvg, font.getName());
	    NanoVG.nvgTextBounds(nvg, 0, 0, text, bounds);

	    return bounds[3] - bounds[1];
	}
	
	public void save() {
		NanoVG.nvgSave(nvg);
	}
	
	public void restore() {
		NanoVG.nvgRestore(nvg);
	}
	
	public void scale(float x, float y, float scale) {
		NanoVG.nvgTranslate(nvg, x, y);
		NanoVG.nvgScale(nvg, scale, scale);
		NanoVG.nvgTranslate(nvg, -x, -y);
	}
	
	public void scale(float x, float y, float width, float height, float scale) {
		NanoVG.nvgTranslate(nvg, (x + (x + width)) / 2, (y + (y + height)) / 2);
		NanoVG.nvgScale(nvg, scale, scale);
		NanoVG.nvgTranslate(nvg, -(x + (x + width)) / 2, -(y + (y + height)) / 2);
	}
	
	public void scissor(float x, float y, float width, float height) {
		NanoVG.nvgScissor(nvg, x, y, width, height);
	}
	
	public void translate(float x, float y) {
		NanoVG.nvgTranslate(nvg, x, y);
	}
	
	public void setAlpha(float alpha) {
		NanoVG.nvgGlobalAlpha(nvg, alpha);
	}
	
	public NVGColor getColor(Color color) {
		
		if(color == null) {
			color = Color.RED;
		}
		
		NVGColor nvgColor = NVGColor.create();
		
		NanoVG.nvgRGBA((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue(), (byte) color.getAlpha(), nvgColor);
		
		return nvgColor;
	}

	public long getContext() {
		return nvg;
	}
}
