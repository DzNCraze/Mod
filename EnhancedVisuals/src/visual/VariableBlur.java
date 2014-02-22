package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;
import java.awt.image.BufferedImage;

import Sonicjumper.EnhancedVisuals.src.filters.AbstractBufferedImageOp;
import Sonicjumper.EnhancedVisuals.src.filters.VariableBlurFilter;

public class VariableBlur extends Blur {
	private BufferedImage blurMask;
	protected int blurRadius, blurIterations;
	
	public VariableBlur(VisualType type, int time, Color rgba, boolean resets, float scale, int radius, int iterations, BufferedImage mask) {
		super(type, time, rgba, resets, scale);
		blurMask = mask;
		blurRadius = radius;
		blurIterations = iterations;
	}
	
	public BufferedImage getBlurMask() {
		return blurMask;
	}
	
	@Override
	public AbstractBufferedImageOp getFilter() {
		VariableBlurFilter vbf = new VariableBlurFilter();
		vbf.setRadius(getRadius());
		vbf.setIterations(getIterations());
		vbf.setBlurMask(getBlurMask());
		return vbf;
	}

	private int getRadius() {
		return blurRadius;
	}

	private int getIterations() {
		return blurIterations;
	}
}
