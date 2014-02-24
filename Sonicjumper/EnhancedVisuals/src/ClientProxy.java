package Sonicjumper.EnhancedVisuals.src;

import net.minecraftforge.common.MinecraftForge;
import Sonicjumper.EnhancedVisuals.src.event.TickHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends ServerProxy {
	public void registerClientThings(FMLPreInitializationEvent event) {
		TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
		basePath = event.getSourceFile().getPath();
		visualsDirectory = /*event.getSourceFile().getPath() + "/assets/sonicjumper/" + */ConfigCore.currentThemePack + "/visuals/";
		soundsDirectory = /*event.getSourceFile().getPath() + "/assets/sonicjumper/" + */ConfigCore.currentThemePack + "/sounds/";
    	MinecraftForge.EVENT_BUS.register(EnhancedVisuals.instance.splatRenderer);
    	//MinecraftForge.EVENT_BUS.register(awarenessRenderer);
    	MinecraftForge.EVENT_BUS.register(EnhancedVisuals.instance.visualEventHandler);
	}
	
	public static String getVisualsDirectory(String themePack) {
		return /*basePath + "/assets/sonicjumper/" + */themePack + "/visuals/";
	}
}
