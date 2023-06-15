package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Students : AppCompatActivity() {

    private lateinit var saveButton: Button
    private lateinit var backButton: Button

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var departmentEditText: EditText
    private lateinit var studentNumberEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students)

        val data = Dbase(this)

        saveButton = findViewById(R.id.okBtn)
        backButton = findViewById(R.id.backBtn)
        nameEditText = findViewById(R.id.name)
        surnameEditText = findViewById(R.id.surname)
        ageEditText = findViewById(R.id.age)
        departmentEditText = findViewById(R.id.dpt)
        studentNumberEditText = findViewById(R.id.stnum)

        saveButton.setOnClickListener {
            val stNum = studentNumberEditText.text.toString()
            val name = nameEditText.text.toString()
            val surname = surnameEditText.text.toString()
            val age = ageEditText.text.toString().toIntOrNull()
            val department = departmentEditText.text.toString()

            if (stNum.isNotEmpty() && name.isNotEmpty() && surname.isNotEmpty() && age != null && department.isNotEmpty()) {
                data.saveDetails(stNum, name, surname, age, department)
                Toast.makeText(this, "Student Information Saved Successfully", Toast.LENGTH_LONG).show()
                clearEditTextFields()
            } else {
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_LONG).show()
            }
        }

        backButton.setOnClickListener {
            openActivity(Stinfo::class.java)
        }
    }

    private fun clearEditTextFields() {
        nameEditText.text.clear()
        surnameEditText.text.clear()
        ageEditText.text.clear()
        departmentEditText.text.clear()
        studentNumberEditText.text.clear()
    }

    private fun openActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}
