package com.tdt4240.amazonwarriors.screens.viewmodels

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.tdt4240.amazonwarriors.screens.models.SettingsModel
import com.tdt4240.amazonwarriors.utils.ScreenManager
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders

class SettingsViewModel {
    private val assetManager = AssetManager()
    private val model = SettingsModel()

    fun handleBtnClick(){
        ScreenManager.pop()
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
        }
        assetManager.finishLoading()
    }

    fun getFont(fontPath: String): BitmapFont = assetManager.get(fontPath)

    fun dispose() {
        assetManager.dispose()
    }
}
