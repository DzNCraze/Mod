package mods.Sonicjumper.EnhancedVisuals.src.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import Sonicjumper.EnhancedVisuals.src.EnhancedVisuals;
import Sonicjumper.EnhancedVisuals.src.event.GuiVisualRenderer;
import Sonicjumper.EnhancedVisuals.src.visual.Visual;

public class RenderVisual {
	protected boolean reset = false;
	/** 
	 * The default Visual renderer. Note that this does not take into account many of the variables needed by overlays and such
	 * @param v Object to render
	 * @param partialTicks 
	 */
	public void doRenderVisual(Visual v, float partialTicks) {
		if(v.specialRendering()) {
			reset = true;
		}
		ScaledResolution scaledRes = new ScaledResolution(EnhancedVisuals.mc.gameSettings, EnhancedVisuals.mc.displayWidth, EnhancedVisuals.mc.displayHeight);
		GL11.glColor4f(v.getColor().getRed() / 255.0F, v.getColor().getGreen() / 255.0F, v.getColor().getBlue() / 255.0F, v.getTranslucency());
		EnhancedVisuals.mc.func_110434_K().func_110577_a(v.getResource());
		Tessellator var3 = Tessellator.instance;
        var3.startDrawingQuads();
        var3.addVertexWithUV(0.0D + (v.getXOffset() * scaledRes.getScaledWidth_double()), (double)v.getHeight() + (v.getYOffset() * scaledRes.getScaledHeight_double()), -90.0D, 0.0D, 1.0D);
        var3.addVertexWithUV((double)v.getWidth() + (v.getXOffset() * scaledRes.getScaledWidth_double()), (double)v.getHeight() + (v.getYOffset() * scaledRes.getScaledHeight_double()), -90.0D, 1.0D, 1.0D);
        var3.addVertexWithUV((double)v.getWidth() + (v.getXOffset() * scaledRes.getScaledWidth_double()), 0.0D + (v.getYOffset() * scaledRes.getScaledHeight_double()), -90.0D, 1.0D, 0.0D);
        var3.addVertexWithUV(0.0D + (v.getXOffset() * scaledRes.getScaledWidth_double()), 0.0D + (v.getYOffset() * scaledRes.getScaledHeight_double()), -90.0D, 0.0D, 0.0D);
        var3.draw();
        if(reset) {
        	GuiVisualRenderer.resetBlending();
        }
	}
}
