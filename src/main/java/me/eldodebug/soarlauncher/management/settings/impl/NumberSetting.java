package me.eldodebug.soarlauncher.management.settings.impl;

import me.eldodebug.soarlauncher.management.language.TranslateText;

public class NumberSetting extends Setting {

	private double minValue, value, maxValue;
	private boolean integer;
	
	public NumberSetting(TranslateText nameTranslate, double defaultValue, double minValue, double maxValue, boolean integer) {
		super(nameTranslate);
		
		this.value = defaultValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.integer = integer;
	}

	public double getValue() {
		
		if(integer) {
			this.value = (int) value;
		}
		
		return value;
	}
	
	public int getValueInt() {
		
		if(integer) {
			this.value = (int) value;
		}
		
		return (int) value;
	}
	
	public float getValueFloat() {
		
		if(integer) {
			this.value = (int) value;
		}
		
		return (float) value;
	}
	
	public long getValueLong() {
		
		if(integer) {
			this.value = (int) value;
		}
		
		return (long) value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getMinValue() {
		return minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}
}
