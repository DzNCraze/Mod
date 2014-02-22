package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;

import Sonicjumper.EnhancedVisuals.src.render.RenderAnimation;
import Sonicjumper.EnhancedVisuals.src.render.RenderVisual;

public class Animation extends Visual {
	public Animation(VisualType type, int time, Color rgba) {
		super(type, time, rgba);
	}
	
	protected RenderVisual getRenderer() {
		return new RenderAnimation();
	}
}
