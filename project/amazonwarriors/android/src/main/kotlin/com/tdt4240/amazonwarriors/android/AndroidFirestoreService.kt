package com.tdt4240.amazonwarriors.android

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tdt4240.amazonwarriors.utils.FirestoreService
import kotlinx.coroutines.tasks.await

object AndroidFirestoreService : FirestoreService {

    private val db = Firebase.firestore

    override fun addLeaderboardEntry(name: String, score: Int) {

        val data = mapOf(
            "Name" to name,
            "Score" to score
        )

        db.collection("Leaderboard")
            .add(data)
            .addOnSuccessListener { documentReference ->
                println("Data added successfully")
            }
            .addOnFailureListener { e ->
                println("Error adding data: $e")
            }
    }

    override suspend fun fetchLeaderboard(): List<String> {
        val leaderboard = mutableListOf<String>()

        try {
            val querySnapshot = db.collection("Leaderboard").get().await()
            for (document in querySnapshot) {
                val name = document.getString("Name") ?: ""
                val score = document.getLong("Score")?.toString() ?: "0"
                val entry = "$name: $score"
                leaderboard.add(entry)
            }

            // Sort the leaderboard data by score in descending order
            leaderboard.sortByDescending {
                val score = it.substringAfterLast(":").trim().toIntOrNull()
                score ?: 0 // Use 0 if the score cannot be parsed as an integer
            }

            println("Documents fetched successfully")
        } catch (exception: Exception) {
            println("Error getting documents: $exception")
        }
        return leaderboard
    }

}
