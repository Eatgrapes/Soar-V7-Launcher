package me.eldodebug.soarlauncher.utils.animation.normal.easing;

import me.eldodebug.soarlauncher.utils.animation.normal.Animation;

public class EaseInQuart extends Animation {

	public EaseInQuart(int ms, double endPoint) {
		super(ms, endPoint);
		this.reset();
	}

	@Override
	protected double getEquation(double x) {
	    double x1 = x / duration;
	    return x1 * x1 * x1 * x1;
	}
}
