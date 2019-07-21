package lazno.bimspot

import kotlinx.coroutines.runBlocking
import lazno.bimspot.rest.FuelRedlistClient
import lazno.bimspot.rest.JacksonDeserializer

val token = "9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee"

fun main() {
    val client = FuelRedlistClient(token, JacksonDeserializer)
    runBlocking {

        val randomRegion = randomEntryOf(client.regions())

        randomRegion?.let {region ->
            println( "region is $region")
            client.speciesBy(region = region, page = 0)
                    .forEach{println(it)}
        } ?: run {
            println("region could not be fetched. exiting..")
        }
    }
}

private fun <T> randomEntryOf(list: List<T>): T? {
    list.ifEmpty {
        return null
    }

    return list.random()
}