package com.example.gramayatri

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.emailEdit)
        val password = findViewById<EditText>(R.id.passwordEdit)
        val registerBtn = findViewById<Button>(R.id.registerBtn)

        registerBtn.setOnClickListener {

            val userEmail = email.text.toString()
            val userPass = password.text.toString()

            auth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener {

                    if (it.isSuccessful) {

                        Toast.makeText(this, "Registration Success", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()

                    } else {

                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}