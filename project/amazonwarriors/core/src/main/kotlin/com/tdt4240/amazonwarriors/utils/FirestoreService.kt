package com.tdt4240.amazonwarriors.utils

interface FirestoreService {
    fun addLeaderboardEntry(name: String, score: Int)
    suspend fun fetchLeaderboard(): List<String>
}
