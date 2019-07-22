package lazno.bimspot

import lazno.bimspot.rest.RedlistClient
import java.util.stream.Stream

/**
 * class with arbitrary logic of this exercise. run method will execute steps 1 through 5
 */
class ExerciseIUCN(private val client: RedlistClient, private val category: Category, private val className: String) {

    /**
     * execute steps 1 through 5
     */
    fun run() {
        val randomRegion = randomRegion()
        randomRegion?.let { region ->
            val species = fetchSpecies(region)

            val endangeredSpecies = filterCriticalEndangered(species)
            fetchMeasures(endangeredSpecies)
                    .forEach { println(it) }

            filterMammals(species)
                    .forEach { println(it) }
        }
    }

    private fun randomRegion(): Region? {
        val regions = client.regions()
        println("Step 1: fetching regions")
        val entry = randomEntryOf(regions)
        entry?.let { reg -> println("\nStep 2: total of ${regions.size} regions. random region is $reg") }
                ?: run {
                    println("Step 2: total of ${regions.size} regions.")
                }
        return entry
    }

    private fun fetchSpecies(region: Region): List<Species> {
        val species = client.speciesBy(region = region, page = 0)
        println("\nStep 3+4: found ${species.size} species for this region")
        return species
    }

    private fun filterCriticalEndangered(species: List<Species>): List<Species> {
        val endangeredSpecies = species.filter { it.category == category }
        println("\nStep 5: found ${endangeredSpecies.size} species with category ${category.code}")
        return endangeredSpecies
    }

    private fun fetchMeasures(species: List<Species>): Stream<Species> {
        if (species.isNotEmpty()) {
            println("fetch conservation measures and print them with species")
        }
        return species.parallelStream()
                .map { spec -> addMeasures(spec, client) }
    }

    private fun filterMammals(species: List<Species>): List<Species> {
        val mammals = species.filter { it.class_name == className }
        if (mammals.isNotEmpty()) {
            println("\nStep 6: printing ${mammals.size} mammals")
        } else {
            println("\nStep 6: no mammals where found")
        }
        return mammals
    }

    private fun addMeasures(spec: Species, client: RedlistClient): Species {
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
}