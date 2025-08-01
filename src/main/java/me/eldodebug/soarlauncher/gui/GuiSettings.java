package me.eldodebug.soarlauncher.gui;

import java.awt.Color;
import java.io.File;

import me.eldodebug.soarlauncher.SoarLauncher;
import me.eldodebug.soarlauncher.logger.SoarLogger;
import me.eldodebug.soarlauncher.management.language.LanguageManager;
import me.eldodebug.soarlauncher.management.language.TranslateText;
import me.eldodebug.soarlauncher.management.nanovg.NanoVGManager;
import me.eldodebug.soarlauncher.management.nanovg.font.Fonts;
import me.eldodebug.soarlauncher.management.nanovg.font.Icon;
import me.eldodebug.soarlauncher.ui.CompSlider;
import me.eldodebug.soarlauncher.utils.FileUtils;
import me.eldodebug.soarlauncher.utils.MouseUtils;
import me.eldodebug.soarlauncher.utils.animation.normal.Animation;
import me.eldodebug.soarlauncher.utils.animation.normal.easing.EaseBackIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiSettings extends GuiScreen {

	private static final Logger log = LogManager.getLogger(GuiSettings.class);
	private CompSlider memorySlider;
	private Animation introAnimation;
	
	@Override
	public void initGui() {
		introAnimation = new EaseBackIn(950, 1.0F, 0.0F);
		memorySlider = new CompSlider(SoarLauncher.getInstance().getSettingsManager().getMemorySetting());
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY) {
		
		SoarLauncher instance = SoarLauncher.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		LanguageManager languageManager = instance.getLanguageManager();
		
		Color fontColor = new Color(255, 255, 255, (int) (introAnimation.getValue() * 255));
		Color backgroundColor = new Color(230, 230, 230, (int) (introAnimation.getValue() * 120));
		
		int offsetY = 0;
		
		nvg.drawRoundedRect(13, instance.getHeight() - 71, 58, 58, 8, new Color(230, 230, 230, 120));
		nvg.drawCenteredText(Icon.ARROW_LEFT, 12 + (58 / 2), instance.getHeight() - 58, Color.WHITE, 32, Fonts.ICON);
	
		nvg.save();
		nvg.translate(0, 60 - (introAnimation.getValueFloat() * 60));
		
		nvg.drawCenteredText(TranslateText.SETTINGS.getText(), instance.getWidth() / 2, 56 + offsetY, fontColor, 32, Fonts.MEDIUM);
		nvg.drawRoundedRect(13, 100 + offsetY, instance.getWidth() - 26, 50, 8, backgroundColor);
		nvg.drawText(TranslateText.MEMORY.getText(), 26, 116 + offsetY, fontColor, 22, Fonts.MEDIUM);
		nvg.drawText(memorySlider.getSetting().getValueInt() + "GB", 190 - nvg.getTextWidth(memorySlider.getSetting().getValueInt() + "GB", 15, Fonts.REGULAR), 120 + offsetY, fontColor, 15.5F, Fonts.REGULAR);
		
		memorySlider.setX(196);
		memorySlider.setY(124 + offsetY);
		memorySlider.setAlphaProgress(introAnimation.getValueFloat());
		memorySlider.draw(mouseX, mouseY);
		
		offsetY+=64;

		nvg.drawRoundedRect(13, 100 + offsetY, instance.getWidth() - 26, 50, 8, backgroundColor);
		nvg.drawText(TranslateText.LOCATION.getText(), 26, 116 + offsetY, fontColor, 22, Fonts.MEDIUM);
		nvg.drawText(Icon.FOLDER, instance.getWidth() - 26 - 24, 116 + offsetY, fontColor, 22, Fonts.ICON);
		
		offsetY+=64;
		
		nvg.drawRoundedRect(13, 100 + offsetY, instance.getWidth() - 26, 50, 8, backgroundColor);
		nvg.drawText(TranslateText.LANGUAGE.getText(), 26, 116 + offsetY, fontColor, 22, Fonts.MEDIUM);
		nvg.drawText(languageManager.getCurrentLanguage().getName(), instance.getWidth() - nvg.getTextWidth(languageManager.getCurrentLanguage().getName(), 18, Fonts.REGULAR) - 24, 118 + offsetY, fontColor, 18, Fonts.REGULAR);

		offsetY+=64;

		nvg.drawRoundedRect(13, 100 + offsetY, instance.getWidth() - 26, 50, 8, backgroundColor);
		nvg.drawText("Launcher Version", 26, 116 + offsetY, fontColor, 22, Fonts.MEDIUM);
		nvg.drawText(instance.getVersion(), instance.getWidth() - nvg.getTextWidth(instance.getVersion(), 18, Fonts.REGULAR) - 24, 118 + offsetY, fontColor, 18, Fonts.REGULAR);

		nvg.restore();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		SoarLauncher instance = SoarLauncher.getInstance();
		LanguageManager languageManager = instance.getLanguageManager();
		
		int offsetY = 0;
		
		if(mouseButton == 0) {
			
			if(MouseUtils.isInside(mouseX, mouseY, 13, instance.getHeight() - 71, 58, 58)) {
				instance.setScreen(new GuiMainMenu(true));
			}
			
			offsetY+=64;
			
			if(MouseUtils.isInside(mouseX, mouseY, instance.getWidth() - 26 - 28, 112 + offsetY, 30, 30)) {
				
				File file = FileUtils.selectDirectory();
				
				if(file != null) {
					instance.getSettingsManager().getLocationSetting().setFile(file);
				}
			}
			
			offsetY+=64;
			
			if(MouseUtils.isInside(mouseX, mouseY, 13, 100 + offsetY, instance.getWidth() - 26, 50)) {
				languageManager.switchLanguage();
			}
			
			memorySlider.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		memorySlider.mouseReleased(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void onGuiClosed() {
		SoarLauncher.getInstance().getConfigManager().save();
	}
}
	
