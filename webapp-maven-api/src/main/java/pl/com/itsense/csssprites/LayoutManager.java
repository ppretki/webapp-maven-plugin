package pl.com.itsense.csssprites;

import java.awt.Rectangle;
import java.util.Map;

import pl.com.itsense.maven.api.ImageData;


/**
 * 
 * @author ppretki
 *
 */
interface LayoutManager
{
    Map<ImageData, Rectangle> eval(final Map<ImageData, Rectangle> images);
}
