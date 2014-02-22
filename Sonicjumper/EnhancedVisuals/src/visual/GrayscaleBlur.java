package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;
import java.awt.image.BufferedImage;

import Sonicjumper.EnhancedVisuals.src.EnhancedVisuals;
import Sonicjumper.EnhancedVisuals.src.filters.AbstractBufferedImageOp;
import Sonicjumper.EnhancedVisuals.src.filters.GrayscaleFilter;
import Sonicjumper.EnhancedVisuals.src.render.BlurHelper;

public class GrayscaleBlur extends Blur {
	public GrayscaleBlur(VisualType type, int time, Color rgba, boolean resets, float scale) {
		super(type, time, rgba, resets, scale);
	}
	
	public void resetBufferedImage(float translucency) {
		if(resetsOnTick || image == null) {
			BufferedImage screen = BlurHelper.captureScreenAsImage(EnhancedVisuals.mc.displayWidth, EnhancedVisuals.mc.displayHeight);
			BufferedImage scaledImage = BlurHelper.scaleImage(screen, blurScaling);
			image = prepareImage(filter.filter(scaledImage, null));
		}
	}
	
	@Override
	public AbstractBufferedImageOp getFilter() {
		return new GrayscaleFilter();
	}
}
