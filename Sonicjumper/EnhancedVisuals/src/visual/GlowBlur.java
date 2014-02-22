package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;
import java.awt.image.BufferedImage;

import net.minecraft.client.Minecraft;
import Sonicjumper.EnhancedVisuals.src.EnhancedVisuals;
import Sonicjumper.EnhancedVisuals.src.filters.AbstractBufferedImageOp;
import Sonicjumper.EnhancedVisuals.src.filters.GlowFilter;
import Sonicjumper.EnhancedVisuals.src.render.BlurHelper;

public class GlowBlur extends Blur {
	private float glowAmount;
	protected int blurRadius;
	
	public GlowBlur(VisualType type, int time, Color rgba, boolean resets, float scale, int radius, float amount) {
		super(type, time, rgba, resets, scale);
		glowAmount = amount;
		blurRadius = radius;
	}
	
	public void resetBufferedImage(float translucency) {
		if(resetsOnTick || image == null) {
			BufferedImage screen = BlurHelper.captureScreenAsImage(EnhancedVisuals.mc.displayWidth, EnhancedVisuals.mc.displayHeight);
			BufferedImage scaledImage = BlurHelper.scaleImage(screen, blurScaling);
			((GlowFilter) filter).setRadius((int) (blurRadius * translucency));
			((GlowFilter) filter).setAmount(glowAmount * translucency);
			//((BoxGlowFilter) filter).setIterations(getIterations());
			long time = Minecraft.getSystemTime();
			BufferedImage filtered = filter.filter(scaledImage, null);
			image = prepareImage(filtered);
		}
	}
	
	@Override
	public AbstractBufferedImageOp getFilter() {
		GlowFilter gf = new GlowFilter();
		gf.setRadius(getRadius());
		gf.setAmount(getGlowAmount());
		return gf;
	}
	
	protected int getRadius() {
		return blurRadius;
	}

	public float getGlowAmount() {
		return glowAmount;
	}
}
