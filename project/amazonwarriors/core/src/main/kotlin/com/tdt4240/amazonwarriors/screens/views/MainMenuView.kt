package com.tdt4240.amazonwarriors.screens.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.tdt4240.amazonwarriors.screens.viewmodels.MainMenuViewModel
import com.tdt4240.amazonwarriors.utils.AudioManager
import ktx.actors.onClick
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class MainMenuView: BaseView() {

    private lateinit var background: Texture
    private val viewModel = MainMenuViewModel()

    private val btnNamesAndScales = mapOf(
        "Play" to arrayOf(0.22f, 0.23f),
        "Leaderboard" to arrayOf(0.32f, 0.23f),
        "Tutorial" to arrayOf(0.20f, 0.22f),
        "Settings" to arrayOf(0.20f, 0.18f)
    )

    override fun show(){
        viewModel.loadTextures()
        background = viewModel.getTexture("Main_Menu/Menu.png")
        Gdx.input.inputProcessor = stage
        createAndAddButtons()
        AudioManager.playMenuMusic()
    }

    private fun createAndAddButtons() {
        var extraHeight = 0f // Used to ensure correct distance between play button and the others
        for ((btnName, scaleInfo) in btnNamesAndScales) {
            val btnImgUp = viewModel.getTexture("Main_Menu/${btnName}_Button.png")
            val btnImgDown = viewModel.getTexture("Main_Menu/${btnName}_Button_Pressed.png")
            val btnImgStyle = ImageButton.ImageButtonStyle()
            btnImgStyle.imageUp = TextureRegionDrawable(btnImgUp)
            btnImgStyle.imageDown = TextureRegionDrawable(btnImgDown)
            val btn = ImageButton(btnImgStyle).apply {
                width = screenWidth * (scaleInfo[0])
                height = screenHeight * (scaleInfo[1])
                setPosition(screenWidth/2 - width/2, screenHeight*0.56f - height/2 - extraHeight)
            }
            extraHeight += btn.height*0.68f
            if (btnName == "Play"){ extraHeight += btn.height*0.05f}
            btn.onClick {
                viewModel.handleBtnClick(btnName)
                AudioManager.playClick()
            }
            stage.addActor(btn)
        }
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        batch.use {
            it.draw(background, 0f, 0f, screenWidth, screenHeight )
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
