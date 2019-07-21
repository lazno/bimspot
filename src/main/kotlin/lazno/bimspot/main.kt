package lazno.bimspot

import lazno.bimspot.rest.FuelRedlistClient
import lazno.bimspot.rest.JacksonDeserializer

val token = "9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee"

fun main() {
    val client = FuelRedlistClient(token, JacksonDeserializer)

    val randomRegion = randomEntryOf(client.regions())

    randomRegion?.let { region ->
        println("region is $region")
        val species = client.speciesBy(region = region, page = 0)
                .filter { it.category == "CR" }

        println("found ${species.size} species for this region")
        species.stream().parallel()
                .map { spec -> addMeasures(spec, client) }
                .forEach { println(it) }

    } ?: run {
        println("region could not be fetched. exiting..")
    }
}

private fun addMeasures(spec: Species, client: FuelRedlistClient): Species {
    val measures = client.conservationMeasuresBy(spec)
    return spec.copy(measures = measuresAsString(measures))
}

private fun measuresAsString(m: List<Measure>): String {
    return m.distinct()
            .joinToString(",") { it.title }
}

private fun <T> randomEntryOf(list: List<T>): T? {
    list.ifEmpty {
        return null
    }

    return list.random()
}