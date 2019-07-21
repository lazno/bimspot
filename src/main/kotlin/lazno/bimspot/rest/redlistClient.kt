package lazno.bimspot.rest

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import lazno.bimspot.Region

class FuelRedlistClient(private val token: String, private val deserializer: Deserializer) {
    private val baseUrl = "https://apiv3.iucnredlist.org/api/v3"

    suspend fun regions(): List<Region> =
            Fuel.get("$baseUrl/region/list?token=$token")
                .responseObject(deserializer.regions())
                .third
                .fold<List<Region>>(
                        { return it.results },
                        { return handleError(it) }
                )

    private fun <T> handleError(err: FuelError): List<T> {
        println(err.message)
        return emptyList()
    }
}