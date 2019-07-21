package lazno.bimspot.rest

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import lazno.bimspot.Region
import lazno.bimspot.Species

/**
 * implementation of the IUCN Redlist API using Fuel
 */
class FuelRedlistClient(private val token: String, private val deserializer: Deserializer) {
    private val baseUrl = "https://apiv3.iucnredlist.org/api/v3"
    private val speciesByRegionUrl: String = "$baseUrl/species/region/%s/page/%d?token=$token"
    private val globalRegion = "global"

    /**
     * fetch all regions
     */
    suspend fun regions(): List<Region> =
            Fuel.get("$baseUrl/region/list?token=$token")
                .responseObject(deserializer.regions())
                .third
                .fold<List<Region>>(
                        { return it.results.filter { region -> region.identifier != globalRegion } },
                        { return handleError(it) }
                )

    /**
     * fetch species on page x in region y
     */
    suspend fun speciesBy(region: Region, page: Int): List<Species> {
        val url = speciesByRegionUrl.format(region.identifier, page)
        return Fuel.get(url)
                .responseObject(deserializer.speciesInRegion())
                .third
                .fold<List<Species>>(
                        {return it.result},
                        {return handleError(it)}
                )
    }

    private fun <T> handleError(err: FuelError): List<T> {
        println(err.message)
        return emptyList()
    }
}