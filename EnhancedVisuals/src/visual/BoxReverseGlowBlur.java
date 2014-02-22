package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;

public class BoxReverseGlowBlur extends BoxGlowBlur {
	private BoxGlowBlur resultBlur;
	
	public BoxReverseGlowBlur(BoxGlowBlur clone) {
		this(clone.getType(), clone.getMaxTime(), clone.getColor(), clone.resetsOnTick, clone.getScale(), clone.getRadius(), clone.getIterations(), clone.getGlowAmount(), clone);
	}
	
	public BoxReverseGlowBlur(VisualType type, int time, Color rgba, boolean resets, float scale, int radius, int iterations, float amount, BoxGlowBlur result) {
		super(type, time, rgba, resets, scale, radius, iterations, amount);
		resultBlur = result;
	}
	
	@Override
	public void beingRemoved() {
		VisualManager.addVisual(resultBlur);
	}
	
	@Override
	public float getTranslucency() {
		return (1 - super.getTranslucency());
	}
}
