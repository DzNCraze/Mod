package Sonicjumper.EnhancedVisuals.src.environment;

import Sonicjumper.EnhancedVisuals.src.event.VisualEventHandler;
import Sonicjumper.EnhancedVisuals.src.visual.VisualManager;

public class WetnessHandler extends BaseEnvironmentEffect {
	private float wetness;
	
	public WetnessHandler(VisualEventHandler veh) {
		super(veh);
		wetness = 0.5F;
	}
	
	public void onTick() {
		// Alter variables
		if(parent.mc.thePlayer.isInWater()) {
			wetness = (float)((double)this.wetness + (double)(1.0F - this.wetness) * 0.05D);
		} else if(parent.mc.thePlayer.isWet()) {
			wetness = (float)((double)this.wetness + (double)(1.0F - this.wetness) * (parent.mc.thePlayer.isSprinting() ? 0.01D : 0.005D));
		} else {
			wetness = (float)((double)this.wetness + (double)(0.0F - this.wetness) * (parent.mc.thePlayer.isSprinting() ? 0.004D : 0.002D));
		}
		// Adjust rendering of overlays
		if(wetness > 0.5F) {
			float wet = ((wetness - 0.5F) * 2.0F) * 0.75F;
			VisualManager.adjustWetOverlay(wet < 1.0F ? wet : 1.0F);
		}
	}

	@Override
	public void resetEffect() {
		wetness = 0.5F;
	}
	
	public float getWetness() {
		return wetness;
	}
}
