package pl.com.itsense.maven.api;

import java.lang.reflect.Type;
import java.util.List;

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
public class CssSpriteDataAdapter implements JsonSerializer<CssSpriteData>, JsonDeserializer<CssSpriteData>
{
    /**
	 * 
	 */
    public JsonElement serialize(final CssSpriteData spriteData, final Type typeOfSrc, final JsonSerializationContext context)
    {
        final JsonObject result = new JsonObject();
        result.add("images", context.serialize(spriteData.getImages()));
        result.add("width", new JsonPrimitive(spriteData.getWidth()));
        result.add("height", new JsonPrimitive(spriteData.getHeight()));
        result.add("name", new JsonPrimitive(StringUtils.defaultString(spriteData.getName())));
        result.add("path", new JsonPrimitive(StringUtils.defaultString(spriteData.getPath())));
        result.add("hashfile", new JsonPrimitive(StringUtils.defaultString(spriteData.getHashFile())));
        result.add("cssClass", new JsonPrimitive(StringUtils.defaultString(spriteData.getCssClass())));
        return result;
    }
    /**
     * 
     */
    public CssSpriteData deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (json instanceof JsonObject)
        {
            final CssSpriteData imageData = new CssSpriteData();
            imageData.setWidth(((JsonObject) json).get("width").getAsInt());
            imageData.setHeight(((JsonObject) json).get("height").getAsInt());
            imageData.setName(((JsonObject) json).get("name").getAsString());
            imageData.setPath(((JsonObject) json).get("path").getAsString());
            imageData.setHashFile(((JsonObject) json).get("hashfile").getAsString());
            imageData.setCssClass(((JsonObject) json).get("cssClass").getAsString());
            return imageData;
        }
        return null;
    }

}
