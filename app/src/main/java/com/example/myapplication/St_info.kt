package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button

class St_info : AppCompatActivity() {

    private lateinit var addButton: Button
    private lateinit var viewButton: Button
    private lateinit var logoutButton: Button

    //@SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stinfo)

        val data = Dbase()

        addButton = findViewById(R.id.addBtn)
        viewButton = findViewById(R.id.viewBtn)
        logoutButton = findViewById(R.id.logout)

        addButton.setOnClickListener {
            openMainActivity(Students::class.java)
        }

        viewButton.setOnClickListener {
            val details = getDetails()
            detailsTextView.text = details
        }
        logoutButton.setOnClickListener {
            openMainActivity(MainActivity::class.java)
        }
    }
    private fun openMainActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish() // Close the login activity so that the user can't go back to it without logging out
    }
}