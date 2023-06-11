package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Dbase : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.login)

        dbHelper = DatabaseHelper(this)
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
        val db = dbHelper.readableDatabase

        val selection = "${DatabaseContract.UserEntry.COLUMN_USERNAME} = ? AND " +
                "${DatabaseContract.UserEntry.COLUMN_PASSWORD} = ?"
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

    fun saveDetails(name: String, surname: String, age: Int, department: String) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
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
                val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DetailsEntry.COLUMN_NAME))
                val surname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DetailsEntry.COLUMN_SURNAME))
                val age = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.DetailsEntry.COLUMN_AGE))
                val department = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.DetailsEntry.COLUMN_DEPARTMENT))

                stringBuilder.appendLine("Name: $name\nSurname: $surname\nAge: $age\nDepartment: $department\n")
            }
        }

        return stringBuilder.toString()
    }

    class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            val SQL_CREATE_ENTRIES = "CREATE TABLE ${DatabaseContract.UserEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${DatabaseContract.UserEntry.COLUMN_USERNAME} TEXT," +
                    "${DatabaseContract.UserEntry.COLUMN_PASSWORD} TEXT)"

            db.execSQL(SQL_CREATE_ENTRIES)

            val SQL_CREATE_DETAILS_ENTRIES = "CREATE TABLE ${DatabaseContract.DetailsEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${DatabaseContract.DetailsEntry.COLUMN_NAME}, TEXT," +
                    "${DatabaseContract.DetailsEntry.COLUMN_SURNAME} TEXT," +
                    "${DatabaseContract.DetailsEntry.COLUMN_AGE} INTEGER," +
                    "${DatabaseContract.DetailsEntry.COLUMN_DEPARTMENT} TEXT)"

            db.execSQL(SQL_CREATE_DETAILS_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // Handle database upgrades if needed
        }

        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "UserCredentials.db"
        }
    }
}

object DatabaseContract {
    object UserEntry : BaseColumns {
        const val TABLE_NAME = "users"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
    }

    object DetailsEntry : BaseColumns {
        const val TABLE_NAME = "Students"
        const val COLUMN_NAME = "First Name"
        const val COLUMN_SURNAME = "Surname"
        const val COLUMN_AGE = "Age"
        const val COLUMN_DEPARTMENT = "Department"
    }
}
