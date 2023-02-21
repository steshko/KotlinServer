import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    val server = embeddedServer(Netty, port = 8080) {
        // Ktor application configuration goes here
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }
        }
    }
    server.start(wait = true)
}