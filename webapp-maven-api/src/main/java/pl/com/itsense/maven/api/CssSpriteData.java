package pl.com.itsense.maven.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author ppretki
 *
 */
public class CssSpriteData extends ImageData
{
    /** */
    private ImageData[] images = new ImageData[0];

    /**
     * 
     * @return
     */
    public ImageData[] getImages()
    {
        return images;
    }
    
    /**
     * 
     * @param images
     */
    public void setImages(final ImageData[] images)
    {
        this.images = images;
    }
    
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("#images", images != null ? images.length : 0).toString();
    }
}
