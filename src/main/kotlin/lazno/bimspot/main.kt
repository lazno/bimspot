package lazno.bimspot

import lazno.bimspot.rest.FuelRedlistClient
import lazno.bimspot.rest.JacksonDeserializer

val token = "9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee"
val mammaliaClass = "MAMMALIA"

fun main() {
    val client = FuelRedlistClient(token, JacksonDeserializer)

    val regions = client.regions()
    println("Step 1: fetched ${regions.size} regions")
    val randomRegion = randomEntryOf(regions)

    randomRegion?.let { region ->
        println("\nStep 2: random region is $region")
        val species = client.speciesBy(region = region, page = 0)
        println("\nStep 3+4: found ${species.size} species for this region")

        val endangeredSpecies = species.filter { it.category == Category.CR }
        println("\nStep 5: found ${endangeredSpecies.size} critically endangered species." )
        if (endangeredSpecies.isNotEmpty()) {
            println("fetching conservation measures and printing them")
        }
        endangeredSpecies.parallelStream()
              .map { spec -> addMeasures(spec, client) }
              .forEach { println(it) }

        val mammals = species.filter { it.class_name == mammaliaClass }
        println("\nStep 5: printing ${mammals.size} mammals")
        mammals
                .forEach{ println(it) }

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