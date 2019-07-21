package lazno.bimspot

data class Regions(val count: Int, val results: List<Region>)
data class Region(val name: String, val identifier: String)

data class SpeciesInRegion(val count: Int, val region_identifier: String, val page: Int, val result: List<Species>)
data class Species(val taxonid: Int, val kingdom_name: String, val phylum_name: String, val class_name: String,
                   val order_name: String, val family_name: String, val genus_name: String, val scientific_name: String,
                   val infra_rank: String?, val infra_name: String?, val population: String?, val subpopulation:
                   String?, val category: String, val measures: String?)

data class MeasuresBySpecies(val name: String, val result: List<Measure>)
data class Measure(val code: String, val title: String)
