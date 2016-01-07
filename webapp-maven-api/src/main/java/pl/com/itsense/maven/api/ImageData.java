package pl.com.itsense.maven.api;
/**
 * 
 * @author ppretki
 *
 */
public class ImageData
{
    /** */
    private int height;
    /** */
    private int width;
    /** */
    private String name;
    /** */
    private String hashFile;
    /** */
    private String path;
    /**
     * 
     * @return
     */
    public String getPath()
    {
        return path;
    }
    /**
     * 
     * @param path
     */
    public void setPath(final String path)
    {
        this.path = path;
    }
    /**
     * @return the height
     */
    public int getHeight()
    {
        return height;
    }
    /**
     * @param height the height to set
     */
    public void setHeight(final int height)
    {
        this.height = height;
    }
    /**
     * @return the width
     */
    public int getWidth()
    {
        return width;
    }
    /**
     * @param width the width to set
     */
    public void setWidth(final int width)
    {
        this.width = width;
    }
    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }
    /**
     * @return the hashFile
     */
    public String getHashFile()
    {
        return hashFile;
    }
    /**
     * @param hashFile the hashFile to set
     */
    public void setHashFile(final String hashFile)
    {
        this.hashFile = hashFile;
    }
    
    
}