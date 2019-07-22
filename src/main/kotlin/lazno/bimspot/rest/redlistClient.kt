package lazno.bimspot.rest

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import lazno.bimspot.Measure
import lazno.bimspot.Region
import lazno.bimspot.Species

/**
 * implementation of the IUCN Redlist API using Fuel
 */
class FuelRedlistClient(private val token: String, private val deserializer: Deserializer) {
    private val baseUrl = "https://apiv3.iucnredlist.org/api/v3"
    private val speciesByRegionUrl: String = "$baseUrl/species/region/%s/page/%d?token=$token"
    private val conservationBySpeciesUrl: String = "$baseUrl/measures/species/name/%s?token=$token"

    private val globalRegion = "global"

    /**
     * fetch all regions
     */
    fun regions(): List<Region> =
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
    fun speciesBy(region: Region, page: Int): List<Species> {
        val url = speciesByRegionUrl.format(region.identifier, page)
        return Fuel.get(url)
                .responseObject(deserializer.speciesInRegion())
                .third
                .fold<List<Species>>(
                        {return it.result},
                        {return handleError(it)}
                )
    }

    /**
     * fetch conservation measures for a species
     */
    fun conservationMeasuresBy(species: Species): List<Measure> {
        val url = conservationBySpeciesUrl.format(species.scientific_name)
        return Fuel.get(url)
                .responseObject(deserializer.measureBySpecies())
                .third
                .fold<List<Measure>>(
                        {return it.result ?: emptyList()},
                        {return handleError(it)}
                )
    }

    private fun <T> handleError(err: FuelError): List<T> {
        println(err.message)
        return emptyList()
    }
}