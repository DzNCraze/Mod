package mods.Sonicjumper.EnhancedVisuals.src.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import Sonicjumper.EnhancedVisuals.src.EnhancedVisuals;
import Sonicjumper.EnhancedVisuals.src.event.GuiVisualRenderer;
import Sonicjumper.EnhancedVisuals.src.visual.Visual;

public class RenderOverlay extends RenderVisual {
	public void doRenderVisual(Visual v, float partialTicks) {
		//System.out.println("Rendering overlay with Alpha = " + v.getTranslucency());
		if(v.specialRendering()) {
			reset = true;
		}
		ScaledResolution scaledRes = new ScaledResolution(EnhancedVisuals.mc.gameSettings, EnhancedVisuals.mc.displayWidth, EnhancedVisuals.mc.displayHeight);
		GL11.glColor4f(v.getColor().getRed() / 255.0F, v.getColor().getGreen() / 255.0F, v.getColor().getBlue() / 255.0F, v.getTranslucency());
		Tessellator var3 = Tessellator.instance;
		EnhancedVisuals.mc.func_110434_K().func_110577_a(v.getResource());
        var3.startDrawingQuads();
        var3.addVertexWithUV(0.0D, scaledRes.getScaledHeight_double(), -90.0D, 0.0D, 1.0D);
        var3.addVertexWithUV(scaledRes.getScaledWidth_double(), scaledRes.getScaledHeight_double(), -90.0D, 1.0D, 1.0D);
        var3.addVertexWithUV(scaledRes.getScaledWidth_double(), 0.0D, -90.0D, 1.0D, 0.0D);
        var3.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        var3.draw();
        if(reset) {
        	GuiVisualRenderer.resetBlending();
        }
	}
}
