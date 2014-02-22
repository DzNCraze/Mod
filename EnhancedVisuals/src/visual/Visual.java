package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;
import java.util.Random;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import Sonicjumper.EnhancedVisuals.src.EnhancedVisuals;
import Sonicjumper.EnhancedVisuals.src.render.RenderVisual;

public class Visual {
	public enum VisualCatagory {
		splat,
		overlay,
		animation,
		shader;
		
		private VisualCatagory() {
		}
	}
	
	public static Random rand = new Random();
	
	private VisualType visualType;
	private ResourceLocation resourceLocation;
	private float xOffset, yOffset;
	private int height, width, timeLeft;
	private boolean hasPhysics;

	protected int maxTime;
	private Color color;
	public boolean isDead;
	protected RenderVisual renderer;
	
	private ScaledResolution scaledRes = new ScaledResolution(EnhancedVisuals.mc.gameSettings, EnhancedVisuals.mc.displayWidth, EnhancedVisuals.mc.displayHeight);
	
	public Visual(VisualType type, int time) {
		this(type, time, new Color(1.0F, 1.0F, 1.0F, 1.0F));
	}
	
	/**
	 * 
	 * @param type Visual render type
	 * @param time Life span(in ticks)
	 * @param rgba Color to mask visual with
	 */
	public Visual(VisualType type, int time, Color rgba) {
		visualType = type;
		if(type.resourceArray.length > 0) {
			resourceLocation = type.resourceArray[rand.nextInt(type.resourceArray.length)];
		}
		timeLeft = time;
		maxTime = time;
		width = type.getSize();
		height = type.getSize();
		if(type.getCatagory().equals(VisualCatagory.splat)) {
			xOffset = calibrateClearViewing((scaledRes.getScaledWidth() - (width/2))) / scaledRes.getScaledWidth();
			yOffset = calibrateClearViewing((scaledRes.getScaledHeight() - (height/2))) / scaledRes.getScaledHeight();
		} else {
			xOffset = 0.0F;
			yOffset = 0.0F;
		}
		//System.out.println(xOffset + " || " + yOffset);
		color = rgba;
		renderer = getRenderer();
		hasPhysics = type.hasPhysics();
	}
	
	public void beingRemoved() {
		
	}

	private float calibrateClearViewing(int par1) {
		int center = par1 / 2;
		double x = Math.sqrt(rand.nextInt(center) + 1);
		double max = Math.sqrt(center);

		return rand.nextInt(2) == 0 ? (float) (center + (center * (x/max))) : (float) (center - (center * (x/max)));
	}

	public void tickUpdate() {
		if(timeLeft == 0) {
			isDead = true;
		} else {
			if(!isDead) {
				timeLeft = timeLeft <= 0 ? timeLeft : timeLeft - 1;
				//System.out.println(timeLeft + " time left for Visual " + this.visualType.toString());
			}
		}
	}

	public void renderUpdate(float partialTicks)
	{
		if(!isDead)
		{
			renderer.doRenderVisual(this, partialTicks);
		}
	}

	/**
	 * Subract a given amount of time from the splats lifespan. Used in "washing" the splats away.
	 */
	public void subTime(int i) {
		timeLeft = timeLeft - i;
		if(timeLeft < 0) {
			timeLeft = 0;
		}
	}
	
	/**
	 * Subract a given percentage(0.0 - 1.0) of time from the splats lifespan. Used in "washing" the splats away.
	 */
	public void subTime(float f) {
		subTime((int)(f * (float)timeLeft));
	}
	
	public boolean specialRendering() {
		return false;
	}

	public void setTranslucency(float intensity) {
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (intensity * 255.0F));
	}

	protected RenderVisual getRenderer() {
		return new RenderVisual();
	}

	public VisualType getType() {
		return visualType;
	}

	public ResourceLocation getResource() {
		return resourceLocation;
	}

	public float getTranslucency() {
		if(timeLeft != -1) {
			Float var1 = new Float(timeLeft);
			Float var2 = new Float(maxTime);
			float var3 = var1.floatValue()/var2.floatValue();
			return (color.getAlpha() / 255.0F) * var3;
		} else {
			return color.getAlpha() / 255.0F;
		}
	}
	
	public String toString() {
		return super.toString() + "[x = " + getXOffset() + ", y = " + getYOffset() + ", translucency = " + getTranslucency()
				+ ", timeLeft = " + timeLeft + ", maxTime = " + maxTime + ", resourceLocation = " + getResource() + "]";
	}

	public double getXOffset() {
		return xOffset;
	}
	
	public double getYOffset() {
		return yOffset;
	}

	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}

	public Color getColor() {
		return color;
	}
	
	public int getMaxTime() {
		return maxTime;
	}
}
