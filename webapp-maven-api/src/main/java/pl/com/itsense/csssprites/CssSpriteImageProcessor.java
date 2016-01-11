package pl.com.itsense.csssprites;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import pl.com.itsense.maven.api.CssSpriteData;


/**
 * 
 * @author ppretki
 *
 */
public class CssSpriteImageProcessor
{

    /**
     * 
     */
    public void process(final Collection<String> fileNames, final File output) 
    {
        CssSpriteData
    }

    /**
     * 
     * @param sprite
     * @param spriteOutDirectory
     * @param cssOutDirectory
     * @param layoutManager
     */
    private void create(final CssSpriteData spriteData, final String spriteOutDirectory, final String cssOutDirectory, final LayoutManager layoutManager)
    {
        Map<Img, Rectangle> bounds = new HashMap<Img, Rectangle>();
        for (final Img img : sprite.getImgs())
        {
            try
            {
                final BufferedImage bImage = ImageIO.read(new File(img.getFile()));
                {
                    bounds.put(img, new Rectangle(bImage.getWidth(), bImage.getHeight()));
                }
                getLog().info("Image: " + img.getFile() + " has been added to CSS Sprite: " + sprite.getName());
            }
            catch (IOException e)
            {
                getLog().error("Image: " + img.getFile() + " not found");
            }
        }
        if (!bounds.isEmpty())
        {
            bounds = layoutManager.eval(bounds);
            int width = 0;
            int height = 0;
            for (final Rectangle rect : bounds.values())
            {
                if (rect.x + rect.width > width)
                {
                    width = rect.x + rect.width;
                }
                if (rect.y + rect.height > height)
                {
                    height = rect.y + rect.height;
                }
            }
            final BufferedImage out;
            Color bgColor = null;
            if (Sprite.COLOR_MODEL_RGB.equals(sprite.getColorModel()))
            {
                out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                if (Utils.isNotNullNorEmpty(sprite.getBgColor()))
                {
                    final String[] tab = sprite.getBgColor().split(",");
                    if (tab.length == 3)
                    {
                        int[] color = new int[3];
                        for (int i = 0; i < 3; i++)
                        {
                            final int cpart = Utils.getInteger(tab[i], -1);
                            if ((cpart > -1) && (cpart < 256))
                            {
                                color[i] = cpart;
                            }
                            else
                            {
                                color = null;
                                break;
                            }
                        }

                        if (color != null)
                        {
                            bgColor = new Color(color[0], color[1], color[2]);
                        }
                    }
                    if (bgColor == null)
                    {
                        bgColor = Color.WHITE;
                        getLog().warn("BG Color has been incorectly defined. Default WHITE color will be used instead.");
                    }
                }
            }
            else if (Sprite.COLOR_MODEL_ARGB.equals(sprite.getColorModel()))
            {
                out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                if (Utils.isNotNullNorEmpty(sprite.getBgColor()))
                {
                    final String[] tab = sprite.getBgColor().split(",");
                    if (tab.length == 4)
                    {
                        int[] color = new int[4];
                        for (int i = 0; i < 4; i++)
                        {
                            final int cpart = Utils.getInteger(tab[i], -1);
                            if ((cpart > -1) && (cpart < 256))
                            {
                                color[i] = cpart;
                            }
                            else
                            {
                                color = null;
                                break;
                            }
                        }

                        if (color != null)
                        {
                            bgColor = new Color(color[0], color[1], color[2], color[3]);
                        }
                    }
                    if (bgColor == null)
                    {
                        bgColor = new Color(255, 255, 255, 0);
                        getLog().warn("BG Color has been incorectly defined. Default WHITE color will be used instead.");
                    }
                }
            }
            else
            {
                out = null;
                getLog().error("Incorrect COLOR_MODEL has been used. Supported values: RGB, ARGB");
            }

            if (out != null && bgColor != null)
            {
                String format = null;
                if (Utils.isNotNullNorEmpty(sprite.getFormat()))
                {
                    format = sprite.getFormat();
                }
                else
                {
                    format = "png";
                    getLog().warn("Format has been incorectly defined. Default PNG format will be used instead.");
                }
                final Graphics g = out.getGraphics();
                g.setColor(bgColor);
                g.fillRect(0, 0, width, height);
                for (final Img img : bounds.keySet())
                {
                    try
                    {
                        final BufferedImage bImage = ImageIO.read(new File(img.getFile()));
                        if (bImage != null)
                        {
                            final Rectangle rect = bounds.get(img);
                            g.drawImage(bImage, rect.x, rect.y, null);
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                g.dispose();
                String spriteFileName = sprite.getName();
                try
                {
                    final File spriteOutFolder = new File(spriteOutDirectory);
                    if (!spriteOutFolder.exists())
                    {
                        Files.createDirectory(spriteOutFolder.toPath());
                    }
                    final File tempFile = new File(spriteOutDirectory + File.separatorChar + sprite.getName() + "." + format);
                    ImageIO.write(out, format, tempFile);
                    spriteFileName = DigestUtils.md5Hex(new FileInputStream(tempFile));
                    tempFile.renameTo(new File(spriteOutDirectory + File.separatorChar + spriteFileName + "." + format));

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                final StringBuffer sb = new StringBuffer();
                for (final Img img : bounds.keySet())
                {
                    final Rectangle rect = bounds.get(img);
                    sb.append(".").append(img.getCss()).append("{\n");
                    sb.append("background: url(\"../images/" + spriteFileName + "." + format + "\") no-repeat scroll ").append(rect.x > 0 ? (-1 * rect.x) : rect.x).append("px ").append(rect.y > 0 ? (-1 * rect.y) : rect.y).append("px; \n");
                    sb.append("width: ").append(rect.width).append("px;\n");
                    sb.append("height: ").append(rect.height).append("px;\n");
                    final Dimension dimension = img.getOuterContainerDimension();
                    if (dimension != null)
                    {
                        sb.append("position: relative; \n");
                        if (Img.VERTICAL_ALIGN_MIDDLE.equals(img.getVertical()))
                        {
                            sb.append("top:").append((dimension.height - rect.height) / 2).append("px;\n");
                        }
                        if (Img.HORIZONTAL_ALIGN_CENTER.equals(img.getHorizontal()))
                        {
                            sb.append("left:").append((dimension.width - rect.width) / 2).append("px;\n");
                        }
                    }
                    sb.append("}").append("\n");

                }

                try
                {
                    final File cssOutFolder = new File(cssOutDirectory);
                    if (!cssOutFolder.exists())
                    {
                        Files.createDirectory(cssOutFolder.toPath());
                    }
                    BufferedWriter writer = new BufferedWriter(new FileWriter(cssOutDirectory + File.separatorChar + sprite.getName() + ".css"));
                    writer.write(sb.toString());
                    writer.flush();
                    writer.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
