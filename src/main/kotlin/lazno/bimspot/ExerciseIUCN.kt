package lazno.bimspot

import lazno.bimspot.rest.RedlistClient

/**
 * class with arbitrary logic of this exercise. run method will execute steps 1 through 5
 */
class ExerciseIUCN(private val client: RedlistClient, private val category: Category, private val className: String) {

    /**
     * execute steps 1 through 5
     */
    fun run() {
        val regions = client.regions()
        println("Step 1: fetched ${regions.size} regions")
        val randomRegion = randomEntryOf(regions)

        randomRegion?.let { region ->
            println("\nStep 2: random region is $region")
            val species = client.speciesBy(region = region, page = 0)
            println("\nStep 3+4: found ${species.size} species for this region")

            val endangeredSpecies = species.filter { it.category == category }
            println("\nStep 5: found ${endangeredSpecies.size} species with category ${category.code}" )
            if (endangeredSpecies.isNotEmpty()) {
                println("fetching conservation measures and printing them with species")
            }
            endangeredSpecies.parallelStream()
                    .map { spec -> addMeasures(spec, client) }
                    .forEach { println(it) }

            val mammals = species.filter { it.class_name == className }
            println("\nStep 5: printing ${mammals.size} mammals")
            mammals
                    .forEach{ println(it) }

        }
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