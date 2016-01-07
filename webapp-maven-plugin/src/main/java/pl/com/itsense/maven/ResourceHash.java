package pl.com.itsense.maven;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import pl.com.itsense.maven.api.GSonConverter;
import pl.com.itsense.maven.api.Image;
import pl.com.itsense.maven.api.ImageData;


@Mojo(name = "resourcehash", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class ResourceHash extends AbstractMojo
{
    @Parameter(property = "resources", required = true)
    private List resources;
    
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
    
    /**
     * 
     */
    public void execute() throws MojoExecutionException
    {
        final ClassLoader loader = getCompileClassLoader();
        getLog().info("ResourceHash: start executing ...");
        if (resources != null)
        {
            if (resources.size() > 0)
            {
                final ArrayList<ImageData> images = new ArrayList<ImageData>();
                for (final Object obj : resources)
                {
                    if (obj instanceof String)
                    {
                        final String className = (String) obj;
                        try
                        {
                            final Class clazz = Class.forName(className, true, loader);
                            for (final Field field : clazz.getDeclaredFields())
                            {
                                final Image image = field.getAnnotation(Image.class);
                                if (image !=  null)
                                {
                                    final File imageFile = new File(warSourceDirectory, image.path());
                                    getLog().info("imageFile " + imageFile.getPath());
                                    try
                                    {
                                        final BufferedImage bufferedImage = ImageIO.read(imageFile);
                                        final ImageData imageData = new ImageData();
                                        imageData.setHeight(bufferedImage.getHeight());
                                        imageData.setWidth(bufferedImage.getWidth());
                                        imageData.setName(field.getName());
                                        imageData.setPath(image.path());
                                        imageData.setHashFile(DigestUtils.md5Hex(new FileInputStream(imageFile)));
                                        images.add(imageData);
                                        Files.copy(imageFile.toPath(), new File(resourcesDirectory, imageData.getHashFile()).toPath());
                                        bufferedImage.flush();
                                    }
                                    catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        catch (ClassNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                final String jsonImages = GSonConverter.getGson().toJson(images);
                try
                {
                    Files.write(new File(targetDirectory, "images.json").toPath(), jsonImages.getBytes());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }  
            }
            else
            {
                getLog().error("No resources have been specified");
            }
        }
        else
        {
            getLog().error("Resources have not been specified");
        }
        getLog().info("ResourceHash: stop executing");
    }
}
