package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Color;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntitySquid;

public class VisualManager {
	private static ArrayList<Visual> playerVisuals = new ArrayList<Visual>();
	private static ArrayList<Visual> permVisuals = new ArrayList<Visual>();
	
	private static Random rand = new Random();
	private static Overlay heatOverlay;
	private static Overlay iceOverlay;
	private static Overlay wetOverlay;
	
	public VisualManager() {
		heatOverlay = new Overlay(VisualType.heat, -1, new Color(1.0F, 1.0F, 1.0F, 0.0F));
		iceOverlay = new Overlay(VisualType.ice, -1, new Color(1.0F, 1.0F, 1.0F, 0.0F));
		wetOverlay = new Overlay(VisualType.waterO, -1, new Color(1.0F, 1.0F, 1.0F, 0.0F));
		permVisuals.add(heatOverlay);
		permVisuals.add(iceOverlay);
		permVisuals.add(wetOverlay);
	}

	public static ArrayList<Visual> getVisuals() {
		ArrayList<Visual> alv = new ArrayList<Visual>();
		alv.addAll(playerVisuals);
		alv.addAll(permVisuals);
		return alv;
	}
	
	public static void removeVisual(Visual v) {
		v.beingRemoved();
		playerVisuals.remove(v);
	}
	
	public static void clearVisuals() {
		playerVisuals.clear();
	}
	
	public static void addVisuals(VisualType vt, int num, int minTime, int maxTime) {
		addVisualsWithColor(vt, num, minTime, maxTime, new Color(1.0F, 1.0F, 1.0F, 1.0F));
	}

	public static void addVisual(Visual v) {
		playerVisuals.add(v);
	}
	
	public static void addRandomNumVisualsWithColor(VisualType vt, int minNum, int maxNum, int minTime, int maxTime, Color color) {
		if(maxNum <= minNum) {
			addVisualsWithColor(vt, minNum, minTime, maxTime, color);
		} else {
			addVisualsWithColor(vt, minNum + rand.nextInt(maxNum - minNum), minTime, maxTime, color);
		}
	}
	
	public static void addVisualsWithColor(VisualType vt, int num, int minTime, int maxTime, Color color) {
		for(int i = 0; i < num; i++) {
			Visual v;
			switch(vt.getCatagory()) {
			case animation:
				if(maxTime <= minTime) {
					v = new Animation(vt, minTime, color);
				} else {
					v = new Animation(vt, minTime + rand.nextInt(maxTime - minTime), color);
				}
				break;
			case overlay:
				if(maxTime <= minTime) {
					v = new Overlay(vt, minTime, color);
				} else {
					v = new Overlay(vt, minTime + rand.nextInt(maxTime - minTime), color);
				}
				break;
			case splat:
				if(maxTime <= minTime) {
					v = new Splat(vt, minTime, color);
				} else {
					v = new Splat(vt, minTime + rand.nextInt(maxTime - minTime), color);
				}
				break;
			default:
				if(maxTime <= minTime) {
					v = new Visual(vt, minTime, color);
				} else {
					v = new Visual(vt, minTime + rand.nextInt(maxTime - minTime), color);
				}
				break;
			}
			playerVisuals.add(v);
		}
	}
	
	/**
	 * See createVisualFromDamageAndDistance(VisualType, float, EntityLivingBase, float);
	 */
	public static void createVisualFromDamage(VisualType type, float damage, EntityLivingBase bleedingEntity) {
		createVisualFromDamageAndDistance(type, damage, bleedingEntity, 1.0D);
	}
	
	/**
	 * Creates visuals based off a simple algorithm and a random variable:
	 * damage * healthRate * distMultiplier * randMultiplier(0.5D, 1.2D) + 1
	 * @param type Visual type to create
	 * @param damage Amount of damage dealt
	 * @param bleedingEntity Entity being damaged
	 * @param distanceSqToEntity Distance to damaged(keep 1.0 if null)
	 */
	public static void createVisualFromDamageAndDistance(VisualType type, float damage, EntityLivingBase bleedingEntity, double distanceSqToEntity) {
		double distance = Math.sqrt(distanceSqToEntity);
		if(distance < 1.0D) {
			distance = 1.0D;
		}
		double distMultiplier = 1/distance;
		if(damage == 0)
    	{
    		return;
    	}
    	if(damage < 0)
    	{
    		damage = 0;
    		return;
    	}
    	float rate = 0.0F;
		float health = bleedingEntity.func_110143_aJ() - damage;
		
		//largeRate = (health / 4) + 1.0F;
		
		if(health > 12) {
			rate = 1.0F;
		}
		if(health <= 12 && health > 8) {
			rate = 1.5F;
		}
		if(health <= 8 && health > 4) {
			rate = 2.0F;
		}
		if(health <= 4 && health > 0) {
			rate = 2.5F;
		}
		if(health <= 0) {
			rate = 3.0F;
		}
		int splats = (int) (damage * rate * distMultiplier * randMultiplier(0.5D, 1.2D) + 1);
		if(type.equals(VisualType.splatter) || type.equals(VisualType.slash) || type.equals(VisualType.pierce) || type.equals(VisualType.impact)) {
			if(bleedingEntity instanceof EntityCreeper) {
				addVisualsWithColor(type, splats, 500, 1500, new Color(0.0F, 0.4F, 0.0F, 0.7F));
			} else if(bleedingEntity instanceof EntitySkeleton) {
				addVisualsWithColor(type, splats, 500, 1500, new Color(0.1F, 0.1F, 0.1F, 0.7F));
			} else if(bleedingEntity instanceof EntitySquid) {
				addVisualsWithColor(type, splats, 500, 1500, new Color(0.0F, 0.0F, 0.2F, 0.7F));
			} else {
				addVisualsWithColor(type, splats, 500, 1500, new Color(0.3F, 0.01F, 0.01F, 0.7F));
			}
		} else if(type.equals(VisualType.dust)) {
			addVisualsWithColor(type, splats * 20, 10, 100, new Color(0.2F, 0.2F, 0.2F, 1.0F));
		}
	}

	private static float randMultiplier(double min, double max) {
		return (float) (min + rand.nextFloat() * (max - min));
	}
	
	public static void adjustHeatOverlay(float intensity) {
		heatOverlay.setTranslucency(intensity);
	}
	
	public static void adjustColdOverlay(float intensity) {
		iceOverlay.setTranslucency(intensity);
	}
	
	public static void adjustWetOverlay(float intensity) {
		wetOverlay.setTranslucency(intensity);
	}
}
