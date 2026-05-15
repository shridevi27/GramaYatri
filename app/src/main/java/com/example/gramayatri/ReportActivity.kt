package com.example.gramayatri

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class ReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val problemInput = findViewById<EditText>(R.id.problemInput)
        val submitBtn = findViewById<Button>(R.id.submitProblemBtn)

        submitBtn.setOnClickListener {

            val text = problemInput.text.toString()

            val ref = FirebaseDatabase.getInstance()
                .getReference("problems")

            ref.push().setValue(text)

            Toast.makeText(
                this,
                "Problem Reported",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}