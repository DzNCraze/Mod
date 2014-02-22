package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;

public class ReverseOverlay extends Overlay {
	private Overlay resultOverlay;
	
	public ReverseOverlay(Overlay clone) {
		this(clone.getType(), clone.getMaxTime(), clone.getColor(), clone);
	}
	
	public ReverseOverlay(VisualType type, int time, Color rgba, Overlay result) {
		super(type, time, rgba);
		resultOverlay = result;
	}
	
	@Override
	public void beingRemoved() {
		VisualManager.addVisual(resultOverlay);
	}
	
	@Override
	public float getTranslucency() {
		return (1 - super.getTranslucency());
	}
}
