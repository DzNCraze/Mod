package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;

import Sonicjumper.EnhancedVisuals.src.render.RenderOverlay;
import Sonicjumper.EnhancedVisuals.src.render.RenderVisual;

public class Overlay extends Visual {
	public Overlay(VisualType type, int time, Color rgba) {
		super(type, time, rgba);
	}
	
	protected RenderVisual getRenderer() {
		return new RenderOverlay();
	}
	
	public boolean specialRendering() {
		if(getType().equals(VisualType.lowhealth)) {
			
			//GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_DST_ALPHA);
			//GL11.glBlendFunc(GL11.GL_DST_ALPHA, GL11.GL_DST_ALPHA);
			//GL11.glColorMask(false, false, false, false);
			return true;
		}
		return false;
	}
}
