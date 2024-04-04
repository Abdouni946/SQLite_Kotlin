import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "database.db"
        private const val TABLE_NAME = "ListItem"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
    }

    class Personnes(val id: Int, val name: String)

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID TEXT PRIMARY KEY," +
                "$COLUMN_NAME TEXT)"
        db.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addItem(id: String, name: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, id)
            put(COLUMN_NAME, name)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateItem(id: String, newName: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, newName)
        }
        val success = db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id))
        db.close()
        return success
    }

    fun deleteItem(id: String): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id))
        db.close()
        return success
    }

    fun getAllItems(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun cursorToPersonnes(cursor: Cursor): List<Personnes> {
    val personnes = mutableListOf<Personnes>()
    while (cursor.moveToNext()) {
        val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        personnes.add(Personnes(id, name))
    }
    return personnes
}
}