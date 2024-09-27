package com.tdt4240.amazonwarriors.screens.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.tdt4240.amazonwarriors.screens.viewmodels.SettingsViewModel
import com.tdt4240.amazonwarriors.utils.SettingsManager
import com.tdt4240.amazonwarriors.utils.AudioManager
import com.tdt4240.amazonwarriors.utils.ScreenManager
import ktx.actors.onClick
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class SettingsView: BaseView() {
    private lateinit var viewBackground: Texture
    private val viewModel = SettingsViewModel()

    private var customFont = BitmapFont()
    private lateinit var cancelWindow: Window

    private val btnNamesAndScales = mapOf(
        "Back" to arrayOf(0.12f, 0.16f),
        "Check_sound_effects" to arrayOf(0.16f, 0.16f),
        "Check_background_sound" to arrayOf(0.16f, 0.16f),
        "Quit_Game" to arrayOf(0.24f, 0.17f)
    )

    override fun show() {
        viewModel.loadTextures()
        viewBackground = viewModel.getTexture("Settings.png")

        Gdx.input.inputProcessor = stage
        createAndAddButtons()
        loadFonts()
        displayText()
        makeCancelWindow()
    }

    private fun createAndAddButtons() {
        var extraHeight = screenHeight * 0.17f
        for ((btnName, scaleInfo) in btnNamesAndScales) {
            if (btnName == "Quit_Game" && ScreenManager.peek() !is GameView) {
                continue
            }
            val btnImgUp: Texture
            val btnImgDown: Texture
            if (btnName == "Check_sound_effects") {
                if (SettingsManager.isSoundEffectOn()) {
                    // on --> green check box
                    btnImgUp = viewModel.getTexture("Check_Button.png")
                    btnImgDown = viewModel.getTexture("Check_Button_Pressed.png")
                } else {
                    // off --> empty box
                    btnImgUp = viewModel.getTexture("Empty_Button.png")
                    btnImgDown = viewModel.getTexture("Empty_Button_Pressed.png")
                }
            } else if (btnName == "Check_background_sound") {
                if (SettingsManager.isBackgroundSoundOn()) {
                    // on --> green check box
                    btnImgUp = viewModel.getTexture("Check_Button.png")
                    btnImgDown = viewModel.getTexture("Check_Button_Pressed.png")
                } else {
                    // off --> empty box
                    btnImgUp = viewModel.getTexture("Empty_Button.png")
                    btnImgDown = viewModel.getTexture("Empty_Button_Pressed.png")
                }
            } else {
                btnImgUp = viewModel.getTexture("${btnName}_Button.png")
                btnImgDown = viewModel.getTexture("${btnName}_Button_Pressed.png")
            }
            val btnImgStyle = ImageButton.ImageButtonStyle()
            btnImgStyle.imageUp = TextureRegionDrawable(btnImgUp)
            btnImgStyle.imageDown = TextureRegionDrawable(btnImgDown)
            val btnWidth = screenWidth * (scaleInfo[0])
            val btnHeight = screenHeight * (scaleInfo[1])
            val positionX : Float
            val positionY : Float
            when (btnName) {
                "Back" -> {
                    positionX = screenWidth * 0.09f - btnWidth / 2
                    positionY = screenHeight * 0.88f - btnHeight / 2
                }
                "Quit_Game" -> {
                    positionX = screenWidth * 0.5f - btnWidth / 2
                    positionY = screenHeight * 0.28f - btnHeight / 2
                }
                else -> {
                    positionX = screenWidth * 5 / 16 - btnWidth / 2
                    positionY = screenHeight * 12 / 16 - btnHeight / 2 - extraHeight
                    extraHeight += btnHeight
                }
            }
            val btn = ImageButton(btnImgStyle).apply {
                width = btnWidth
                height = btnHeight
                setPosition(positionX, positionY)
            }

            btn.onClick {
                when (btnName){
                    "Check_sound_effects" -> {
                        SettingsManager.setSoundEffect(!SettingsManager.isSoundEffectOn())
                        if (SettingsManager.isSoundEffectOn()){
                            btn.style.imageUp = TextureRegionDrawable(viewModel.getTexture("Check_Button.png"))
                            btn.style.imageDown = TextureRegionDrawable(viewModel.getTexture("Check_Button_Pressed.png"))
                        }
                        else {
                            btn.style.imageUp = TextureRegionDrawable(viewModel.getTexture("Empty_Button.png"))
                            btn.style.imageDown = TextureRegionDrawable(viewModel.getTexture("Empty_Button_Pressed.png"))
                        }
                    }
                    "Check_background_sound" -> {
                        SettingsManager.setBackgroundSound(!SettingsManager.isBackgroundSoundOn())
                        if (SettingsManager.isBackgroundSoundOn()){
                            btn.style.imageUp = TextureRegionDrawable(viewModel.getTexture("Check_Button.png"))
                            btn.style.imageDown = TextureRegionDrawable(viewModel.getTexture("Check_Button_Pressed.png"))
                            AudioManager.settingsTurnBackOnBackgroundMusic()
                        }
                        else {
                            btn.style.imageUp = TextureRegionDrawable(viewModel.getTexture("Empty_Button.png"))
                            btn.style.imageDown = TextureRegionDrawable(viewModel.getTexture("Empty_Button_Pressed.png"))
                            AudioManager.stopBackgroundMusic()
                        }
                    }
                    "Back" -> {
                        viewModel.handleBtnClick()
                    }
                    "Quit_Game" -> {
                        cancelWindow.isVisible = true
                        for (actor in stage.actors) {
                            if (actor != cancelWindow){
                                actor.isVisible = false
                            }
                        }
                        viewBackground = viewModel.getTexture("Settings_Blurred.png")
                    }
                }
                AudioManager.playClickBack()
            }
            stage.addActor(btn)
        }
    }

    private fun loadFonts(){
        viewModel.loadFonts(screenHeight)
        customFont = viewModel.getFont("Passion_One/PassionOne-Regular.ttf")
    }

    private fun makeWindowBtns(windowWidth : Float, windowHeight : Float): ArrayList<ImageButton> {
        val windowBtnsNamesAndScales = mapOf(
            "Cancel" to arrayOf(0.14f, 0.14f),
            "Quit_Game" to arrayOf(0.20f, 0.14f)
        )
        val windowBtns = ArrayList<ImageButton>()
        var extraWidth = 0f //make the buttons space out evenly by adding extra width
        for ((btnName, scaleInfo) in windowBtnsNamesAndScales) {
            val btnImgUp = viewModel.getTexture("${btnName}_Button.png")
            val btnImgDown = viewModel.getTexture("${btnName}_Button_Pressed.png")
            val btnImgStyle = ImageButton.ImageButtonStyle()
            btnImgStyle.imageUp = TextureRegionDrawable(btnImgUp)
            btnImgStyle.imageDown = TextureRegionDrawable(btnImgDown)
            val btnWidth = screenWidth * scaleInfo[0]
            val btnHeight = screenHeight * scaleInfo[1]
            val positionX = windowWidth * 0.25f - btnWidth / 2 + extraWidth
            val positionY = windowHeight * 0.23f - btnHeight / 2
            val btn = ImageButton(btnImgStyle).apply {
                width = btnWidth
                height = btnHeight
                setPosition(positionX, positionY)
            }
            extraWidth += btnWidth * 1.5f
            windowBtns.add(btn)
        }
        return windowBtns
    }

    private fun makeCancelWindow(){
        val windowStyle = Window.WindowStyle().apply {
            titleFont = customFont
            background = TextureRegionDrawable(viewModel.getTexture("Cancel_Window.png"))
        }
        cancelWindow = Window("", windowStyle).apply {
            isMovable = false
            val windowWidth = screenWidth * 0.48f
            val windowHeight = screenHeight * 0.47f
            setSize(windowWidth, windowHeight)
            setPosition(screenWidth * 0.5f - windowWidth/2, screenHeight*0.5f - windowHeight/2)
            val group = Group() //make a group so that the buttons retain their given positions
            group.setSize(windowWidth, windowHeight)
            add(group)
            val windowBtns = makeWindowBtns(windowWidth, windowHeight)
            for (i in 0..< windowBtns.size) {
                windowBtns[i].onClick {
                    cancelWindow.isVisible = false
                    for (actor in stage.actors) {
                        if (actor != cancelWindow){
                            actor.isVisible = true
                        }
                    }
                    viewBackground = viewModel.getTexture("Settings.png")
                    if (i == 1) {
                        ScreenManager.leaveGame()
                        println("Clicked quit game")
                    }
                }
                group.addActor(windowBtns[i])
            }
        }
        cancelWindow.isVisible = false
        stage.addActor(cancelWindow)
    }


    private fun displayText() {
        val positionX = screenWidth * 0.36f
        var positionY = screenHeight * 17 / 32 //- 300f
        displaySoundEffect(positionX, positionY)
        positionY -= screenHeight*0.15f
        displayBackgroundSound(positionX, positionY)
    }

    private fun displaySoundEffect(posX: Float, posY: Float) {
        val soundEffectLabel = Label("Sound Effect", Label.LabelStyle().apply {
                this.font = customFont
                this.fontColor = Color.BLACK
            }).apply {
                setPosition(posX, posY)
        }

        stage.addActor(soundEffectLabel)
    }
    private fun displayBackgroundSound(posX: Float, posY: Float) {
        val backgroundSoundLabel = Label("Background Music", Label.LabelStyle().apply {
            this.font = customFont
            this.fontColor = Color.BLACK
        }).apply {
            setPosition(posX, posY)
        }
        stage.addActor(backgroundSoundLabel)
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        batch.use {
            it.draw(viewBackground, 0f, 0f, screenWidth, screenHeight)
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
