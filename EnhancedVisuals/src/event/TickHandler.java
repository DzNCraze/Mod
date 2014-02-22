package Sonicjumper.EnhancedVisuals.src.event;

import java.util.ArrayList;
import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

import org.lwjgl.input.Keyboard;

import Sonicjumper.EnhancedVisuals.src.EnhancedVisuals;
import Sonicjumper.EnhancedVisuals.src.visual.Visual;
import Sonicjumper.EnhancedVisuals.src.visual.VisualManager;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements ITickHandler {
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        if (type.equals(EnumSet.of(TickType.CLIENT)))
        {
            GuiScreen guiscreen = Minecraft.getMinecraft().currentScreen;
            if (guiscreen != null)
            {
                onTickInGUI(guiscreen);
            } else {
                onTickInGame();
            }
        }
        if (type.equals(EnumSet.of(TickType.RENDER)))
        {
        	// Shouldn't happen
        	// onRenderTickInGame();
        }
    }

	private void onTickInGUI(GuiScreen guiscreen) {
		if(guiscreen instanceof GuiChat || guiscreen instanceof GuiContainer) {
			onTickInGame();
		} else if(guiscreen instanceof GuiIngameMenu) {
			if(!Minecraft.getMinecraft().isSingleplayer()) {
				onTickInGame();
			}
		} else {
			updateTickVisuals();
		}
	}

	private void onTickInGame() {
		updateTickVisuals();
		EnhancedVisuals.instance.visualEventHandler.onTick();
		if(Keyboard.isKeyDown(Keyboard.KEY_G)) {
			//Visual bb = new GlowBlur(VisualType.blur, 250, new Color(1.0F, 1.0F, 1.0F, 1.0F), 20, 1, true, ConfigCore.blurQuality, 1.0F);
			//VisualManager.addVisual(bb);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_H)) {
			VisualManager.clearVisuals();
		}
	}
	
	private void updateTickVisuals() {
		ArrayList<Visual> vList = VisualManager.getVisuals();
		for(int i = 0; i < vList.size(); i++) {
			Visual v = vList.get(i);
			v.tickUpdate();
			if(v.isDead) {
				VisualManager.removeVisual(v);
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "SonicVisuals";
	}
}
