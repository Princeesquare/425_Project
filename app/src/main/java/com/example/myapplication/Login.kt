package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Login: AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        dbHelper = DatabaseHelper(this)

        username = findViewById(R.id.usname)
        password = findViewById(R.id.pswd)
        loginButton = findViewById(R.id.loginbtn)

        loginButton.setOnClickListener {
            val username = username.text.toString()
            val password = password.text.toString()

            // Save username and password to the database
            saveCredentials(username, password)
        }
    }

    private fun saveCredentials(username: String, password: String) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseContract.UserEntry.COLUMN_USERNAME, username)
            put(DatabaseContract.UserEntry.COLUMN_PASSWORD, password)
        }

        db.insert(DatabaseContract.UserEntry.TABLE_NAME, null, values)
        db.close()
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