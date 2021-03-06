package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;
import java.awt.image.BufferedImage;

import net.minecraft.client.Minecraft;
import Sonicjumper.EnhancedVisuals.src.EnhancedVisuals;
import Sonicjumper.EnhancedVisuals.src.filters.AbstractBufferedImageOp;
import Sonicjumper.EnhancedVisuals.src.filters.BoxGlowFilter;
import Sonicjumper.EnhancedVisuals.src.render.BlurHelper;

public class BoxGlowBlur extends BoxBlur {
	private float glowAmount;
	
	public BoxGlowBlur(VisualType type, int time, Color rgba, boolean resets, float scale, int radius, int iterations, float amount) {
		super(type, time, rgba, resets, scale, radius, iterations);
		glowAmount = amount;
	}
	
	public void resetBufferedImage(float translucency) {
		if(resetsOnTick || image == null) {
			BufferedImage screen = BlurHelper.captureScreenAsImage(EnhancedVisuals.mc.displayWidth, EnhancedVisuals.mc.displayHeight);
			BufferedImage scaledImage = BlurHelper.scaleImage(screen, blurScaling);
			((BoxGlowFilter) filter).setRadius((int) (blurRadius * translucency));
			((BoxGlowFilter) filter).setAmount(glowAmount * translucency);
			//((BoxGlowFilter) filter).setIterations(getIterations());
			long time = Minecraft.getSystemTime();
			BufferedImage filtered = filter.filter(scaledImage, null);
			image = prepareImage(filtered);
		}
	}
	
	@Override
	public AbstractBufferedImageOp getFilter() {
		BoxGlowFilter gf = new BoxGlowFilter();
		gf.setRadius(getRadius());
		gf.setAmount(getGlowAmount());
		gf.setIterations(getIterations());
		return gf;
	}
	
	public float getGlowAmount() {
		return glowAmount;
	}
}
