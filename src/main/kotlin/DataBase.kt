
import java.sql.Connection
import java.sql.DriverManager

class DataBase {
    private val url = "jdbc:sqlite:C:\\Users\\Daniel S\\Desktop\\projectdb.db"
    private var conn: Connection? = null

    private val createUserTableStatement = """
        CREATE TABLE IF NOT EXISTS users (
            username TEXT PRIMARY KEY,
            password TEXT NOT NULL,
            email TEXT NOT NULL
        );
    """.trimIndent()

    private val createDataTableStatement = """
        CREATE TABLE IF NOT EXISTS data (
            id TEXT PRIMARY KEY,
            username TEXT NOT NULL,
            title TEXT NOT NULL,
            note TEXT NOT NULL,
            edit_date TEXT NOT NULL,
            FOREIGN KEY (username) REFERENCES users(username)
        );
    """.trimIndent()

    private fun createUserTable() {
        conn?.createStatement()?.execute(createUserTableStatement)
    }

    private fun createDataTable() {
        conn?.createStatement()?.execute(createDataTableStatement)
    }

    init {
        try {
            Class.forName("org.sqlite.JDBC")
            conn = DriverManager.getConnection(url)
            println("Connection to SQLite has been established.")
        } catch (e: Exception) {
            println(e.message)
        }

        createUserTable()
        createDataTable()
    }

    fun getConnection(): Connection? {
        return conn
    }

    fun insertUser(user: User) {
        conn?.prepareStatement("INSERT INTO users(username, password, email) VALUES (?, ?, ?)").use { statement ->
            statement?.setString(1, user.username)
            statement?.setString(2, user.password)
            statement?.setString(3, user.email)

            statement?.executeUpdate()
        }
    }

    fun authenticateUser(username: String, password: String): Boolean {
        conn?.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?;").use { statement ->
            statement?.setString(1, username)
            statement?.setString(2, password)
            val result = statement?.executeQuery()

            if (result?.next() == true) {
                return true
            }
        }
        return false
    }

    fun insertData(data: Data) {
        val insertStatement = """
            INSERT INTO data (id, username, title, note, edit_date)
            VALUES (?, ?, ?, ?, ?)
        """.trimIndent()

        conn?.prepareStatement(insertStatement)?.use { statement ->
            statement.setString(1, data.id)
            statement.setString(2, data.username)
            statement.setString(3, data.title)
            statement.setString(4, data.note)
            statement.setString(5, data.editDate)

            statement.executeUpdate()
        }
        println("Inserted")
    }

    fun updateData(data: Data) {
        val updateStatement = """
            UPDATE data
            SET username = ?, title = ?, note = ?, edit_date = ?
            WHERE id = ?
        """.trimIndent()

        conn?.prepareStatement(updateStatement)?.use { statement ->
            statement.setString(1, data.username)
            statement.setString(2, data.title)
            statement.setString(3, data.note)
            statement.setString(4, data.editDate)
            statement.setString(5, data.id)

            statement.executeUpdate()
        }
        println("Updated")
    }

    fun deleteData(id: String) {
        val deleteStatement = """
            DELETE FROM data
            WHERE id = ?
        """.trimIndent()
        conn?.prepareStatement(deleteStatement)?.use { statement ->
            statement.setString(1, id)
            statement.executeUpdate()
        }
        println("Deleted")
    }

    fun getData(inUsername: String): MutableList<Data> {
        val dataList = mutableListOf<Data>()
        conn?.prepareStatement("SELECT * FROM data WHERE username = ?;").use { statement ->
            statement?.setString(1, inUsername)
            val result = statement?.executeQuery()
            if (result != null) {
                while (result.next()) {
                    val id = result.getString("id")
                    val username = result.getString("username")
                    val title = result.getString("title")
                    val note = result.getString("note")
                    val editDate = result.getString("edit_date")
                    dataList.add(Data(id, username, title, note, editDate))
                }
            }
        }
        return dataList
    }

}