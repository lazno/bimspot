package lazno.bimspot

data class Regions(val count: Int, val results: List<Region>)
data class Region(val name: String, val identifier: String)