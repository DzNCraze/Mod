package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.minecraft.util.ResourceLocation;
import Sonicjumper.EnhancedVisuals.src.ClientProxy;
import Sonicjumper.EnhancedVisuals.src.ConfigCore;
import Sonicjumper.EnhancedVisuals.src.visual.Visual.VisualCatagory;

public class VisualType {
	public final static VisualType[] visualList = new VisualType[4096];

	public static VisualType splatter = new VisualType(0, VisualCatagory.splat, "splatter", true);
	public static VisualType impact = new VisualType(1, VisualCatagory.splat, "impact", true);
	public static VisualType slash = new VisualType(2, VisualCatagory.splat, "slash", true);
	public static VisualType pierce = new VisualType(3, VisualCatagory.splat, "pierce", true);
	public static VisualType dust = new VisualType(4, VisualCatagory.splat, "dust", true);
	public static VisualType fire = new VisualType(5, VisualCatagory.splat, "fire", true);
	public static VisualType lavaS = new VisualType(6, VisualCatagory.splat, "lava", true);
	public static VisualType sand = new VisualType(7, VisualCatagory.splat, "sand", true);
	public static VisualType waterS = new VisualType(8, VisualCatagory.splat, "water", true);
	public static VisualType snow = new VisualType(9, VisualCatagory.splat, "snow", true);

	public static VisualType lowhealth = new VisualType(10, VisualCatagory.overlay, "lowhealth", false);
	public static VisualType damaged = new VisualType(11, VisualCatagory.overlay, "damaged", false);
	public static VisualType lavaO = new VisualType(12, VisualCatagory.overlay, "lava", false);
	public static VisualType potion = new VisualType(13, VisualCatagory.overlay, "potion", false);
	public static VisualType waterO = new VisualType(14, VisualCatagory.overlay, "water", false);
	public static VisualType ice = new VisualType(15, VisualCatagory.overlay, "ice", false);
	public static VisualType heat = new VisualType(16, VisualCatagory.overlay, "heat", false);
	public static VisualType colorTemplate = new VisualType(17, VisualCatagory.overlay, "colorTemplate", false);

	public static VisualType slender = new VisualType(18, VisualCatagory.animation, "slender", false);
	public static VisualType crack = new VisualType(19, VisualCatagory.animation, "crack", false);

	public static VisualType blur = new VisualType(20, VisualCatagory.shader, "blur", false);
	//public static VisualType variableBlur = new VisualType(20, VisualCatagory.shader, "variableBlur");
	//public static VisualType glowBlur = new VisualType(21, VisualCatagory.shader, "glowBlur");
	//public static VisualType rayBlur = new VisualType(22, VisualCatagory.shader, "rayBlur");

	//public HashMap<VisualType, Integer> visualCounts = new HashMap<VisualType, Integer>();

	private int visualID;
	private VisualCatagory visualCatagory;
	private String visualName, themePack;
	private Dimension imageDimensions;
	private boolean hasPhysics;

	public ResourceLocation[] resourceArray;

	public VisualType(int id, VisualCatagory catagory, String name, boolean physics) {
		if (visualList[id] != null)
		{
			throw new IllegalArgumentException("Slot " + id + " is already occupied by " + visualList[id] + " when adding " + this);
		} else {
			visualList[id] = this;
			visualID = id;
			visualCatagory = catagory;
			visualName = name;
			hasPhysics = physics;
			if(catagory != VisualCatagory.shader) {
				createResources();
				for(ResourceLocation s : resourceArray) {
					System.out.println(s.getResourcePath());
				}
			} else {
				resourceArray = new ResourceLocation[] {new ResourceLocation("sonicjumper", "textures/entity/steve.png")};
			}
			System.out.println(id + " || " + visualCatagory.toString() + " || " + name + " || " + resourceArray.length);
		}
	}

