package lazno.bimspot

import lazno.bimspot.rest.FuelRedlistClient
import lazno.bimspot.rest.JacksonDeserializer
import lazno.bimspot.rest.RedlistClient

const val token = "9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee"
const val mammaliaClass = "MAMMALIA"

fun main() {
    val client: RedlistClient = FuelRedlistClient(token, JacksonDeserializer)
    val exercise = ExerciseIUCN(client, Category.CR, mammaliaClass)

    exercise.run()
}

