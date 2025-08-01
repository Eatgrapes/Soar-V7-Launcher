package me.eldodebug.soarlauncher.ui;

import java.awt.Color;

import me.eldodebug.soarlauncher.SoarLauncher;
import me.eldodebug.soarlauncher.management.nanovg.NanoVGManager;
import me.eldodebug.soarlauncher.management.settings.impl.NumberSetting;
import me.eldodebug.soarlauncher.utils.MathUtils;
import me.eldodebug.soarlauncher.utils.MouseUtils;
import me.eldodebug.soarlauncher.utils.animation.simple.SimpleAnimation;

public class CompSlider extends Comp {

	private SimpleAnimation animation = new SimpleAnimation();
	
	private NumberSetting setting;
	private double baseWidth, valueWidth;
	private boolean dragging;
	private float alphaProgress;
	
	public CompSlider(NumberSetting setting) {
		this.setting = setting;
		this.alphaProgress = 0F;
	}
	
	@Override
	public void draw(int mouseX, int mouseY) {
		
		SoarLauncher instance = SoarLauncher.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		
		double maxValue = setting.getMaxValue();
		double minValue = setting.getMinValue();
		double value = setting.getValue();
		double width = 108;
		
		baseWidth = width * (maxValue - minValue) / (maxValue - minValue);
		valueWidth = width * (value - minValue) / (maxValue - minValue);
		
		double diff = Math.min(width, Math.max(0, mouseX - (this.getX() + 2)));
		
		if(dragging) {
			if(diff == 0) {
				setting.setValue(minValue);
			}else {
				setting.setValue(MathUtils.roundToPlace(((diff / width) * (maxValue - minValue) + minValue), 2));
			}
		}
		
		animation.setAnimation((float) valueWidth, 10);
		
		nvg.drawRoundedRect(this.getX(), this.getY(), (float) baseWidth, (float) 5, 2F, new Color(230, 230, 230, (int) (120 * alphaProgress)));
		nvg.drawRoundedRect(this.getX(), this.getY(), animation.getValue(), (float) 5, 2F, new Color(255, 255, 255, (int) (255 * alphaProgress)));
		nvg.drawRoundedRect(this.getX() + animation.getValue() - 3, this.getY() - 2.5F, 10, 10, 5, new Color(255, 255, 255, (int) (255 * alphaProgress)));
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		if(MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY() - 2, (float) baseWidth, (float) 8) && mouseButton == 0) {
			dragging = true;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		dragging = false;
	}

	public NumberSetting getSetting() {
		return setting;
	}

	public void setAlphaProgress(float alphaProgress) {
		this.alphaProgress = alphaProgress;
	}
}
