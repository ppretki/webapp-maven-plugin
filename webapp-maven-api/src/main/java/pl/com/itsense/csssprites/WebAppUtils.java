package pl.com.itsense.csssprites;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import pl.com.itsense.maven.api.ImageData;

/**
 * 
 * @author ppretki
 *
 */
public abstract class WebAppUtils
{
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
            
        }
        return imageData;
    }
}
