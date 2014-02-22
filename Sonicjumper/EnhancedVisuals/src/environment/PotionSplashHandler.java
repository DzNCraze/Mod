package Sonicjumper.EnhancedVisuals.src.environment;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.AxisAlignedBB;
import Sonicjumper.EnhancedVisuals.src.event.VisualEventHandler;
import Sonicjumper.EnhancedVisuals.src.utils.MathUtils;
import Sonicjumper.EnhancedVisuals.src.visual.VisualManager;
import Sonicjumper.EnhancedVisuals.src.visual.VisualType;

public class PotionSplashHandler extends BaseEnvironmentEffect {

	public PotionSplashHandler(VisualEventHandler veh) {
		super(veh);
	}

	@Override
	public void onTick() {
		AxisAlignedBB var1 = AxisAlignedBB.getBoundingBox(Math.floor(parent.mc.thePlayer.posX) - 4.5D, Math.floor(parent.mc.thePlayer.posY) - 5.0D, Math.floor(parent.mc.thePlayer.posZ) - 4.5D, Math.floor(parent.mc.thePlayer.posX) + 4.5D, Math.floor(parent.mc.thePlayer.posY) + 2.0D, Math.floor(parent.mc.thePlayer.posZ) + 4.5D);

		for(EntityPotion ep : (ArrayList<EntityPotion>) parent.mc.theWorld.getEntitiesWithinAABB(EntityPotion.class, var1)) {
			if(ep.isDead) {
				double modifier = 1/Math.sqrt(MathUtils.sq(Math.floor(parent.mc.thePlayer.posX) - ep.posX) + MathUtils.sq(Math.floor(parent.mc.thePlayer.posY) - ep.posY) + MathUtils.sq(Math.floor(parent.mc.thePlayer.posZ) - ep.posZ));
				int var11 = PotionHelper.func_77915_a(ep.getPotionDamage(), false);
				float r = (float)(var11 >> 16 & 255) / 255.0F;
				float g = (float)(var11 >> 8 & 255) / 255.0F;
				float b = (float)(var11 & 255) / 255.0F;
				float f1 = (float) (modifier * 2.0F);
				VisualManager.addVisualsWithColor(VisualType.potion, 1, 30, 60, new Color(r, g, b, f1 <= 1.0F ? f1 : 1.0F));
			}
		}
	}
	
	@Override
	public void resetEffect() {
		
	}
}
