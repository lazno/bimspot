package lazno.bimspot

import kotlinx.coroutines.runBlocking
import lazno.bimspot.rest.FuelRedlistClient
import lazno.bimspot.rest.JacksonDeserializer

val token = "9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee"

fun main() {
    val client = FuelRedlistClient(token, JacksonDeserializer)
    runBlocking {
        client.regions()
                .forEach { println(it) }
    }
}