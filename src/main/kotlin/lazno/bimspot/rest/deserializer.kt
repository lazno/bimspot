package lazno.bimspot.rest

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.core.ResponseDeserializable
import lazno.bimspot.Regions

interface Deserializer {
    fun regions(): ResponseDeserializable<Regions>
}

object JacksonDeserializer: Deserializer {
    override fun regions(): ResponseDeserializable<Regions> {
        return RegionsDeserializable
    }

    private object RegionsDeserializable : ResponseDeserializable<Regions> {
        override fun deserialize(content: String): Regions {
            try {
                return jacksonObjectMapper().readValue(content)
            } catch (e: JsonMappingException) {
                return Regions(count = 0, results = emptyList())
            }
        }
    }
}