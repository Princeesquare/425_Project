package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.doAfterTextChanged

class MainActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        var data = Dbase()

        username = findViewById(R.id.usname)
        password = findViewById(R.id.pswd)
        loginButton = findViewById(R.id.loginbtn)
        signupButton = findViewById(R.id.sign)

        val uname = username.text.toString()
        val pword = password.text.toString()

        signupButton.setOnClickListener {// SIGN UP BUTTON

            validateInputs(uname, pword)

            // Save username and password to the database
            data.saveCredentials(uname, pword)
            showToast( "Signed Up Successfully")
        }

        loginButton.setOnClickListener {// LOGIN BUTTON

            if (validateInputs(uname, pword)) {
                if (data.validateCredentials(uname, pword)) {
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
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the login activity so that the user can't go back to it without logging out
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateLoginButtonState() {
        val username = username.text.toString()
        val password = password.text.toString()

        loginButton.isEnabled = validateInputs(username, password,)
    }

}