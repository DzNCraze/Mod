package Sonicjumper.EnhancedVisuals.src.event;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import Sonicjumper.EnhancedVisuals.src.ConfigCore;
import Sonicjumper.EnhancedVisuals.src.environment.BaseEnvironmentEffect;
import Sonicjumper.EnhancedVisuals.src.environment.EyeSensitivityHandler;
import Sonicjumper.EnhancedVisuals.src.environment.PotionSplashHandler;
import Sonicjumper.EnhancedVisuals.src.environment.TemperatureHandler;
import Sonicjumper.EnhancedVisuals.src.environment.WetnessHandler;
import Sonicjumper.EnhancedVisuals.src.visual.Blur;
import Sonicjumper.EnhancedVisuals.src.visual.BoxBlur;
import Sonicjumper.EnhancedVisuals.src.visual.GrayscaleBlur;
import Sonicjumper.EnhancedVisuals.src.visual.MotionBlur;
import Sonicjumper.EnhancedVisuals.src.visual.Visual;
import Sonicjumper.EnhancedVisuals.src.visual.VisualManager;
import Sonicjumper.EnhancedVisuals.src.visual.VisualType;

public class VisualEventHandler {
	public Minecraft mc;
	private ArrayList<BaseEnvironmentEffect> environmentalEffects;
	
	public WetnessHandler wetnessHandler = new WetnessHandler(this);
	public EyeSensitivityHandler eyeSensitivityHandler = new EyeSensitivityHandler(this);
	public TemperatureHandler temperatureHandler = new TemperatureHandler(this);
	public PotionSplashHandler potionSplashHandler = new PotionSplashHandler(this);
	
	private int lowHealthBuffer;
	private float playerTemp = 1.0F;
	
	public VisualEventHandler(Minecraft minecraftInstance) {
		mc = minecraftInstance;
		environmentalEffects = new ArrayList<BaseEnvironmentEffect>();
		environmentalEffects.add(wetnessHandler);
		environmentalEffects.add(eyeSensitivityHandler);
		environmentalEffects.add(temperatureHandler);
		environmentalEffects.add(potionSplashHandler);
	}
	
	@ForgeSubscribe
	public void onPlayerDamage(LivingHurtEvent e) {
		if(e.source.equals(DamageSource.outOfWorld)) {
			// If damage from void or /kill command, ignore
			return;
		}
		if(e.entityLiving instanceof EntityPlayer && ((EntityPlayer) e.entityLiving).equals(mc.thePlayer)) {
			// The entity is the Player, this is all self-bleed functions
			EntityPlayer p = (EntityPlayer) e.entityLiving;
			if(e.source.getEntity() != null && e.source.getEntity() instanceof EntityLivingBase) {
				// If Player was attacked by another EntityLivingBase
				EntityLivingBase elb = (EntityLivingBase) e.source.getEntity();
				if((elb.getHeldItem() != null && isSharp(elb.getHeldItem().getItem())) || elb instanceof EntityZombie) {
					VisualManager.createVisualFromDamage(VisualType.slash, e.ammount, e.entityLiving);
				} else if((elb.getHeldItem() != null && isBlunt(elb.getHeldItem().getItem())) || elb instanceof EntityGolem) {
					VisualManager.createVisualFromDamage(VisualType.impact, e.ammount, e.entityLiving);
				} else if((elb.getHeldItem() != null && isPierce(elb.getHeldItem().getItem())) || elb instanceof EntitySpider || elb instanceof EntityWolf) {
					VisualManager.createVisualFromDamage(VisualType.pierce, e.ammount, e.entityLiving);
				} else {
					VisualManager.createVisualFromDamage(VisualType.splatter, e.ammount, e.entityLiving);
				}
			}
			// If player was hit by an arrow
			if(e.source.getEntity() != null && e.source.getEntity() instanceof EntityArrow || e.source.equals(DamageSource.cactus)) {
				VisualManager.createVisualFromDamage(VisualType.pierce, e.ammount, e.entityLiving);
			}
			// If player received fall damage
			if(e.source.equals(DamageSource.fall) || e.source.equals(DamageSource.fallingBlock)) {
				VisualManager.createVisualFromDamage(VisualType.impact, e.ammount, e.entityLiving);
			}
			
			// Create dust, blur, and sound if damage was explosion
			if(e.source.isExplosion()) {
				if(e.source.getSourceOfDamage() != null && e.source.getSourceOfDamage().getDistanceToEntity(mc.thePlayer) < 16.0D) {
					VisualManager.createVisualFromDamageAndDistance(VisualType.dust, e.ammount, e.entityLiving, e.source.getSourceOfDamage().getDistanceSqToEntity(mc.thePlayer));
					Blur b = new BoxBlur(VisualType.blur, (int) (e.ammount * 10), new Color(1.0F, 1.0F, 1.0F, 0.8F), true, ConfigCore.blurQuality, 10, 1);
					VisualManager.addVisual(b);
				} else {
					VisualManager.createVisualFromDamage(VisualType.dust, e.ammount, e.entityLiving);
					Blur b = new BoxBlur(VisualType.blur, (int) (e.ammount * 10), new Color(1.0F, 1.0F, 1.0F, 0.8F), true, ConfigCore.blurQuality, 10, 1);
					VisualManager.addVisual(b);
				}
			}
			
			// Generate damaged overlay from any damage except lava
			if(getOverlayFromSource(e.source) != null) {
				//VisualManager.addVisualsWithColor(getOverlayFromSource(e.source), 1, (int) (e.ammount * 10), (int) (e.ammount * 15), new Color(1.0F, 1.0F, 1.0F, 0.6F));
			}
			if(e.source.equals(DamageSource.drown)) {
				VisualManager.addRandomNumVisualsWithColor(VisualType.waterS, 4, 8, (int) (e.ammount * 10), (int) (e.ammount * 15), new Color(1.0F, 1.0F, 1.0F, 1.0F));
			}
			// Add zoom blur effect
			VisualManager.addVisual(new MotionBlur(VisualType.blur, (int) (10 * e.ammount), new Color(1.0F, 1.0F, 1.0F, 1.0F), true, 0.5F, 0.05F * e.ammount));
		} else {
			// The entity is NOT the Player, this is all vicinity splatter functions
			if(mc.thePlayer != null) {
				DamageSource ds = e.source;
				if(ds.equals(DamageSource.anvil) || ds.equals(DamageSource.fall) || ds.equals(DamageSource.fallingBlock)
						|| ds.getDamageType().equals("mob") || ds.getDamageType().equals("player")) {
					if(e.entityLiving.getDistanceToEntity(mc.thePlayer) < 8.0D) {
						VisualManager.createVisualFromDamageAndDistance(VisualType.splatter, e.ammount, e.entityLiving, e.entityLiving.getDistanceSqToEntity(mc.thePlayer));
					}
				}
			}
		}
	}

