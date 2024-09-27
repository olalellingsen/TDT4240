package com.tdt4240.amazonwarriors.screens.viewmodels

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.tdt4240.amazonwarriors.screens.models.LeaderboardModel
import com.tdt4240.amazonwarriors.utils.FirebaseManager
import com.tdt4240.amazonwarriors.utils.ScreenManager
import ktx.assets.disposeSafely
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders

class LeaderboardViewModel {

    private val model = LeaderboardModel()
    private val assetManager = AssetManager()



    suspend fun fetchLeaderboardData(): List<String> {
        val leaderboard = FirebaseManager.fetchLeaderboard()
        updateModel(leaderboard)
        return leaderboard
    }

    private fun updateModel(leaderboard: List<String>) {
        model.leaderboardData.clear()
        model.leaderboardData.addAll(leaderboard)
    }

    fun handleBtnClick(){
        ScreenManager.pop()
    }

    fun loadTextures() {
        model.texturePaths.forEach{ assetManager.load(it, Texture::class.java) }
        assetManager.finishLoading()
    }

    fun getTexture(path: String): Texture = assetManager.get(path)

    fun dispose() {
        assetManager.disposeSafely()
    }

    fun loadFonts(screenHeight: Float) {
        assetManager.registerFreeTypeFontLoaders()
        assetManager.registerFreeTypeFontLoaders(fileExtensions = arrayOf(".custom"))
        assetManager.registerFreeTypeFontLoaders(replaceDefaultBitmapFontLoader = true)
        assetManager.loadFreeTypeFont("Passion_One/PassionOne-Regular.ttf")  {
            size = (screenHeight * 0.075f).toInt()
        }
        assetManager.finishLoading()
    }

    fun getFont(fontPath: String): BitmapFont = assetManager.get(fontPath)
}
