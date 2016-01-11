package pl.com.itsense.csssprites.impl;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import pl.com.itsense.csssprites.LayoutManager;
import pl.com.itsense.maven.api.ImageData;

/**
 * 
 * @author ppretki
 *
 */
public class SimpleVerticalLayoutManager implements LayoutManager
{
    /**
     * 
     */
    @Override
    public Map<ImageData, Rectangle> evalPositions(final ImageData[] images)
    {
        final HashMap<ImageData, Rectangle> result = new HashMap<ImageData, Rectangle>();
        Rectangle prevRect = null;
        for (final ImageData image : images)
        {
            final Rectangle rect = new Rectangle(0, 0, image.getWidth(), image.getHeight());
            if (prevRect == null)
            {
                rect.x = 0;
                rect.y = 0;
            }
            else
            {
                rect.y = prevRect.y + prevRect.height;
                rect.x = 0;
            }
            result.put(image, rect);
            prevRect = rect;
        }
        return result;
    }
}
