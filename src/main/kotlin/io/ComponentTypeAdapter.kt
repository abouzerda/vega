package io

import com.google.gson.*
import component.Component
import java.lang.reflect.Type

object ComponentTypeAdapter : JsonSerializer<Component>, JsonDeserializer<Component> {
    override fun serialize(src: Component?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonObject().apply {
            add("type", JsonPrimitive(src?.javaClass?.canonicalName))
            add("properties", context?.serialize(src, src?.javaClass))
        }
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Component {
        val jsonObject = json?.asJsonObject
        val type = jsonObject?.get("type")?.asString
        val properties = jsonObject?.get("properties")
        return context?.deserialize(properties, Class.forName(type))!!
    }

}