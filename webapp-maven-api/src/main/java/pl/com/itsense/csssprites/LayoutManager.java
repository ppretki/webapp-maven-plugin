package pl.com.itsense.csssprites;

import java.awt.Rectangle;
import java.util.Map;

import pl.com.itsense.maven.api.ImageData;


/**
 * 
 * @author ppretki
 *
 */
public interface LayoutManager
{
    Map<ImageData, Rectangle> evalPositions(final ImageData[] images);
}
