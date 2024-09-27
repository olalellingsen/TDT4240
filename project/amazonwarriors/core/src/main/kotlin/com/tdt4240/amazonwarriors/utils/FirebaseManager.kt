package com.tdt4240.amazonwarriors.utils

object FirebaseManager {

    private lateinit var firestoreService: FirestoreService

    private val leaderboardData: MutableList<String> = mutableListOf()

    fun setFirestoreService(firestoreService: FirestoreService) {
        FirebaseManager.firestoreService = firestoreService
    }

    suspend fun fetchLeaderboard(): List<String> {
        leaderboardData.clear()
        val fetchedLeaderboardData = firestoreService.fetchLeaderboard()
        leaderboardData.addAll(fetchedLeaderboardData)
        return leaderboardData
    }

    fun addDataToLeaderboard(name: String, score: Int) {
        firestoreService.addLeaderboardEntry(name, score)
    }
}