	@ForgeSubscribe
	public void onPlayerDeath(LivingDeathEvent e) {
		if(e.entityLiving instanceof EntityPlayer && ((EntityPlayer) e.entityLiving).equals(mc.thePlayer)) {
			VisualManager.clearVisuals();
			for(BaseEnvironmentEffect ee : environmentalEffects) {
				ee.resetEffect();
			}
		}
	}
	
	private boolean isSharp(Item item) {
		return item.equals(Item.axeDiamond) || item.equals(Item.axeGold) || item.equals(Item.axeIron)
				|| item.equals(Item.axeStone) || item.equals(Item.axeWood) || item.equals(Item.swordDiamond)
				|| item.equals(Item.swordGold) || item.equals(Item.swordIron) || item.equals(Item.swordStone)
				|| item.equals(Item.swordWood);
	}
	
	private boolean isBlunt(Item item) {
		return item.equals(Item.shovelDiamond) || item.equals(Item.shovelGold) || item.equals(Item.shovelIron)
				|| item.equals(Item.shovelStone) || item.equals(Item.shovelWood) || item.equals(Item.pickaxeDiamond)
				|| item.equals(Item.pickaxeGold) || item.equals(Item.pickaxeIron) || item.equals(Item.pickaxeStone)
				|| item.equals(Item.pickaxeWood);
	}

	private boolean isPierce(Item item) {
		return item.equals(Item.arrow);
	}
	
	private VisualType getOverlayFromSource(DamageSource ds) {
		if(ds.equals(DamageSource.lava)) {
			return VisualType.lavaO;
		}
		if(ds.equals(DamageSource.anvil) || ds.equals(DamageSource.cactus) || ds.equals(DamageSource.fall)
				 || ds.equals(DamageSource.fallingBlock) || ds.getDamageType().equals("mob") || ds.getDamageType().equals("player")) {
			return VisualType.damaged;
		}
		if(ds.equals(DamageSource.drown)) {
			return VisualType.waterO;
		}
		return null;
	}

