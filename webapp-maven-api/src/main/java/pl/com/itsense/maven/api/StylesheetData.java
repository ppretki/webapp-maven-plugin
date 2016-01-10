package pl.com.itsense.maven.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author ppretki
 *
 */
public class StylesheetData
{
    /** */
    private String hashFile;
    /** */
    private String className;
    
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
    public String getHashFile()
    {
        return hashFile;
    }
    /**
     * 
     * @param hashFile
     */
    public void setHashFile(final String hashFile)
    {
        this.hashFile = hashFile;
    }
    
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("hashFile", hashFile).append("hashFile", hashFile).toString();
    }
}
