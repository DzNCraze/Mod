package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;

import Sonicjumper.EnhancedVisuals.src.render.RenderSplat;
import Sonicjumper.EnhancedVisuals.src.render.RenderVisual;


public class Splat extends Visual {
	public Splat(VisualType type, int time, Color rgba) {
		super(type, time, rgba);
	}
	
	protected RenderVisual getRenderer() {
		return new RenderSplat();
	}
	
	public double getHeight() {
		return 64.0D;
	}
	
	public double getWidth() {
		return 64.0D;
	}
}
