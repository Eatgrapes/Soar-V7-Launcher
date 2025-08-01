package me.eldodebug.soarlauncher.gui;

import java.awt.Color;

import me.eldodebug.soarlauncher.SoarLauncher;
import me.eldodebug.soarlauncher.management.language.TranslateText;
import me.eldodebug.soarlauncher.management.nanovg.NanoVGManager;
import me.eldodebug.soarlauncher.management.nanovg.font.Fonts;
import me.eldodebug.soarlauncher.utils.TimerUtils;

public class GuiLoading extends GuiScreen {

	private TimerUtils timer = new TimerUtils();
	
	private String text, dot;
	private boolean start;
	private int amount;
	
	public GuiLoading() {
		text = TranslateText.CHECKING_FOR_UPDATE.getText();
		dot = ".";
		start = false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY) {
		
		SoarLauncher instance = SoarLauncher.getInstance();
		NanoVGManager nvg = instance.getNanoVGManager();
		
		int width = instance.getWidth();
		int height = instance.getHeight();
		
		amount+=6;
		
		nvg.drawArc((width / 2), (height / 2) - 22, 28, amount, amount + 80, 3.5F, Color.WHITE);
		nvg.drawCenteredText(text, width / 2, (height / 2) + 24, Color.WHITE, 18, Fonts.REGULAR);
		
		if(instance.getUpdateManager().isDone()) {
			if(instance.getUpdateManager().isNeedUpdate()) {
				
				text = TranslateText.UPDATING.getText() + dot;
				
				if(!start) {
					start = true;
					instance.getUpdateManager().updateLauncher();
				}
				
				if(timer.delay(600)) {
					
					dot = dot + ".";
					
					if(dot.equals("....")) {
						dot = ".";
					}
					
					timer.reset();
				}
			} else {
				instance.setScreen(new GuiMainMenu(false));
			}
		}
	}
}
