package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Students : AppCompatActivity() {

    private lateinit var save: Button
    private lateinit var back: Button

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var departmentEditText: EditText

    //@SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students)

        val data = Dbase()

        save = findViewById(R.id.saveBtn)
        back = findViewById(R.id.backBtn)
        nameEditText = findViewById(R.id.fname)
        surnameEditText = findViewById(R.id.sname)
        ageEditText = findViewById(R.id.age)
        departmentEditText = findViewById(R.id.dpt)

        save.setOnClickListener {
            val name = nameEditText.text.toString()
            val surname = surnameEditText.text.toString()
            val age = ageEditText.text.toString().toInt()
            val department = departmentEditText.text.toString()

            data.saveDetails(name, surname, age, department)
            Toast.makeText(this, "Student Information Saved Successfully", Toast.LENGTH_LONG).show()
            clearEditTextFields()
        }
        back.setOnClickListener{
            openMainActivity(St_info::class.java)
        }
    }

    private fun clearEditTextFields() {
        nameEditText.text.clear()
        surnameEditText.text.clear()
        ageEditText.text.clear()
        departmentEditText.text.clear()
    }
    private fun openMainActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish()
    }
}