package com.example.gramayatri

import com.google.firebase.database.FirebaseDatabase

/**
 * Seeds all 10 routes and their stops into Firebase on first app launch.
 * Uses a simple flag node "seeded" in Firebase to avoid re-seeding.
 */
object RouteSeeder {

    fun seedIfNeeded() {
        val db = FirebaseDatabase.getInstance().reference
        db.child("seeded").get().addOnSuccessListener { snapshot ->
            if (!snapshot.exists()) {
                val routes = db.child("routes")
                for ((routeName, stops) in RouteDataHelper.ALL_ROUTES) {
                    routes.child(routeName).child("stops").setValue(stops)
                }
                db.child("seeded").setValue(true)
            }
        }
    }
}
