package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.Model;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ModelDeserializer implements JsonDeserializer<Model> {
    @Override
    public Model deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject modelAsObject = jsonElement.getAsJsonObject();
        return new Model(modelAsObject.get("name").getAsString(), modelAsObject.get("type").getAsString(), modelAsObject.get("size").getAsInt());
    }
}