	public void createResources() {
		themePack = ConfigCore.currentThemePack;
		try {
			resourceArray = createResourcesForVisualType(this);
		} catch(FileNotFoundException e) {
			System.out.println("[Enhanced Visuals] Could not find the directory, make sure you installed the theme pack correctly: " + ClientProxy.getVisualsDirectory(themePack) + visualCatagory.toString() + "/" + visualName);
		}
		if(resourceArray == null || (resourceArray != null && resourceArray.length == 0)) {
			System.out.println("[Enhanced Visuals] Using backups for:" + visualName);
			themePack = ConfigCore.backupThemePack;
			try {
				resourceArray = createResourcesForVisualType(this);
			} catch (FileNotFoundException e) {
				System.out.println("[Enhanced Visuals] Error finding backup directory, make sure you installed the theme pack correctly: " + ClientProxy.getVisualsDirectory(themePack) + visualCatagory.toString() + "/" + visualName);
			}
			if(resourceArray == null || (resourceArray != null && resourceArray.length == 0)) {
				System.out.println("[Enhanced Visuals] Using defaults for:" + visualName);
				themePack = ConfigCore.defaultThemePack;
				try {
					resourceArray = createResourcesForVisualType(this);
				} catch (FileNotFoundException e) {
					System.out.println("[Enhanced Visuals] Error finding default directory, make sure you installed the mod correctly: " + ClientProxy.getVisualsDirectory(themePack) + visualCatagory.toString() + "/" + visualName);
				}
			}
		}
	}

	public static VisualType addNewType(int id, VisualCatagory catagory, String name, boolean physics) {
		System.out.println("Adding id:" + id + " into catagory:" + catagory.toString() + " with name:" + name);
		return new VisualType(id, catagory, name, physics);
	}

	private ResourceLocation[] createResourcesForVisualType(VisualType vt) throws FileNotFoundException {
		List<ResourceLocation> result = new LinkedList<ResourceLocation>();

		File f = new File(ClientProxy.getVisualsDirectory(themePack) + visualCatagory.toString() + "/" + visualName);
		//System.out.println(f.exists() ? "File does exist" : "File doesn't exist");
		File[] list = f.listFiles();

		if(list == null) {
			throw new FileNotFoundException(f.getPath());
		}

		int matchedIndex = 0;

		for(int i = 0; i < list.length; i++) {
			if(list[i].getName().toLowerCase().contains(visualName.toLowerCase())) {
				result.add(matchedIndex++, new ResourceLocation("sonicjumper", themePack + "/visuals/" + vt.visualCatagory.toString() + "/" + vt.visualName + "/" + list[i].getName()));
			}
		}

		if(matchedIndex > 0) {
			File firstImage = new File(ClientProxy.getVisualsDirectory(themePack) + vt.visualCatagory.toString() + "/" + vt.visualName + "/" + new File(result.get(0).getResourcePath()).getName());
			try {
				imageDimensions = getDimensionsOfImage(firstImage);
			} catch (Exception e) {
				System.out.println("[Enhanced Visuals] Could not read dimensions of image: " + f.getPath());
			}
		} else {
			imageDimensions = new Dimension(0, 0);
		}

		return result.toArray(new ResourceLocation[0]);
	}

	private Dimension getDimensionsOfImage(File resourceFile) throws IOException {
		ImageInputStream in;
		//System.out.println(resourceFile.getPath());
		in = ImageIO.createImageInputStream(resourceFile);
		try {
			final Iterator readers = ImageIO.getImageReaders(in);
			if (readers.hasNext()) {
				ImageReader reader = (ImageReader) readers.next();
				try {
					reader.setInput(in);
					return new Dimension(reader.getWidth(0), reader.getHeight(0));
				} finally {
					reader.dispose();
				}
			}
		} finally {
			if (in != null) in.close();
		}
		return new Dimension(0, 0);
	}

	public int getSize() {
		return imageDimensions != null ? imageDimensions.height : 0;
	}

	public VisualCatagory getCatagory() {
		return visualCatagory;
	}
	
	public int getID() {
		return visualID;
	}

	public boolean hasPhysics() {
		return hasPhysics;
	}
}