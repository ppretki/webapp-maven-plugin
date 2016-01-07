package pl.com.itsense.maven;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


@Mojo(name = "resourcehash", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class ResourceHash extends AbstractMojo
{

    @Parameter(property = "resources", required = true)
    private List resources;

    /**
     * 
     */
    public void execute() throws MojoExecutionException
    {
        getLog().info("ResourceHash: start executing ...");
        if (resources != null)
        {
            if (resources.size() > 0)
            {
                for (final Object obj : resources)
                {
                    getLog().info("ResourceHash: resource  = " + obj + ", obj.class = " + obj.getClass());
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
