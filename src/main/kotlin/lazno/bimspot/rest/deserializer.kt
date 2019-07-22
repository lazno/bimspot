package lazno.bimspot.rest

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.core.ResponseDeserializable
import lazno.bimspot.MeasuresBySpecies
import lazno.bimspot.Regions
import lazno.bimspot.SpeciesInRegion

/**
 * Library of Deserializers for the IUCN Redlist API
 */
interface Deserializer {
    fun regions(): ResponseDeserializable<Regions>
    fun speciesInRegion(): ResponseDeserializable<SpeciesInRegion>
    fun measureBySpecies(): ResponseDeserializable<MeasuresBySpecies>
}

/**
 * implementation using jackson
 *
 * deserializers will return the deserialized result or an empty result if mapping failed
 */
object JacksonDeserializer : Deserializer {
    override fun regions(): ResponseDeserializable<Regions> =  RegionsDeserializable

    override fun speciesInRegion(): ResponseDeserializable<SpeciesInRegion> = SpeciesDeserializable

    override fun measureBySpecies(): ResponseDeserializable<MeasuresBySpecies> = MeasureDeserializable


    private object RegionsDeserializable : ResponseDeserializable<Regions> {
        override fun deserialize(content: String): Regions =
                tryOrElse(
                        { jacksonObjectMapper().readValue(content) },
                        Regions(count = 0, results = emptyList())
                )
    }

    private object SpeciesDeserializable : ResponseDeserializable<SpeciesInRegion> {
        override fun deserialize(content: String) =
                tryOrElse(
                        {jacksonObjectMapper().readValue(content)},
                        SpeciesInRegion(count = 0, region_identifier = "", page = 0, result = emptyList())
                )
    }

    private object MeasureDeserializable: ResponseDeserializable<MeasuresBySpecies> {
        override fun deserialize(content: String) =
                tryOrElse(
                        {jacksonObjectMapper().readValue<MeasuresBySpecies>(content)},
                        MeasuresBySpecies(name = "", result = emptyList(), value = "", species = "")
                )
    }

    /**
     * wrap jackson call. when exception is caught, return "orElse"
     */
    private fun <T> tryOrElse(fn: () -> T, orElse: T): T {
        return try {
            fn()
        } catch (e: JsonMappingException) {
            return orElse
        }
    }

}
