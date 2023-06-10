package com.example.myapplication

import android.os.Bundle
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import androidx.appcompat.app.AppCompatActivity

class Dbase: AppCompatActivity() {

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

    class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            val SQL_CREATE_ENTRIES = "CREATE TABLE ${DatabaseContract.UserEntry.TABLE_NAME} (" +
                    "${DatabaseContract.UserEntry._ID} INTEGER PRIMARY KEY," +
                    "${DatabaseContract.UserEntry.COLUMN_USERNAME} TEXT," +
                    "${DatabaseContract.UserEntry.COLUMN_PASSWORD} TEXT)"

            db.execSQL(SQL_CREATE_ENTRIES)
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
    class UserEntry : BaseColumns {
        companion object {
            val _ID: String = ""
            const val TABLE_NAME = "users"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_PASSWORD = "password"
        }
    }

}