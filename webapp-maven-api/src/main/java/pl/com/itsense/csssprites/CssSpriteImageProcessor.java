package pl.com.itsense.csssprites;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.com.itsense.csssprites.impl.SimpleVerticalLayoutManager;
import pl.com.itsense.maven.api.CssSpriteData;
import pl.com.itsense.maven.api.ImageData;

/**
 * 
 * @author ppretki
 *
 */
public class CssSpriteImageProcessor
{
    private final static Logger LOG = LoggerFactory.getLogger(CssSpriteImageProcessor.class);
    /** */
    public static final String FORMAT_PNG = "png";
    /** */
    public static final String FORMAT_JPG = "jpg";
    /**
     * 
     */
    public static CssSpriteData process(final Collection<String> fileNames, final File output) 
    {
        final ImageData[] images = WebAppUtils.getImageData(fileNames);
        final LayoutManager layoutManager = new SimpleVerticalLayoutManager();
        final Map<ImageData, Rectangle> positions = layoutManager.evalPositions(images);
        final CssSpriteData spriteData = WebAppUtils.getSpriteData(positions);
        createImage(spriteData, output, BufferedImage.TYPE_INT_RGB, Color.WHITE, "png");
        return spriteData;
    }

    /**
     * 
     * @param sprite
     * @param spriteOutDirectory
     * @param cssOutDirectory
     * @param layoutManager
     * @param format see subclasses of {@link ImageWriterSpi}
     */
    public static void createImage(final CssSpriteData spriteData, final File output, final int type, final Color bg,final String format)
    {
        final BufferedImage spriteImage = new BufferedImage(spriteData.getWidth(), spriteData.getHeight(), type);
        final Graphics spriteGraphics = spriteImage.getGraphics();
        spriteGraphics.setColor(bg);
        spriteGraphics.fillRect(0, 0, spriteData.getWidth(), spriteData.getHeight());
        for (final ImageData image : spriteData.getImages())
        {
            try
            {
                final BufferedImage bImage = ImageIO.read(new File(image.getPath()));
                if (bImage != null)
                {
                    spriteGraphics.drawImage(bImage, image.getXpos(), image.getYpos(), null);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        spriteGraphics.dispose();
        try
        {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ImageIO.write( spriteImage,  format, buffer );
            spriteData.setHashFile(DigestUtils.md5Hex(buffer.toByteArray()) + "." + format);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        try
        {
            final File spriteFile = new File(output, spriteData.getHashFile() + "." + format);
            ImageIO.write(spriteImage, format, spriteFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
