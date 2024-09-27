package com.tdt4240.amazonwarriors.screens.viewmodels

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.tdt4240.amazonwarriors.screens.models.MainMenuModel
import com.tdt4240.amazonwarriors.screens.views.LeaderboardView
import com.tdt4240.amazonwarriors.screens.views.SettingsView
import com.tdt4240.amazonwarriors.screens.views.TutorialView
import com.tdt4240.amazonwarriors.utils.AudioManager
import com.tdt4240.amazonwarriors.utils.ScreenManager
import ktx.assets.disposeSafely

class MainMenuViewModel {

    private val model = MainMenuModel()
    private val assetManager = AssetManager()

    fun handleBtnClick(btnName: String){
        when(btnName) {
            "Play" -> {
                ScreenManager.setNewGame()
                AudioManager.playGameMusic()
            }
            "Leaderboard" -> {
                ScreenManager.push(LeaderboardView())
            }
            "Tutorial" -> {
                ScreenManager.push(TutorialView())
            }
            "Settings" -> {
                ScreenManager.push(SettingsView())
            }
        }
    }


    fun loadTextures() {
        model.texturePaths.forEach { assetManager.load(it, Texture::class.java) }
        assetManager.finishLoading()
    }

    fun getTexture(texturePath: String): Texture = assetManager.get(texturePath)

    fun dispose() {
        assetManager.disposeSafely()
    }
}
