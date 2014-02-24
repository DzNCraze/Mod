package Sonicjumper.EnhancedVisuals.src.visual;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.minecraft.util.ResourceLocation;
import Sonicjumper.EnhancedVisuals.src.ClientProxy;
import Sonicjumper.EnhancedVisuals.src.ConfigCore;
import Sonicjumper.EnhancedVisuals.src.EnhancedVisuals;
import Sonicjumper.EnhancedVisuals.src.visual.Visual.VisualCatagory;

public class VisualType
{
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
	
	public VisualType(int id, VisualCatagory catagory, String name, boolean physics)
	{
		if(visualList[id] != null)
		{
			throw new IllegalArgumentException("Slot " + id + " is already occupied by " + visualList[id] + " when adding " + this);
		} else
		{
			visualList[id] = this;
			visualID = id;
			visualCatagory = catagory;
			visualName = name;
			hasPhysics = physics;
			if(catagory != VisualCatagory.shader)
			{
				createResources();
			} else
			{
				resourceArray = new ResourceLocation[]{new ResourceLocation("sonicjumper", "textures/entity/steve.png")};
			}
			
		}
	}
	
	public void createResources()
	{
		themePack = ConfigCore.currentThemePack;
		try
		{
			resourceArray = createResourcesForVisualType(this);
		} catch(FileNotFoundException e)
		{
			
		}
		if(resourceArray == null || (resourceArray != null && resourceArray.length == 0))
		{
			
			themePack = ConfigCore.backupThemePack;
			try
			{
				resourceArray = createResourcesForVisualType(this);
			} catch(FileNotFoundException e)
			{
				
			}
			if(resourceArray == null || (resourceArray != null && resourceArray.length == 0))
			{
				
				themePack = ConfigCore.defaultThemePack;
				try
				{
					resourceArray = createResourcesForVisualType(this);
				} catch(FileNotFoundException e)
				{
					
				}
				
				if(resourceArray == null)
				{
					System.out.println("[SEVERE] Enhanced visuals has failed to load any resources!");
					resourceArray = new ResourceLocation[]{new ResourceLocation("")};
				}
			}
		}
	}
	
	public static VisualType addNewType(int id, VisualCatagory catagory, String name, boolean physics)
	{
		return new VisualType(id, catagory, name, physics);
	}
	
	private ResourceLocation[] createResourcesForVisualType(VisualType vt) throws FileNotFoundException
	{
		List<ResourceLocation> result = new LinkedList<ResourceLocation>();
		ArrayList<File> fileList = new ArrayList<File>();
		
		try
		{
			CodeSource src = EnhancedVisuals.class.getProtectionDomain().getCodeSource();
			if (src != null)
			{
				boolean loadAsDir = false;
				URL jar = src.getLocation();
				
				if(src.getLocation().toString().endsWith("EnhancedVisuals.class"))
				{
					jar = new URL(src.getLocation().toString().replace("/Sonicjumper/EnhancedVisuals/src/EnhancedVisuals.class", "/assets/sonicjumper/"));
					loadAsDir = true;
				}
				
				if(loadAsDir)
				{
					File URL_File;
					try
					{
						URL_File = new File(jar.toURI());
					} catch(URISyntaxException e)
					{
						URL_File = new File(jar.getPath());
					}
					System.out.println("Loading entries in " + URL_File.getAbsoluteFile() + "/" + ClientProxy.getVisualsDirectory(themePack) + visualCatagory.toString() + "/" + visualName);
					
					File f = new File(URL_File.getAbsoluteFile() + "/" + ClientProxy.getVisualsDirectory(themePack) + visualCatagory.toString() + "/" + visualName);
					
					File[] list = f.listFiles();
					
					if(list != null)
					{
						for(int i = 0; i < list.length; i++)
						{
							System.out.println("Loading texture: " + list[i].getName());
							fileList.add(list[i]);
						}
					} else
					{
						System.out.println("No textures found");
					}
				} else
				{
					ZipInputStream zip = new ZipInputStream(jar.openStream());
					System.out.println("Loading entries in " + jar.toString());
					while(true)
					{
						ZipEntry e = zip.getNextEntry();
					    if (e == null)
					    {
							break;
					    }
					    String name = e.getName();
					    if (name.contains(ClientProxy.getVisualsDirectory(themePack) + visualCatagory.toString() + "/" + visualName))
					    {
					    	fileList.add(new File(name));
					    }
					}
					zip.close();
				}
			} 
			else
			{
				System.out.println("Failed to find containing jar!");
			}
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		
		/*File f = new File(ClientProxy.getVisualsDirectory(themePack) + visualCatagory.toString() + "/" + visualName);
		
		list = f.listFiles();*/

		if(fileList == null) {
			return null;//throw new FileNotFoundException(f.getPath());
		}

		int matchedIndex = 0;

		for(int i = 0; i < fileList.size(); i++) {
			if(fileList.get(i).getName().toLowerCase().contains(visualName.toLowerCase())) {
				result.add(matchedIndex++, new ResourceLocation("sonicjumper", themePack + "/visuals/" + vt.visualCatagory.toString() + "/" + vt.visualName + "/" + fileList.get(i).getName()));
			}
			
			System.out.println("Loading '"+ visualCatagory.toString() + "' texture: " + fileList.get(i).getAbsoluteFile());
		}

		if(matchedIndex > 0) {
			File firstImage = new File(ClientProxy.getVisualsDirectory(themePack) + vt.visualCatagory.toString() + "/" + vt.visualName + "/" + new File(result.get(0).getResourcePath()).getName());
			try {
				imageDimensions = getDimensionsOfImage(firstImage);
			} catch (Exception e) {
				
			}
		} else {
			imageDimensions = new Dimension(0, 0);
		}

		return result.toArray(new ResourceLocation[0]);
	}
	
	private Dimension getDimensionsOfImage(File resourceFile) throws IOException
	{
		ImageInputStream in;
		in = ImageIO.createImageInputStream(resourceFile);
		try
		{
			final Iterator readers = ImageIO.getImageReaders(in);
			if(readers.hasNext())
			{
				ImageReader reader = (ImageReader)readers.next();
				try
				{
					reader.setInput(in);
					return new Dimension(reader.getWidth(0), reader.getHeight(0));
				} finally
				{
					reader.dispose();
				}
			}
		} finally
		{
			if(in != null)
				in.close();
		}
		return new Dimension(0, 0);
	}
	
	public int getSize()
	{
		return imageDimensions != null ? imageDimensions.height : 0;
	}
	
	public VisualCatagory getCatagory()
	{
		return visualCatagory;
	}
	
	public int getID()
	{
		return visualID;
	}
	
	public boolean hasPhysics()
	{
		return hasPhysics;
	}
}