	public void onTick() {
		// If the player splashed then add blur and water splats
		if(hasSplashed(mc.thePlayer)) {
			VisualManager.addVisual(new BoxBlur(VisualType.blur, 60, new Color(1.0F, 1.0F, 1.0F, 1.0F), true, ConfigCore.blurQuality, 8, 1));
			VisualManager.addVisuals(VisualType.waterS, 20, 60, 100);
		}
		// "Wash" the player if it is in water
		if(mc.thePlayer.isInWater()) {
			ArrayList<Visual> visualList = VisualManager.getVisuals();

			for(int i = 0; i < visualList.size(); i++) {
				Visual accelerator = visualList.get(i);
				if(accelerator.getType().equals(VisualType.splatter) || accelerator.getType().equals(VisualType.slash)
						|| accelerator.getType().equals(VisualType.pierce) || accelerator.getType().equals(VisualType.impact)
						|| accelerator.getType().equals(VisualType.dust) || accelerator.getType().equals(VisualType.sand)
						|| accelerator.getType().equals(VisualType.fire) || accelerator.getType().equals(VisualType.lavaS)
						|| accelerator.getType().equals(VisualType.lavaO) || accelerator.getType().equals(VisualType.potion)) {
					accelerator.subTime(0.1F);
				}
			}
		}
		// Add a lowhealth overlay every 15 ticks if the players health is 3 hearts or less
		if(mc.thePlayer.getHealth() < 7.0F) {
			if(lowHealthBuffer <= 0) {
				float f1 = (7.0F - mc.thePlayer.getHealth()) * 0.25F;
				if(ConfigCore.grayscaleLowHealth) {
					VisualManager.addVisual(new GrayscaleBlur(VisualType.blur, (int) (5 * (7.0F - mc.thePlayer.getHealth())), new Color(1.0F, 1.0F, 1.0F, f1 <= 0.8F ? f1 : 0.8F), false, 0.75F));
					//VisualManager.addVisual(new GrayscaleBlur(VisualType.blur, (int) (5 * (6.0F - mc.thePlayer.func_110143_aJ())), new Color(1.0F, 1.0F, 1.0F, f1 <= 1.0F ? f1 : 1.0F), 0, 0, true, 1.0F));
				} else {
					VisualManager.addVisualsWithColor(VisualType.lowhealth, 1, (int) (5 * (7.0F - mc.thePlayer.getHealth())), (int) (10 * (7.0F - mc.thePlayer.getHealth())), new Color(1.0F, 1.0F, 1.0F, f1 <= 1.0F ? f1 : 1.0F));
				}
				lowHealthBuffer = 15;
			} else {
				lowHealthBuffer--;
			}
		}

		for(BaseEnvironmentEffect ee : environmentalEffects) {
			// Keep track of Player's "wetness"
			// Adjust the player's vision depending on sudden increases in light
			// Keep track of Player's "temperature"
			ee.onTick();
		}
	}
	
	private boolean hasSplashed(EntityPlayer entityPlayer) {
		return hasSplashedIn(entityPlayer) || hasSplashedOut(entityPlayer);
	}
	
	private boolean hasSplashedIn(EntityPlayer entityPlayer) {
		int x = (int) Math.floor(entityPlayer.posX);
		int y = (int) (entityPlayer.posY - entityPlayer.getEyeHeight());
		int z = (int) Math.floor(entityPlayer.posZ);
		
		int prevX = (int) Math.floor(entityPlayer.prevPosX);
		int prevY = (int) (entityPlayer.prevPosY - entityPlayer.getEyeHeight());
		int prevZ = (int) Math.floor(entityPlayer.prevPosZ);
		
		if(mc.theWorld != null)
		{
			if((mc.theWorld.getBlockId(x, y, z) == Block.waterStill.blockID || mc.theWorld.getBlockId(x, y, z) == Block.waterMoving.blockID) && (mc.theWorld.getBlockId(prevX, prevY, prevZ) != Block.waterStill.blockID && mc.theWorld.getBlockId(prevX, prevY, prevZ) != Block.waterMoving.blockID))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean hasSplashedOut(EntityPlayer entityPlayer) {
		int x = (int) Math.floor(entityPlayer.prevPosX);
		int y = (int) (entityPlayer.prevPosY - entityPlayer.getEyeHeight());
		int z = (int) Math.floor(entityPlayer.prevPosZ);
		
		int prevX = (int) Math.floor(entityPlayer.posX);
		int prevY = (int) (entityPlayer.posY - entityPlayer.getEyeHeight());
		int prevZ = (int) Math.floor(entityPlayer.posZ);
		
		if(mc.theWorld != null)
		{
			if((mc.theWorld.getBlockId(x, y, z) == Block.waterStill.blockID || mc.theWorld.getBlockId(x, y, z) == Block.waterMoving.blockID) && (mc.theWorld.getBlockId(prevX, prevY, prevZ) != Block.waterStill.blockID && mc.theWorld.getBlockId(prevX, prevY, prevZ) != Block.waterMoving.blockID))
			{
				return true;
			}
		}
		return false;
	}
}
