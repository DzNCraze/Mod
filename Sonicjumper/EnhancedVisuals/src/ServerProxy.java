package Sonicjumper.EnhancedVisuals.src;

import java.io.File;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.minecraft.client.Minecraft;

public class ServerProxy {
	/** Directory to where all the visuals are stored */
	public static String basePath;
	public static String visualsDirectory;
	public static String soundsDirectory;
	//public static final String visualsRenderDirectory = "/bin/mods/Sonicjumper/EnhancedVisuals/visuals/";
	
	// Client code only
	public void registerClientThings(FMLPreInitializationEvent event) {
		
	}
}
