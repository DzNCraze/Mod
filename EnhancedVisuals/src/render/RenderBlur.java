package Sonicjumper.EnhancedVisuals.src.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import Sonicjumper.EnhancedVisuals.src.EnhancedVisuals;
import Sonicjumper.EnhancedVisuals.src.event.GuiVisualRenderer;
import Sonicjumper.EnhancedVisuals.src.visual.Blur;
import Sonicjumper.EnhancedVisuals.src.visual.Visual;

public class RenderBlur extends RenderVisual {
    public static Minecraft mc = Minecraft.getMinecraft();
	
	public void doRenderVisual(Visual v, float partialTicks) {
		if(v.specialRendering()) {
			reset = true;
		}
		Blur b = (Blur) v;
		EnhancedVisuals.mc.func_110434_K().func_110577_a(v.getResource());
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, b.getImageWidth(), b.getImageHeight(), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, b.getBufferedImage());
		ScaledResolution scaledRes = new ScaledResolution(EnhancedVisuals.mc.gameSettings, EnhancedVisuals.mc.displayWidth, EnhancedVisuals.mc.displayHeight);
		GL11.glColor4f(v.getColor().getRed() / 255.0F, v.getColor().getGreen() / 255.0F, v.getColor().getBlue() / 255.0F, v.getTranslucency());
		Tessellator var3 = Tessellator.instance;
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
