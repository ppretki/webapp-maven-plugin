package pl.com.itsense.maven;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.AnnotationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import pl.com.itsense.csssprites.WebAppUtils;
import pl.com.itsense.maven.api.CssSprite;
import pl.com.itsense.maven.api.CssSpriteData;
import pl.com.itsense.maven.api.GSonConverter;
import pl.com.itsense.maven.api.Image;
import pl.com.itsense.maven.api.ImageData;
import pl.com.itsense.maven.api.StylesheetData;


@Mojo(name = "resourcehash", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class ResourceHash extends AbstractMojo
{
    @Parameter(required = true)
    private List images;
    
    @Parameter(defaultValue = "${basedir}/src/main/webapp", required = false)
    private File warSourceDirectory;

    @Parameter(defaultValue = "${project.build.directory}/webapp-maven-temp", required = false)
    private File targetDirectory;

    @Parameter(defaultValue = "${project.build.directory}/webapp-maven-temp/resources", required = false)
    private File resourcesDirectory;

    
    @Component
    private MavenProject project;
    
    /**
     * 
     * @param scripts
     * @param links
     * @return
     */
    private boolean createDirectory(final File directory)
    {
        boolean result = false;
        if (!directory.exists())
        {
            try
            {
                Files.createDirectory(directory.toPath());
                result = true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 
     * @return
     * @throws MojoExecutionException
     */
    private ClassLoader getCompileClassLoader() throws MojoExecutionException 
    {
        createDirectory(targetDirectory);
        createDirectory(resourcesDirectory);
        final List<String> classpathElements = new ArrayList<String>();
        try 
        {
            classpathElements.addAll(project.getCompileClasspathElements());
            List<URL> projectClasspathList = new ArrayList<URL>();
            for (final String element : classpathElements) 
            {
                try 
                {
                    getLog().info("element: " + element);
                    projectClasspathList.add(new File(element).toURI().toURL());
                } 
                catch (MalformedURLException e) 
                {
                    throw new MojoExecutionException(element + " is an invalid classpath element", e);
                }
            }
            return new URLClassLoader(projectClasspathList.toArray(new URL[0]), getClass().getClassLoader());
        } 
        catch (final DependencyResolutionRequiredException e) 
        {
            throw new MojoExecutionException("Dependency resolution failed");
        }
    }
    
    private List<ImageData> imagesProcessing(final ClassLoader loader)
    {
        final ArrayList<ImageData> imageDataList = new ArrayList<ImageData>();
        if (images != null)
        {
            if (images.size() > 0)
            {
                final ArrayList<StylesheetData> stylesheets = new ArrayList<StylesheetData>();
                final ArrayList<CssSpriteData> sprites = new ArrayList<CssSpriteData>();
                for (final Object obj : images)
                {
                    final StringBuilder cssClasses = new StringBuilder();
                    if (obj instanceof String)
                    {
                        final String className = (String) obj;
                        try
                        {
                            final Class clazz = Class.forName(className, true, loader);
                            final Annotation annotation = clazz.getAnnotation(CssSprite.class);
                            CssSpriteData spriteData = null;
                            if (annotation instanceof CssSprite)
                            {
                                final CssSprite sprite = (CssSprite) annotation;
                                spriteData = new CssSpriteData();
                                spriteData.setClassName(className);
                                getLog().info("Sprite Definition Found: " + spriteData + " <=> " + spriteData);
                            }
                            final ArrayList<ImageData> spriteImages = new ArrayList<ImageData>();
                            for (final Field field : clazz.getDeclaredFields())
                            {
                                final Image image = field.getAnnotation(Image.class);
                                if (image !=  null)
                                {
                                    final File imageFile = new File(warSourceDirectory, image.path());
                                    getLog().info("imageFile " + imageFile.getPath());
                                    try
                                    {
                                        final String ext = FilenameUtils.getExtension(imageFile.getName());
                                        final String hash = DigestUtils.md5Hex(new FileInputStream(imageFile));
                                        final ImageData imageData = WebAppUtils.getImageData(imageFile);
                                        imageData.setName(field.getName());
                                        imageData.setPath(image.path());
                                        imageData.setClassName(clazz.getName());
                                        if (StringUtils.isNotBlank(ext))
                                        {
                                            imageData.setHashFile(hash + "." + ext);
                                        }
                                        else
                                        {
                                            imageData.setHashFile( hash);
                                        }
                                        imageDataList.add(imageData);
                                        Files.copy(imageFile.toPath(), new File(resourcesDirectory, imageData.getHashFile()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                                        // CSS PROCESSING
                                        if (image.css())
                                        {
                                            imageData.setCssClass(appendStylesheetClass(cssClasses, clazz, imageData));
                                        }
                                        else
                                        {
                                            imageData.setCssClass(StringUtils.EMPTY);
                                        }
                                        
                                        if (image.sprite() && spriteData != null)
                                        {
                                            spriteImages.add(imageData);
                                        }
                                        getLog().info(imageFile.getPath() + " <=> " + imageData);
                                    }
                                    catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if (spriteData != null &&  spriteImages.size() > 0)
                            {
                                spriteData.setImages(spriteImages.toArray(new ImageData[0]));
                                sprites.add(spriteData);
                                getLog().info("Sprite Added: " + spriteData);
                            }
                            if (cssClasses.length() > 0)
                            {
                                final String hash = DigestUtils.md5Hex(cssClasses.toString());
                                try
                                {
                                    final Path path = new File(resourcesDirectory, hash + ".css").toPath();
                                    Files.write(path, cssClasses.toString().getBytes());
                                    
                                    final StylesheetData stylesheetData = new StylesheetData();
                                    stylesheetData.setClassName(className);
                                    stylesheetData.setHashFile(hash);
                                    stylesheets.add(stylesheetData);
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }  
                            }
                        }
                        catch (ClassNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                
                
                final String jsonImages = imageDataList.size() > 0 ? GSonConverter.getGson().toJson(imageDataList) : StringUtils.EMPTY;
                final String jsonStylesheet = stylesheets.size() > 0 ? GSonConverter.getGson().toJson(stylesheets): StringUtils.EMPTY;
                final String jsonSprites = sprites.size() > 0 ? GSonConverter.getGson().toJson(sprites): StringUtils.EMPTY;
                try
                {
                    if (StringUtils.isNotBlank(jsonImages)) Files.write(new File(targetDirectory, "images.json").toPath(), jsonImages.getBytes());
                    if (StringUtils.isNotBlank(jsonStylesheet)) Files.write(new File(targetDirectory, "stylesheets.json").toPath(), jsonStylesheet.getBytes());
                    if (StringUtils.isNotBlank(jsonSprites)) Files.write(new File(targetDirectory, "sprites.json").toPath(), jsonSprites.getBytes());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }  
            }
            else
            {
                getLog().error("Images have not been specified");
            }
        }
        else
        {
            getLog().error("images have not been specified");
        } 
        return imageDataList;
    }
    /**
     * 
     */
    public void execute() throws MojoExecutionException
    {
        final ClassLoader loader = getCompileClassLoader();
        getLog().info("ResourceHash: start executing ...");
        imagesProcessing(loader);
        getLog().info("ResourceHash: stop executing");
    }
    /**
     * 
     * @return
     */
    private static String appendStylesheetClass(final StringBuilder buffer, final Class clazz, final ImageData imageData)
    {
        final String name = clazz.getName().replaceAll("\\.", "-") + "-" + imageData.getName();
        buffer.append(".").append(name).append("{\n");
        buffer.append("background-image: url(\"").append(imageData.getHashFile()).append("\");\n");
        buffer.append("background-repeat: no-repeat;\n");
        buffer.append("width:").append(imageData.getWidth()).append("px;\n");
        buffer.append("height:").append(imageData.getHeight()).append("px;\n");
        buffer.append("}\n");
        return name;
    }
}
