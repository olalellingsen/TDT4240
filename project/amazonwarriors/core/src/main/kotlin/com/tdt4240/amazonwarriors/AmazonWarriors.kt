package com.tdt4240.amazonwarriors

import com.tdt4240.amazonwarriors.screens.views.MainMenuView
import com.tdt4240.amazonwarriors.utils.AudioManager
import com.tdt4240.amazonwarriors.utils.FirebaseManager
import com.tdt4240.amazonwarriors.utils.FirestoreService
import com.tdt4240.amazonwarriors.utils.ScreenManager
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class AmazonWarriors(private val firestoreService: FirestoreService) : KtxGame<KtxScreen>() {

    override fun create() {
        KtxAsync.initiate()
        ScreenManager.setGame(this)
        ScreenManager.addAllScreens()
        FirebaseManager.setFirestoreService(firestoreService)
        ScreenManager.push(MainMenuView())
    }

    override fun dispose() {
        AudioManager.disposeMusic()
        super.dispose()
    }
}

