package com.tdt4240.amazonwarriors.screens.viewmodels

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.tdt4240.amazonwarriors.screens.models.TutorialModel
import com.tdt4240.amazonwarriors.utils.ScreenManager
import ktx.assets.disposeSafely

class TutorialViewModel {
    private val model = TutorialModel()

    private val assetManager = AssetManager()

    private var imageIndex: Int = 0
        set(value) {
            field = value
            updateImageStatus()
        }

    var isFirstImage: Boolean = true
        private set

    var isLastImage: Boolean = false
        private set

    init {
        updateImageStatus()
    }

    fun setFirstImage() {
        imageIndex = 0
    }

    fun getCurrentTexturePath(): String {
        return model.tutorialTextures[imageIndex]
    }

    fun handleNextBtnClick() {
        if (imageIndex < model.tutorialTextures.size - 1) {
            imageIndex++
            updateImageStatus()
        }
    }

    fun handlePrevBtnClick() {
        if (imageIndex > 0) {
            imageIndex--
            updateImageStatus()
        }
    }

    private fun updateImageStatus() {
        isFirstImage = imageIndex == 0
        isLastImage = imageIndex == model.tutorialTextures.size - 1
    }

    fun handleBackBtnClick() {
        ScreenManager.pop()
    }

    private fun loadTutorialImages(assetManager: AssetManager) {
        model.tutorialTextures.forEach { assetManager.load(it, Texture::class.java) }
    }

    fun loadTextures() {
        model.texturePaths.forEach { assetManager.load(it, Texture::class.java) }
        loadTutorialImages(assetManager)
        assetManager.finishLoading()
    }

    fun getTexture(texturePath: String): Texture = assetManager.get(texturePath)

    fun dispose() {
        assetManager.disposeSafely()
    }
}
