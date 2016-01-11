package pl.com.itsense.csssprites;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.imageio.ImageIO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.com.itsense.maven.api.CssSpriteData;
import pl.com.itsense.maven.api.ImageData;

/**
 * 
 * @author ppretki
 *
 */
public abstract class WebAppUtils
{
    /** */
    final static Logger LOG = LoggerFactory.getLogger(WebAppUtils.class);
    
   /**
    *  
    * @param positions
    * @return
    */
   public static CssSpriteData getSpriteData(final Map<ImageData, Rectangle> positions)
   {
       final CssSpriteData spriteData = new CssSpriteData();
       int x = Integer.MAX_VALUE;
       int y = Integer.MAX_VALUE;
       int width = 0;
       int height = 0;
       for (final ImageData image : positions.keySet())
       {
           final Rectangle rect = positions.get(image);
           if (x > rect.x) x = rect.x;
           if (y > rect.y) y = rect.y;
           if (width < rect.x + rect.width) width = rect.x + rect.width;
           if (height < rect.y + rect.height) height = rect.y + rect.height;
       }
       final ArrayList<ImageData> images = new ArrayList<ImageData>();
       for (final ImageData image : positions.keySet())
       {
           final Rectangle rect = positions.get(image);
           final ImageData imageData = new ImageData(image);
           imageData.setXpos(rect.x - x);
           imageData.setYpos(rect.y - y);
           imageData.setWidth(rect.width);
           imageData.setHeight(rect.height);
           images.add(imageData);
       }
       spriteData.setImages(images.toArray(new ImageData[images.size()]));
       spriteData.setWidth(width - x);
       spriteData.setHeight(height - y);
       return spriteData;
   }
    /**
     * 
     * @return
     */
    public static ImageData[] getImageData(final Collection<String> filePaths)
    {
        final ArrayList<ImageData> data = new ArrayList<ImageData>();
        for (final String filePath : filePaths)
        {
            final ImageData imageData = getImageData(filePath);
            if (imageData != null)
            {
                data.add(imageData);
                LOG.debug("getImageData: file =  " + filePath + ", imageData = "+ imageData);
            }
            else
            {
                LOG.error("getImageData: Cannot create image data for " + filePath);
            }
        }
        return data.toArray(new ImageData[data.size()]);
    }
    /**
     * 
     * @return
     */
    public static ImageData getImageData(final String filePath)
    {
        return StringUtils.isNotBlank(filePath) ? getImageData(new File(filePath)) : null;
    }
    /**
     * 
     * @param file
     * @return
     */
    public static ImageData getImageData(final File file)
    {
        ImageData imageData = null;
        if (file != null && file.exists() && !file.isDirectory())
        {
            BufferedImage bufferedImage = null;
            try
            {
                bufferedImage = ImageIO.read(file);
                imageData = new ImageData();
                imageData.setHeight(bufferedImage.getHeight());
                imageData.setWidth(bufferedImage.getWidth());
                imageData.setPath(file.getPath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally 
            {
                if (bufferedImage != null)
                {
                    bufferedImage.flush();
                }
            }
        }
        else
        {
            LOG.error("getImageData: cannot process file = " + file);

        }
        return imageData;
    }
    /**
     * 
     * @param spriteData
     * @return
     */
    public static String getStylesheetClass(final CssSpriteData spriteData, final String baseURL)
    {
        final StringBuilder sb = new StringBuilder();
        appendStylesheetClass(sb, spriteData, baseURL);
        return sb.toString();
                
    }
    
    /**
     * 
     * @param buffer
     * @param spriteData
     */
    public static void appendStylesheetClass(final StringBuilder buffer, final CssSpriteData spriteData, final String baseURL)
    {
       for (final ImageData image : spriteData.getImages())
       {
           appendStylesheetClass(buffer, spriteData, image, baseURL);
       }
    }
    
    /**
     * 
     * @return
     */
    public static String appendStylesheetClass(final StringBuilder buffer, final CssSpriteData spriteData, final ImageData imageData, final String baseURL)
    {
        final String name = spriteData.getName() + "-" + imageData.getName();
        buffer.append(".").append(name).append("{\n");
        buffer.append("background-image: url(\"").append(baseURL).append(spriteData.getHashFile()).append("\");\n");
        buffer.append("background-repeat: no-repeat;\n");
        buffer.append("background-position:").append(imageData.getXpos()).append("px ").append(imageData.getYpos()).append("px;\n");
        buffer.append("width:").append(imageData.getWidth()).append("px;\n");
        buffer.append("height:").append(imageData.getHeight()).append("px;\n");
        buffer.append("}\n");
        return name;
    }
}
