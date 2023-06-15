package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class Search : AppCompatActivity() {

    private lateinit var studentNumberEditText: EditText
    private lateinit var firstNameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var departmentEditText: EditText
    private lateinit var okButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Linking buttons to component on activity
        studentNumberEditText = findViewById(R.id.stno)
        studentNumberEditText.keyListener = null // Prevent user input from keyboard
        firstNameEditText = findViewById(R.id.name)
        firstNameEditText.keyListener = null
        surnameEditText = findViewById(R.id.surname)
        surnameEditText.keyListener = null
        ageEditText = findViewById(R.id.agebox)
        ageEditText.keyListener = null
        departmentEditText = findViewById(R.id.dept)
        departmentEditText.keyListener = null
        okButton = findViewById(R.id.okBtn)

        // Passing Student Information to activity components
        val studentNumber = intent.getStringExtra("studentNumber")
        val firstName = intent.getStringExtra("firstName")
        val surname = intent.getStringExtra("surname")
        val age = intent.getIntExtra("age", 0)
        val department = intent.getStringExtra("department")

        // Setting the student information after search
        studentNumberEditText.setText(studentNumber)
        firstNameEditText.setText(firstName)
        surnameEditText.setText(surname)
        ageEditText.setText(age.toString())
        departmentEditText.setText(department)

        okButton.setOnClickListener{
            val n: String? = null
            val intent = Intent(this, Stinfo::class.java)
            intent.putExtra("Clear", n)
            startActivity(intent)
        }
    }

}