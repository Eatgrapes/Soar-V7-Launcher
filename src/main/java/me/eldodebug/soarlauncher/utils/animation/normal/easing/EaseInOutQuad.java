package me.eldodebug.soarlauncher.utils.animation.normal.easing;

import me.eldodebug.soarlauncher.utils.animation.normal.Animation;

public class EaseInOutQuad extends Animation {

	public EaseInOutQuad(int ms, double endPoint) {
		super(ms, endPoint);
		this.reset();
	}

	@Override
	protected double getEquation(double x) {
		
	    double x1 = x / (duration / 2);
	    
	    if (x1 < 1) {
	    	return 0.5 * x1 * x1;
	    }
	    
	    x1--;
	    
	    return -0.5 * (x1 * (x1 - 2) - 1);
	}
}
