import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class Data(val id: String = UUID.randomUUID().toString(), val username: String, var title:String, var note: String, var editDate: String) {
}