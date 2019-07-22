package lazno.bimspot

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import lazno.bimspot.rest.FuelRedlistClient
import lazno.bimspot.rest.JacksonDeserializer
import lazno.bimspot.rest.RedlistClient

const val defaultToken = "9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee"
const val defaultTimeout = 2000
const val mammaliaClass = "MAMMALIA"

fun main(args: Array<String>) = Starter().main(args)

class Starter: CliktCommand() {
    val token: String by option(help = "Token for IUCN Red List API (default token used if omitted)")
            .default(defaultToken)
    val timeout: Int by option(help = "Timout in millis for single rest requests (default = 2000 ms)")
            .int().default(defaultTimeout)

    override fun run() {
        val client: RedlistClient = FuelRedlistClient(token = token, timeout = timeout, deserializer =
        JacksonDeserializer)
        val exercise = ExerciseIUCN(client = client, category = Category.CR, className = mammaliaClass)

        exercise.run()
    }
}

