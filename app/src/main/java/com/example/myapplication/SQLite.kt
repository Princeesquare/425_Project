package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class Dbase(context: Context) {

    private val dbHelper: DatabaseHelper = DatabaseHelper(context)

    fun createDatabase() {
        dbHelper.writableDatabase
    }

    private fun isDatabaseExists(): Boolean {
        val dbFile = dbHelper.context.getDatabasePath(DatabaseHelper.DATABASE_NAME)
        return dbFile.exists()
    }

    fun saveCredentials(username: String, password: String) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseContract.UserEntry.COLUMN_USERNAME, username)
            put(DatabaseContract.UserEntry.COLUMN_PASSWORD, password)
        }

        db.insert(DatabaseContract.UserEntry.TABLE_NAME, null, values)
        db.close()
    }

    fun validateCredentials(username: String, password: String): Boolean {
        if (!isDatabaseExists()) {
            return false
        }
        val db = dbHelper.readableDatabase

        val selection =
            "${DatabaseContract.UserEntry.COLUMN_USERNAME} = ? AND ${DatabaseContract.UserEntry.COLUMN_PASSWORD} = ?"
        val selectionArgs = arrayOf(username, password)

        val cursor: Cursor = db.query(
            DatabaseContract.UserEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val isValid = cursor.count > 0

        cursor.close()
        db.close()

        return isValid
    }

    fun saveDetails(studentNumber: String, name: String, surname: String, age: Int, department: String) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseContract.DetailsEntry.COLUMN_STUDENT_NUMBER, studentNumber)
            put(DatabaseContract.DetailsEntry.COLUMN_NAME, name)
            put(DatabaseContract.DetailsEntry.COLUMN_SURNAME, surname)
            put(DatabaseContract.DetailsEntry.COLUMN_AGE, age)
            put(DatabaseContract.DetailsEntry.COLUMN_DEPARTMENT, department)
        }

        db.insert(DatabaseContract.DetailsEntry.TABLE_NAME, null, values)
        db.close()
    }

    fun getDetails(): String {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            DatabaseContract.DetailsEntry.COLUMN_STUDENT_NUMBER,
            DatabaseContract.DetailsEntry.COLUMN_NAME,
            DatabaseContract.DetailsEntry.COLUMN_SURNAME,
            DatabaseContract.DetailsEntry.COLUMN_AGE,
            DatabaseContract.DetailsEntry.COLUMN_DEPARTMENT
        )

        val stringBuilder = StringBuilder()

        db.query(
            DatabaseContract.DetailsEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        ).use { cursor ->
            while (cursor.moveToNext()) {
                val studentNumber =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DetailsEntry.COLUMN_STUDENT_NUMBER))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DetailsEntry.COLUMN_NAME))
                val surname =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DetailsEntry.COLUMN_SURNAME))
                val age =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.DetailsEntry.COLUMN_AGE))
                val department =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DetailsEntry.COLUMN_DEPARTMENT))

                stringBuilder.appendLine("Student Number: $studentNumber")
                stringBuilder.appendLine("Name: $name")
                stringBuilder.appendLine("Surname: $surname")
                stringBuilder.appendLine("Age: $age")
                stringBuilder.appendLine("Department: $department")
                stringBuilder.appendLine()
            }
        }

        db.close()

        return stringBuilder.toString()
    }


    fun deleteDetailsByStudentNumber(studentNumber: String): Boolean {
        val db = dbHelper.writableDatabase

        val selection = "${DatabaseContract.DetailsEntry.COLUMN_STUDENT_NUMBER} = ?"
        val selectionArgs = arrayOf(studentNumber)

        val deletedRows = db.delete(
            DatabaseContract.DetailsEntry.TABLE_NAME,
            selection,
            selectionArgs
        )

        db.close()

        return deletedRows > 0
    }

    private class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        val context: Context = context.applicationContext

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(DatabaseContract.UserEntry.SQL_CREATE_ENTRIES)
            db.execSQL(DatabaseContract.DetailsEntry.SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL(DatabaseContract.UserEntry.SQL_DELETE_ENTRIES)
            db.execSQL(DatabaseContract.DetailsEntry.SQL_DELETE_ENTRIES)
            onCreate(db)
        }

        companion object {
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "MyDatabase.db"
        }
    }

    private object DatabaseContract {

        object UserEntry : BaseColumns {
            const val TABLE_NAME = "users"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_PASSWORD = "password"

            const val SQL_CREATE_ENTRIES =
                "CREATE TABLE $TABLE_NAME (" +
                        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                        "$COLUMN_USERNAME TEXT," +
                        "$COLUMN_PASSWORD TEXT)"

            const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
        }

        object DetailsEntry : BaseColumns {
            const val TABLE_NAME = "details"
            const val COLUMN_STUDENT_NUMBER = "student_number"
            const val COLUMN_NAME = "name"
            const val COLUMN_SURNAME = "surname"
            const val COLUMN_AGE = "age"
            const val COLUMN_DEPARTMENT = "department"

            const val SQL_CREATE_ENTRIES =
                "CREATE TABLE $TABLE_NAME (" +
                        "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                        "$COLUMN_STUDENT_NUMBER TEXT," +
                        "$COLUMN_NAME TEXT," +
                        "$COLUMN_SURNAME TEXT," +
                        "$COLUMN_AGE INTEGER," +
                        "$COLUMN_DEPARTMENT TEXT)"

            const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
        }
    }
}
