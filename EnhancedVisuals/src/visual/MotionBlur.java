package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;
import java.awt.image.BufferedImage;

import net.minecraft.client.Minecraft;
import Sonicjumper.EnhancedVisuals.src.filters.AbstractBufferedImageOp;
import Sonicjumper.EnhancedVisuals.src.filters.MotionBlurOp;
import Sonicjumper.EnhancedVisuals.src.render.BlurHelper;

public class MotionBlur extends Blur {
	private float motionZoom;
	
	public MotionBlur(VisualType type, int time, Color rgba, boolean resets, float scale, float zoom) {
		super(type, time, rgba, resets, scale);
		motionZoom = zoom;
	}
	
	@Override
	public void resetBufferedImage(float translucency) {
		if(resetsOnTick || image == null) {
			BufferedImage screen = BlurHelper.captureScreenAsImage();
			BufferedImage scaledImage = BlurHelper.scaleImage(screen, blurScaling);
			((MotionBlurOp) filter).setZoom(motionZoom * translucency);
			long time = Minecraft.getSystemTime();
			BufferedImage filtered = filter.filter(scaledImage, null);
			System.out.println("Filter Time: " + (Minecraft.getSystemTime() - time));
			image = prepareImage(filtered);
		}
	}
	
	@Override
	public AbstractBufferedImageOp getFilter() {
		MotionBlurOp mbf = new MotionBlurOp(0.0F, 0.0F, 0.0F, motionZoom);
		return mbf;
	}
}
