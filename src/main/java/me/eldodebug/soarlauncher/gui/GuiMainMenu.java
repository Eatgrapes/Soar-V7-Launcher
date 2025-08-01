package me.eldodebug.soarlauncher.gui;

import java.awt.Color;

import me.eldodebug.soarlauncher.SoarLauncher;
import me.eldodebug.soarlauncher.management.language.TranslateText;
import me.eldodebug.soarlauncher.management.launch.LaunchManager;
import me.eldodebug.soarlauncher.management.launch.LaunchProgress;
import me.eldodebug.soarlauncher.management.nanovg.NanoVGManager;
import me.eldodebug.soarlauncher.management.nanovg.font.Fonts;
import me.eldodebug.soarlauncher.management.nanovg.font.Icon;
import me.eldodebug.soarlauncher.utils.MouseUtils;
import me.eldodebug.soarlauncher.utils.TimerUtils;
import me.eldodebug.soarlauncher.utils.animation.normal.Animation;
import me.eldodebug.soarlauncher.utils.animation.normal.easing.EaseBackIn;

public class GuiMainMenu extends GuiScreen {

	private TimerUtils timer = new TimerUtils();
	
	private Animation iconAnimation, titleAnimation, descAnimation, buttonAnimation;
	private boolean fromSetting;
	
	public GuiMainMenu(boolean fromSetting) {
		this.fromSetting = fromSetting;
	}
	
	@Override
	public void initGui() {
		iconAnimation = new EaseBackIn(950, 1.0F, 0.0F);
		timer.reset();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY) {
		
		SoarLauncher instance = SoarLauncher.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		LaunchManager launchManager = instance.getLaunchManager();
		
		if(timer.delay(250) && titleAnimation == null) {
			titleAnimation = new EaseBackIn(950, 1.0F, 0.0F); 
		}
		
		if(timer.delay(500) && descAnimation == null) {
			descAnimation = new EaseBackIn(950, 1.0F, 0.0F); 
		}
		
		if(timer.delay(750) && buttonAnimation == null) {
			buttonAnimation = new EaseBackIn(950, 1.0F, 0.0F);
		}
		
		if(fromSetting && buttonAnimation == null) {
			buttonAnimation = new EaseBackIn(950, 1.0F, 0.0F);
			buttonAnimation.setValue(1.0F);
		}
		
		nvg.save();
		nvg.translate(0, 60 - (iconAnimation.getValueFloat() * 60));
		nvg.drawCenteredText(Icon.SOAR, instance.getWidth() / 2, 85, new Color(255, 255, 255, (int) (iconAnimation.getValue() * 255)), 146, Fonts.ICON);
		nvg.restore();
		
		if(titleAnimation != null) {
			nvg.save();
			nvg.translate(0, 60 - (titleAnimation.getValueFloat() * 60));
			nvg.drawCenteredText("Soar Client", (instance.getWidth() / 2) + 6, 248, new Color(255, 255, 255, (int) (titleAnimation.getValue() * 255)), 38, Fonts.MEDIUM);
			nvg.restore();
		}
		
		if(descAnimation != null) {
			nvg.save();
			nvg.translate(0, 60 - (descAnimation.getValueFloat() * 60));
			nvg.drawCenteredText(TranslateText.MAIN_MENU_TITLE.getText(), instance.getWidth() / 2, 286, new Color(255, 255, 255, (int) (descAnimation.getValue() * 255)), 16, Fonts.REGULAR);
			nvg.restore();
		}

		if(buttonAnimation != null || fromSetting) {

			Color fontColor = new Color(255, 255, 255, (int) (buttonAnimation.getValue() * 255));
			Color backgroundColor = new Color(230, 230, 230, (int) (buttonAnimation.getValue() * 120));

			nvg.save();
			nvg.translate(0, 60 - (buttonAnimation.getValueFloat() * 60));
			nvg.drawRoundedRect(13, instance.getHeight() - 71, 238, 58, 8, backgroundColor);
			nvg.drawRoundedRect(instance.getWidth() - 71, instance.getHeight() - 71, 58, 58, 8, backgroundColor);
			nvg.drawCenteredText(Icon.SETTINGS, instance.getWidth() - 70 + (58 / 2), instance.getHeight() - 58, fontColor, 32, Fonts.ICON);

			// Retrieve the current launch progress text
			String progressText = launchManager.getProgress().getProgress();

			// Append the version only if the progress text is "Launch"
			if (progressText.equals(TranslateText.LAUNCH.getText())) {
				progressText += " " + instance.getUpdateManager().getClientVersion();
			}

			nvg.drawCenteredText(progressText, 13 + (238 / 2), instance.getHeight() - 56, fontColor, 32, Fonts.MEDIUM);

			nvg.restore();
		}

	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		SoarLauncher instance = SoarLauncher.getInstance();
		LaunchManager launchManager = instance.getLaunchManager();
		
		if(MouseUtils.isInside(mouseX, mouseY, 13, instance.getHeight() - 71, 238, 58) && mouseButton == 0 && launchManager.getProgress().equals(LaunchProgress.LAUNCH)) {
			launchManager.launch();
		}
		
		if(MouseUtils.isInside(mouseX, mouseY, instance.getWidth() - 71, instance.getHeight() - 71, 58, 58) && mouseButton == 0) {
			instance.setScreen(new GuiSettings());
		}
	}
}
