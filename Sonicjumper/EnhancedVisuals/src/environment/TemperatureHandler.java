package Sonicjumper.EnhancedVisuals.src.environment;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import Sonicjumper.EnhancedVisuals.src.event.VisualEventHandler;
import Sonicjumper.EnhancedVisuals.src.utils.EntityUtils;
import Sonicjumper.EnhancedVisuals.src.visual.VisualManager;

public class TemperatureHandler extends BaseEnvironmentEffect {
	private float temperature;
	private ArrayList<TemperatureFactor> factors = new ArrayList<TemperatureFactor>();
	
	public TemperatureHandler(VisualEventHandler veh) {
		super(veh);
		temperature = 1.0F;
		factors.add(new TemperatureFactor() {
			@Override
			public boolean runFactor() {
				float currentWorldTemp = parent.mc.theWorld.getBiomeGenForCoords((int)Math.floor(parent.mc.thePlayer.posX), (int)Math.floor(parent.mc.thePlayer.posZ)).temperature;
				factor = currentWorldTemp + (parent.mc.thePlayer.isBurning() ? 4.0F : 0.0F) - parent.wetnessHandler.getWetness() * (parent.mc.thePlayer.isSprinting() ? 4.0F : 1.0F);
				return true;
			}

			@Override
			public float getFactorRate() {
				return (float) (parent.mc.thePlayer.isSprinting() ? 0.0004D : 0.0001D);
			}
		});
		factors.add(new TemperatureFactor() {
			@Override
			public boolean runFactor() {
				factor = 0.0F;
				int leatherCount = 0;
				for (int j = 0; j < 5; ++j) {
	                ItemStack wornItem = parent.mc.thePlayer.getCurrentItemOrArmor(j);
	                if(wornItem != null && wornItem.getItem() instanceof ItemArmor) {
	                	ItemArmor armor = (ItemArmor) wornItem.getItem();
	                	if(armor.getArmorMaterial().equals(EnumArmorMaterial.CLOTH)) {
	                		leatherCount++;
	                	}
	                	/*if(armor.getArmorMaterial().equals(EnumArmorMaterial.IRON) || armor.getArmorMaterial().equals(EnumArmorMaterial.CHAIN)) {
	                		factor += 0.2F;
	                	}
	                	if(armor.getArmorMaterial().equals(EnumArmorMaterial.DIAMOND)) {
	                		factor -= 0.1F;
	                	}
	                	if(armor.getArmorMaterial().equals(EnumArmorMaterial.GOLD)) {
	                		factor = factor < 1.0F ? factor + 0.25F : factor - 0.25F;
	                	}*/
	                }
	            }
				if(temperature < 0.15F * leatherCount) {
					factor = 0.15F * leatherCount;
				}
				return factor != 0.0F;
			}

			@Override
			public float getFactorRate() {
				return 0.0001F;
			}
			
		});
		factors.add(new TemperatureFactor() {

			@Override
			public boolean runFactor() {
				factor = 1.0F;
				float heat = 0.0F;
				if(EntityUtils.isBlockNearEntity(parent.mc.thePlayer, Block.fire, 8)) {
					double dist = EntityUtils.getDistanceToNearestBlock(parent.mc.thePlayer, Block.fire, 8);
					heat = Math.max((float) (1 / dist) * 2.0F, heat);
				}
				if(EntityUtils.isBlockNearEntity(parent.mc.thePlayer, Block.lavaStill, 8)) {
					double dist = EntityUtils.getDistanceToNearestBlock(parent.mc.thePlayer, Block.lavaStill, 8);
					heat = Math.max((float) (1 / dist) * 5.0F, heat);
				}
				if(EntityUtils.isBlockNearEntity(parent.mc.thePlayer, Block.lavaMoving, 8)) {
					double dist = EntityUtils.getDistanceToNearestBlock(parent.mc.thePlayer, Block.lavaMoving, 8);
					heat = Math.max((float) (1 / dist) * 5.0F, heat);
				}
				factor += heat;
				return factor != 1.0F;
			}

			@Override
			public float getFactorRate() {
				return 0.0001F;
			}
		});
	}

	@Override
	public void onTick() {
		for(TemperatureFactor tf : factors) {
			if(tf.runFactor()) {
				temperature = (float) (temperature + (tf.getFactor() - temperature) * tf.getFactorRate());
			}
		}
		temperature = temperature > 2.0F ? 2.0F : temperature;
		temperature = temperature < 0.0F ? 0.0F : temperature;
		if(temperature < 0.25F) {
			float cold = (1.0F - (temperature * 4.0F)) * 0.75F;
			VisualManager.adjustColdOverlay(cold < 1.0F ? cold : 1.0F);
		}
		if(temperature > 1.0F) {
			float heat = (temperature - 1.0F) * 0.75F;
			VisualManager.adjustHeatOverlay(heat < 1.0F ? heat : 1.0F);
		}
	}

	@Override
	public void resetEffect() {
		temperature = 1.0F;
	}
}
