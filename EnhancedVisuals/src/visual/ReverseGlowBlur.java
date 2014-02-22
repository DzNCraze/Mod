package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;

public class ReverseGlowBlur extends GlowBlur {
	private GlowBlur resultBlur;
	
	public ReverseGlowBlur(GlowBlur clone) {
		this(clone.getType(), clone.getMaxTime(), clone.getColor(), clone.resetsOnTick, clone.getScale(), clone.getRadius(), clone.getGlowAmount(), clone);
	}
	
	public ReverseGlowBlur(VisualType type, int time, Color rgba, boolean resets, float scale, int radius, float amount, GlowBlur result) {
		super(type, time, rgba, resets, scale, radius, amount);
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
