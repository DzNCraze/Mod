package Sonicjumper.EnhancedVisuals.src.environment;

import Sonicjumper.EnhancedVisuals.src.event.VisualEventHandler;

public abstract class BaseEnvironmentEffect {
	protected VisualEventHandler parent;
	
	public BaseEnvironmentEffect(VisualEventHandler veh) {
		parent = veh;
	}

	public abstract void onTick();

	public abstract void resetEffect();
}
