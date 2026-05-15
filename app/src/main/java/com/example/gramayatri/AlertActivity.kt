package com.example.gramayatri

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AlertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        val routeInput = findViewById<EditText>(R.id.routeInput)
        val descriptionInput = findViewById<EditText>(R.id.descriptionInput)
        val alertSpinner = findViewById<Spinner>(R.id.alertSpinner)
        val submitAlertBtn = findViewById<Button>(R.id.submitAlertBtn)

        // Alert options
        val alertTypes = arrayOf(
            "Bus Delayed",
            "Bus Missing",
            "Heavy Crowd",
            "Accident",
            "Road Block",
            "Bus Breakdown"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            alertTypes
        )

        alertSpinner.adapter = adapter

        submitAlertBtn.setOnClickListener {

            val route = routeInput.text.toString()
            val description = descriptionInput.text.toString()
            val alertType = alertSpinner.selectedItem.toString()

            if (route.isEmpty() || description.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please fill all details",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Alert Submitted Successfully!",
                    Toast.LENGTH_LONG
                ).show()

                routeInput.text.clear()
                descriptionInput.text.clear()
            }
        }
    }
}