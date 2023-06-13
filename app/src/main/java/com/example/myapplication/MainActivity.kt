package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged

class MainActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button

    private lateinit var data: Dbase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        data = Dbase(this)

        username = findViewById(R.id.usname)
        password = findViewById(R.id.pswd)
        loginButton = findViewById(R.id.loginbtn)
        signupButton = findViewById(R.id.sign)

        signupButton.setOnClickListener {
            val uname = username.text.toString()
            val pwd = password.text.toString()

            if (validateInputs(uname, pwd)) {
                // Save username and password to the database
                data.saveCredentials(uname, pwd)
                showToast("Signed Up Successfully")
            }
        }

        loginButton.setOnClickListener {
            val uname = username.text.toString()
            val pwd = password.text.toString()

            if (validateInputs(uname, pwd)) {
                val credValid = data.validateCredentials(uname, pwd)
                if (credValid) {
                    openMainActivity()
                } else {
                    showToast("Invalid username or password")
                }
            }
        }

        // Enable or disable the login button based on input validity
        username.doAfterTextChanged { updateLoginButtonState() }
        password.doAfterTextChanged { updateLoginButtonState() }
        updateLoginButtonState()
    }

    private fun validateInputs(user: String, pwd: String): Boolean {
        if (user.isEmpty()) {
            username.error = "Username cannot be empty"
            return false
        }
        if (pwd.isEmpty()) {
            password.error = "Password cannot be empty"
            return false
        }
        return true
    }

    private fun openMainActivity() {
        val intent = Intent(this, Stinfo::class.java)
        startActivity(intent)
        finish() // Close the login activity so that the user can't go back to it without logging out
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateLoginButtonState() {
        val uname = username.text.toString()
        val pwd = password.text.toString()

        loginButton.isEnabled = validateInputs(uname, pwd)
    }
}
