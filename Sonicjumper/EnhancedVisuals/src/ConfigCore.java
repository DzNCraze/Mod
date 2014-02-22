package Sonicjumper.EnhancedVisuals.src;

import java.io.File;

import net.minecraftforge.common.Configuration;

public class ConfigCore {
	private Configuration config;
	
	public static String currentThemePack;
	public static String backupThemePack;
	public static String defaultThemePack = "defaulttheme";
	public static boolean shouldRenderBlur, useTrueGlow, grayscaleLowHealth;
	public static float blurQuality;
	
	public ConfigCore(File configFile) {
		config = new Configuration(configFile);
	}
	
	public void loadConfig() {
		config.load();
		
		currentThemePack = config.get(Configuration.CATEGORY_GENERAL, "Current Theme Pack", defaultThemePack, "Change this to the file name of any theme pack you have installed").getString();
		backupThemePack = config.get(Configuration.CATEGORY_GENERAL, "Backup Theme Pack", defaultThemePack, "This 'fills in' the gaps left by the current theme pack").getString();
		
		shouldRenderBlur = config.get(Configuration.CATEGORY_GENERAL, "Render Blur Flag", false, "Enable this to have blur filters render").getBoolean(false);
		useTrueGlow = config.get(Configuration.CATEGORY_GENERAL, "True Glow Flag", false, "Enable this to have better glows").getBoolean(false);
		grayscaleLowHealth = config.get(Configuration.CATEGORY_GENERAL, "Grayscale Low Health Flag", false, "Disable this to have classic low health warnings, enable to have grayscale(NOTE: If this is true, blurs must also be enabled for the grayscale to render)").getBoolean(false);
		
		blurQuality = (float) config.get(Configuration.CATEGORY_GENERAL, "Blur Quality", 0.25F, "Increase only if you think your computer can handle it. Decrease if you want to keep blurs, but are having lag.").getDouble(0.25F);
		
		config.save();
	}
}
