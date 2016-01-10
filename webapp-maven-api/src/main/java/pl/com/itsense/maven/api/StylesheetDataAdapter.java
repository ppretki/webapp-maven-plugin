package pl.com.itsense.maven.api;

import java.lang.reflect.Type;

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
public class StylesheetDataAdapter implements JsonSerializer<StylesheetData>, JsonDeserializer<StylesheetData>
{
    /**
	 * 
	 */
    public JsonElement serialize(final StylesheetData stylesheetData, final Type typeOfSrc, final JsonSerializationContext context)
    {
        final JsonObject result = new JsonObject();
        result.add("className", new JsonPrimitive(stylesheetData.getClassName()));
        result.add("hashfile", new JsonPrimitive(stylesheetData.getHashFile()));
        return result;
    }
    /**
     * 
     */
    public StylesheetData deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
    {
        if (json instanceof JsonObject)
        {
            final StylesheetData stylesheetData = new StylesheetData();
            stylesheetData.setHashFile(((JsonObject) json).get("hashfile").getAsString());
            stylesheetData.setClassName(((JsonObject) json).get("className").getAsString());
            return stylesheetData;
        }
        return null;
    }

}
