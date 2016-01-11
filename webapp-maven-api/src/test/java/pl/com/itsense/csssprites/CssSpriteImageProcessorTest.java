package pl.com.itsense.csssprites;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.com.itsense.maven.api.CssSpriteData;
import pl.com.itsense.maven.api.ImageData;

public class CssSpriteImageProcessorTest
{
    /** */
    private final Logger log = LoggerFactory.getLogger(CssSpriteImageProcessor.class);
    @Test
    public void testProcess()
    {
        final String[] icons = new String[]{
            "TINY_LOVE.jpg",
            "TOLO.jpg",
            "TOMMEE_TIPPEE.jpg",
            "TOTSEAT.jpg",
            "TREPPY.jpg",
            "TROLL.jpg",
            "VOKSI.jpg",
            //"YEPZON.jpg"
        };
        final File input = new File(getClass().getClassLoader().getResource("icons").getFile());
        final ArrayList<String> fileNames = new ArrayList<String>();
        for (final String icon : icons)
        {
            final String filePath = new File(input,icon).toPath().toString();
            fileNames.add(filePath);
            log.debug(filePath);
        }
        
        final CssSpriteData spriteData = CssSpriteImageProcessor.process(fileNames, input);
        
        
        
        spriteData.setName("TestCssSprite");
        log.debug("spriteData = " + spriteData);
        for (final ImageData image : spriteData.getImages())
        {
            log.debug("image = " + image);
            image.setName("Test-icon");
        }
        log.debug(WebAppUtils.getStylesheetClass(spriteData,"https://www.test.fi/verkkokauppa/images/"));
    }
}
