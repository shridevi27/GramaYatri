package com.example.gramayatri

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.emailEdit)
        val password = findViewById<EditText>(R.id.passwordEdit)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val signupText = findViewById<TextView>(R.id.signupText)

        loginBtn.setOnClickListener {

            val userEmail = email.text.toString()
            val userPass = password.text.toString()

            auth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener {

                    if (it.isSuccessful) {

                        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()

                    } else {

                        Toast.makeText(this, "Invalid User", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        signupText.setOnClickListener {

            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}