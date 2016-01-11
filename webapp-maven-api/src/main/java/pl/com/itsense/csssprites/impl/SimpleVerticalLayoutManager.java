package pl.com.itsense.csssprites.impl;

import java.awt.Rectangle;
import java.util.Map;

import pl.com.itsense.csssprites.LayoutManager;
import pl.com.itsense.webmodel.Img;

/**
 * 
 * @author ppretki
 *
 */
public class SimpleVerticalLayoutManager extends LayoutManager
{
    /**
     * 
     */
    @Override
    public Map<Img, Rectangle> eval(final Map<Img, Rectangle> imgs)
    {
        Rectangle prevRect = null;
        for (final Img img : imgs.keySet())
        {
            final Rectangle rect = imgs.get(img);
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
            prevRect = rect;
        }
        return imgs;
    }
}
