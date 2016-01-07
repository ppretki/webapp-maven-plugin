package pl.com.itsense.maven.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author P.Pretki
 * 
 */
public abstract class GSonConverter
{
    /** */
    private final static Gson gson = (new GsonBuilder()).registerTypeAdapter(ImageData.class, new ImageDataAdapter()).setPrettyPrinting().create();

    /**
     * 
     * @return
     */
    public static Gson getGson()
    {
        return gson;
    }

}
