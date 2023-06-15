package com.example.myapplication

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class Stinfo : AppCompatActivity() {

    private lateinit var addButton: Button
    private lateinit var viewButton: Button
    private lateinit var logoutButton: Button
    private lateinit var deleteButton: Button
    private lateinit var searchButton: Button
    private lateinit var detailsTextView: EditText
    private lateinit var studentNumber: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stinfo)

        val data = Dbase(this)

        addButton = findViewById(R.id.addBtn)
        viewButton = findViewById(R.id.viewBtn)
        logoutButton = findViewById(R.id.logout)
        detailsTextView = findViewById(R.id.viewDetails)
        searchButton = findViewById(R.id.searchBtn)
        studentNumber = findViewById(R.id.stnum2)
        deleteButton = findViewById(R.id.delButton)
        detailsTextView.keyListener = null


        addButton.setOnClickListener {
            openActivity(Students::class.java)
        }

        viewButton.setOnClickListener {
            val details: String = data.getDetails()
            detailsTextView.setText(details)
        }

        deleteButton.setOnClickListener{
            openActivity(Delete::class.java)
        }
        searchButton.setOnClickListener {
            val studentNum = studentNumber.text.toString()
            if (studentNum.isNotEmpty()) {
                val studentInfo = data.getStudentInfoByStudentNumber(studentNum)
                if (studentInfo != null) {
                    val intent = Intent(this, Search::class.java)
                    intent.putExtra("studentNumber", studentInfo.studentNumber)
                    intent.putExtra("firstName", studentInfo.firstName)
                    intent.putExtra("surname", studentInfo.surname)
                    intent.putExtra("age", studentInfo.age)
                    intent.putExtra("department", studentInfo.department)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No student found with the provided student number", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please enter a student number", Toast.LENGTH_LONG).show()
            }
            val clear = intent.getStringExtra("Clear")
            studentNumber.setText(clear)
        }

        logoutButton.setOnClickListener {
            val builder = AlertDialog.Builder(this@Stinfo)
            builder.setTitle("Exit Alert")
            builder.setMessage("Do you want to logout?")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
                openActivity(MainActivity::class.java)
            }
            builder.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                dialog.cancel()
            }
            builder.show()
        }
    }

    private fun openActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish() // Close the activity so that the user can't go back to it without logging out
    }
}
