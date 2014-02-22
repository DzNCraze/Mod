package Sonicjumper.EnhancedVisuals.src.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public class EntityUtils {
	public static boolean isBlockNearEntity(Entity e, Block b, int radius) {
		for(int x = (int) (e.posX - radius); x < e.posX + radius; x++) {
			for(int y = (int) (e.posY - (radius/2)); y < e.posY + radius; y++) {
				for(int z = (int) (e.posZ - radius); z < e.posZ + radius; z++) {
					if(e.worldObj.getBlockId(x, y, z) == b.blockID) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static double getDistanceToNearestBlock(Entity e, Block b, int radius) {
		double minDist = radius * 2;
		for(int x = (int) (e.posX - radius); x < e.posX + radius; x++) {
			for(int y = (int) (e.posY - (radius/2)); y < e.posY + radius; y++) {
				for(int z = (int) (e.posZ - radius); z < e.posZ + radius; z++) {
					if(e.worldObj.getBlockId(x, y, z) == b.blockID) {
						double dist = Math.sqrt(MathUtils.sq(e.posX - x) + MathUtils.sq(e.posY - y) + MathUtils.sq(e.posZ - z));
						minDist = Math.min(minDist, dist);
					}
				}
			}
		}
		return minDist;
	}
}
