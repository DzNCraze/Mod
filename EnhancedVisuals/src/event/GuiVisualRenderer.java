package Sonicjumper.EnhancedVisuals.src.event;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import Sonicjumper.EnhancedVisuals.src.ConfigCore;
import Sonicjumper.EnhancedVisuals.src.visual.Blur;
import Sonicjumper.EnhancedVisuals.src.visual.Visual;
import Sonicjumper.EnhancedVisuals.src.visual.VisualManager;

public class GuiVisualRenderer {
	private Minecraft mc;

	public GuiVisualRenderer(Minecraft minecraftInstance) {
		super();

		mc = minecraftInstance;
	}

	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void onRenderActiveVisuals(RenderGameOverlayEvent event) {
		if(event.isCancelable() || !(event.type == ElementType.EXPERIENCE || event.type == ElementType.JUMPBAR))
		{
			return;
		}
		ArrayList<Visual> visualList = VisualManager.getVisuals();
		
		if(visualList.size() > 0)
		{
			resetBlending();
			ArrayList<Blur> activeBlurs = new ArrayList<Blur>();
			for(int i = 0; i < visualList.size(); i++) {
				Visual v = visualList.get(i);
				if(v instanceof Blur) {
					// Instead of blurring on first render pass, add the blur to a list to be processed later
					activeBlurs.add((Blur) v);
				} else {
					v.renderUpdate(event.partialTicks);
				}
			}
			if(ConfigCore.shouldRenderBlur) {
				for(int i = 0; i < activeBlurs.size(); i++) {
					Blur b = activeBlurs.get(i);
					b.renderUpdate(event.partialTicks);
				}
			}
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
	
	public static void resetBlending() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
}
