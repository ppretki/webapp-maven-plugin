package pl.com.itsense.maven.api;

import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
/**
 * 
 * @author ppretki
 *
 */
public class ImageDataAdapter implements JsonSerializer<ImageData>, JsonDeserializer<ImageData>
{
    /**
	 * 
	 */
    public JsonElement serialize(final ImageData imageData, final Type typeOfSrc, final JsonSerializationContext context)
    {
        final JsonObject result = new JsonObject();
        result.add("width", new JsonPrimitive(imageData.getWidth()));
        result.add("height", new JsonPrimitive(imageData.getHeight()));
        result.add("name", new JsonPrimitive(StringUtils.defaultString(imageData.getName())));
        result.add("path", new JsonPrimitive(StringUtils.defaultString(imageData.getPath())));
        result.add("hashfile", new JsonPrimitive(StringUtils.defaultString(imageData.getHashFile())));
        result.add("cssClass", new JsonPrimitive(StringUtils.defaultString(imageData.getCssClass())));
        result.add("className", new JsonPrimitive(StringUtils.defaultString(imageData.getClassName())));
        return result;
    }
    /**
     * 
     */
    public ImageData deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (json instanceof JsonObject)
        {
            final ImageData imageData = new ImageData();
            imageData.setWidth(((JsonObject) json).get("width").getAsInt());
            imageData.setHeight(((JsonObject) json).get("height").getAsInt());
            imageData.setName(((JsonObject) json).get("name").getAsString());
            imageData.setPath(((JsonObject) json).get("path").getAsString());
            imageData.setHashFile(((JsonObject) json).get("hashfile").getAsString());
            imageData.setCssClass(((JsonObject) json).get("cssClass").getAsString());
            imageData.setClassName(((JsonObject) json).get("className").getAsString());
            return imageData;
        }
        return null;
    }

}
