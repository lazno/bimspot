import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.ResponseDeserializable

data class Regions(val count: Int, val results: List<Region>)
data class Region(val name: String, val identifier: String)

private object RegionsDeserializable : ResponseDeserializable<Regions> {
    override fun deserialize(content: String) =
            jacksonObjectMapper().readValue<Regions>(content)
}

val token = "9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee"

fun main() {
    Fuel.get("https://apiv3.iucnredlist.org/api/v3/region/list?token=$token")
            .responseObject(RegionsDeserializable)
            .third
            .fold(
                    { regions ->
                        regions.results
                                .forEach { region ->
                                    println(region)
                                }
                    },
                    { error ->
                        handleError(error)
                    }
            )
}

private fun handleError(err: FuelError) {
    println(err.message)
}