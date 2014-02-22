package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;
import java.awt.image.BufferedImage;

import Sonicjumper.EnhancedVisuals.src.EnhancedVisuals;
import Sonicjumper.EnhancedVisuals.src.filters.AbstractBufferedImageOp;
import Sonicjumper.EnhancedVisuals.src.filters.SwimFilter;
import Sonicjumper.EnhancedVisuals.src.render.BlurHelper;

public class SwimBlur extends Blur {
	private float swimAmount, swimAngle, swimStretch, swimTurbulence, swimTime;
	
	public SwimBlur(VisualType type, int time, Color rgba, boolean resets, float scale, float amount, float angle, float stretch, float turbulence) {
		super(type, time, rgba, resets, scale);
		swimAmount = amount;
		swimAngle = angle;
		swimStretch = stretch;
		swimTurbulence = turbulence;
	}
	
	public void resetBufferedImage(float translucency) {
		if(resetsOnTick || image == null) {
			BufferedImage screen = BlurHelper.captureScreenAsImage(EnhancedVisuals.mc.displayWidth, EnhancedVisuals.mc.displayHeight);
			if(filter != null) {
				SwimFilter sf = (SwimFilter) filter;
				swimTime += 5.0F;
				sf.setTime((swimTime % 100.0F) / 100.0F);
				BufferedImage scaledImage = BlurHelper.scaleImage(screen, blurScaling);
				image = prepareImage(filter.filter(scaledImage, null));
			} else {
				image = prepareImage(screen);
			}
		}
	}
	
	@Override
	public AbstractBufferedImageOp getFilter() {
		SwimFilter sf = new SwimFilter();
		sf.setAmount(swimAmount);
		sf.setAngle(swimAngle);
		sf.setStretch(swimStretch);
		sf.setTurbulence(swimTurbulence);
		return new SwimFilter();
	}
}
