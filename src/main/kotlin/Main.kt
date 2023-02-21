
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main() {
    val db = DataBase()
    val conn = db.getConnection()

    //db.createDataTable()

    val server = embeddedServer(Netty, port = 8080) {
        install(CORS) {
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Get)
            allowHeader(HttpHeaders.AccessControlAllowOrigin)
            allowHeader(HttpHeaders.ContentType)
            anyHost()
        }
        
        routing {
            post("/register") {
                val registerData = call.receive<String>()
                val regObj = Json.decodeFromString<User>(registerData)

                db.insertUser(regObj)
                call.respond(HttpStatusCode.OK)
            }
            post("/auth") {
                val userData = call.receive<String>()
                val userObj = Json.decodeFromString<User>(userData)

                val auth: Boolean = db.authenticateUser(username = userObj.username, password = userObj.password)
                @Serializable
                data class AuthResponse(val auth: Boolean, val notes: MutableList<Data> = mutableListOf())
                if (auth) {
                    val dataResponse = AuthResponse(true, db.getData(userObj.username))

                    call.response.header("Content-Type", "application/json")
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = Json.encodeToString(dataResponse)
                    )
                } else {
                    call.response.header("Content-Type", "application/json")
                    call.respond(status = HttpStatusCode.OK, message = Json.encodeToString(AuthResponse(false)))
                }
            }
            post("/insert") {
                val data = call.receive<String>()
                val dataObj = Json.decodeFromString<Data>(data)
                db.insertData(dataObj)
                call.respond(HttpStatusCode.OK)

            }
            post("/update") {
                val data = call.receive<String>()
                val dataObj = Json.decodeFromString<Data>(data)

                db.updateData(dataObj)
                call.respond(HttpStatusCode.OK)
            }
            post("/delete") {
                val id = Json.decodeFromString<String>(call.receive())
                db.deleteData(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
    server.start(wait = true)
}

