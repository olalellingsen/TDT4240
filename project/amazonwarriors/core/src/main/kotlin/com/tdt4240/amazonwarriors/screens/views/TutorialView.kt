package com.tdt4240.amazonwarriors.screens.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.tdt4240.amazonwarriors.screens.viewmodels.TutorialViewModel
import com.tdt4240.amazonwarriors.utils.AudioManager
import ktx.actors.onClick
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class TutorialView: BaseView() {
    private val viewModel = TutorialViewModel()
    private lateinit var background: Texture

    private val btnNamesAndScales = mapOf(
        // arrayOf(scaleX, scaleY, scaleWidth, scaleHeight)
        "Back" to arrayOf(0.08f, 0.92f, 0.12f, 0.16f),
        "Next" to arrayOf(0.92f, 0.08f, 0.12f, 0.16f),
        "Prev" to arrayOf(0.08f, 0.08f, 0.12f, 0.16f)
    )

    private lateinit var currentImage: Texture

    override fun show(){
        viewModel.loadTextures()
        background = viewModel.getTexture("Background.png")
        Gdx.input.inputProcessor = stage
        updateCurrentImg()
    }

    private fun createAndAddButtons() {
        stage.clear()
        for ((btnName, scaleInfo) in btnNamesAndScales) {
            val btnImgUp = viewModel.getTexture("${btnName}_Button.png")
            val btnImgDown = viewModel.getTexture("${btnName}_Button_Pressed.png")
            val btnImgStyle = ImageButton.ImageButtonStyle()
            btnImgStyle.imageUp = TextureRegionDrawable(btnImgUp)
            btnImgStyle.imageDown = TextureRegionDrawable(btnImgDown)
            val btn = ImageButton(btnImgStyle).apply {
                width = screenWidth * (scaleInfo[2])
                height = screenHeight * (scaleInfo[3])
                setPosition(screenWidth * (scaleInfo[0]) - width / 2, screenHeight * (scaleInfo[1]) - height / 2)
            }
            when (btnName) {
                "Back" -> btn.onClick {
                    viewModel.handleBackBtnClick()
                    viewModel.setFirstImage()
                    AudioManager.playClickBack()
                }
                "Next" -> btn.onClick {
                    AudioManager.playClick()
                    viewModel.handleNextBtnClick()
                    updateCurrentImg()
                }
                "Prev" -> btn.onClick {
                    AudioManager.playClick2()
                    viewModel.handlePrevBtnClick()
                    updateCurrentImg()
                }
            }

            // add prev
            if (!viewModel.isFirstImage && btnName == "Prev") {
                stage.addActor(btn)
            }
            // add next
            if (!viewModel.isLastImage && btnName == "Next") {
                stage.addActor(btn)
            }
            // add back
            if (btnName == "Back") {
                stage.addActor(btn)
            }
        }
    }

    private fun updateCurrentImg(){
        currentImage = viewModel.getTexture(viewModel.getCurrentTexturePath())
        createAndAddButtons() // in order to update the buttons visibility
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        batch.use {
            it.draw(background, 0f,0f, screenWidth, screenHeight)
            it.draw(currentImage, screenWidth*0.15f, screenHeight*0.15f, screenWidth*0.7f, screenHeight*0.7f)
        }
        stage.act()
        stage.draw()
    }

    override fun dispose() {
        batch.disposeSafely()
        stage.disposeSafely()
        viewModel.dispose()
    }
}
