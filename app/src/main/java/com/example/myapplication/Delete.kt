package com.example.myapplication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class Delete : AppCompatActivity() {

    private lateinit var deleteButton: Button
    private lateinit var cancelButton: Button
    private lateinit var studentNumber: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)

        val data = Dbase(this)

        deleteButton = findViewById(R.id.delBtn)
        cancelButton = findViewById(R.id.cancelBtn)
        studentNumber = findViewById(R.id.delstudent)

        deleteButton.setOnClickListener {
            val stNum = studentNumber.text.toString()
            if (stNum.isNotEmpty()) {
                val deleted = data.deleteDetailsByStudentNumber(stNum)
                if (deleted) {
                    val builder = AlertDialog.Builder(this@Delete)
                    builder.setTitle("Exit Alert")
                    builder.setMessage("Do you want to Delete this Student Information?")
                    builder.setCancelable(false)
                    builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
                        Toast.makeText(this, "Student Information Deleted Successfully",
                            Toast.LENGTH_LONG).show()
                    }
                    builder.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                        dialog.cancel()
                    }
                    builder.show()
                } else {
                    Toast.makeText(this, "No matching student found with the provided student number", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please Enter Student Number", Toast.LENGTH_LONG).show()
            }
            studentNumber.text.clear()
        }

        cancelButton.setOnClickListener{
            val intent = Intent(this, Stinfo::class.java)
            startActivity(intent)
            finish()
        }
    }
}
