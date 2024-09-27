package com.tdt4240.amazonwarriors.screens.viewmodels

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.tdt4240.amazonwarriors.screens.models.GameEndModel
import com.tdt4240.amazonwarriors.utils.FirebaseManager
import com.tdt4240.amazonwarriors.utils.ScreenManager
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders

class GameEndViewModel {

    private val assetManager = AssetManager()
    private val model = GameEndModel()

    fun handleMainMenuClick(){
        ScreenManager.leaveEndGame()
    }

    fun addToLeaderBoard(name: String, score: Int) {
        FirebaseManager.addDataToLeaderboard(name, score)
    }

    fun loadTextures() {
        model.texturePaths.forEach { assetManager.load(it, Texture::class.java) }
        assetManager.finishLoading()
    }

    fun getTexture(texturePath: String): Texture = assetManager.get(texturePath)

    fun loadFonts(screenHeight: Float) {
        assetManager.registerFreeTypeFontLoaders()
        assetManager.registerFreeTypeFontLoaders(fileExtensions = arrayOf(".custom"))
        assetManager.registerFreeTypeFontLoaders(replaceDefaultBitmapFontLoader = true)
        assetManager.loadFreeTypeFont("Passion_One/PassionOne-Regular.ttf")  {
            size = (screenHeight * 0.075f).toInt()
            borderWidth = (size/25).toFloat()
            borderColor = Color.valueOf("8a3e0b")
        }
        assetManager.loadFreeTypeFont("Passion_One/PassionOne-Regular2.ttf")  {
            size = (screenHeight * 0.05f).toInt()
            borderWidth = (size/25).toFloat()
            borderColor = Color.valueOf("89261f")
        }
        assetManager.finishLoading()
    }

    fun getFont(fontPath: String): BitmapFont = assetManager.get(fontPath)

    fun dispose() {
        assetManager.dispose()
    }
}
