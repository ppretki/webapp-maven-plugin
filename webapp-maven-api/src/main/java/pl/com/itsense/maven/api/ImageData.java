package pl.com.itsense.maven.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author ppretki
 *
 */
public class ImageData
{
    /** */
    private int xpos;
    /** */
    private int ypos;
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
    /** */
    private String cssClass;
    /** */
    private String className;
    /** */
    private String absolutePath;
    /**
     * 
     */
    public ImageData()
    {
    }

    /**
     * 
     * @param image
     */
    public ImageData(final ImageData image)
    {
        this.xpos = image.xpos;
        this.ypos = image.ypos;
        this.width = image.width;
        this.height = image.height;
        this.name = image.name;
        this.hashFile = image.hashFile;
        this.path = image.path;
        this.cssClass = image.cssClass;
        this.className = image.className;
    }
    
    /**
     * 
     * @param xpos
     */
    public void setXpos(final int xpos)
    {
        this.xpos = xpos;
    }
    /**
     * 
     * @param ypos
     */
    public void setYpos(final int ypos)
    {
        this.ypos = ypos;
    }
    /**
     * 
     * @return
     */
    public int getXpos()
    {
        return xpos;
    }
    /**
     * 
     * @return
     */
    public int getYpos()
    {
        return ypos;
    }
    
    /**
     * 
     * @return
     */
    public String getClassName()
    {
        return className;
    }
    
    /**
     * 
     * @param className
     */
    public void setClassName(final String className)
    {
        this.className = className;
    }
    /**
     * 
     * @return
     */
    public String getCssClass()
    {
        return cssClass;
    }
    
    /**
     * 
     * @param cssClass
     */
    public void setCssClass(final String cssClass)
    {
        this.cssClass = cssClass;
    }
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
    
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("[xpos, ypox, width, height]", "[" + xpos + "," + ypos + "," + width + "," + height + "]").append("css", cssClass).append("name", name).append("path", path).append("hashFile", hashFile).toString();
    }
}
