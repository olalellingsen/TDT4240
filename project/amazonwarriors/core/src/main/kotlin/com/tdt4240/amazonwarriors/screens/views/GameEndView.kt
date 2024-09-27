package com.tdt4240.amazonwarriors.screens.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.tdt4240.amazonwarriors.screens.viewmodels.GameEndViewModel
import ktx.actors.onClick
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class GameEndView(private val score: Int): BaseView() {
    private lateinit var background: Texture
    private val viewModel = GameEndViewModel()

    private val btnNamesAndScales = mapOf(
        "Add_To_Leaderboard" to arrayOf(0.35f, 0.17f),
        "Main_Menu" to arrayOf(0.24f, 0.17f)
    )

    private lateinit var nameAndScoreFont : BitmapFont
    private lateinit var errorMessageFont : BitmapFont
    private lateinit var nameField : TextField
    private lateinit var scoreLabel : Label
    private lateinit var errorMessageLabel : Label

    override fun show() {
        viewModel.loadTextures()
        background = viewModel.getTexture("Game_End/Game_Over.png")
        Gdx.input.inputProcessor = stage
        createAndAddButtons()
        createTextFieldsAndLabels()
    }

    private fun createAndAddButtons() {
        var extraHeight = 0f // Used to ensure correct distance between buttons
        for ((btnName, scaleInfo) in btnNamesAndScales) {
            val btnImgUp = viewModel.getTexture("Game_End/${btnName}.png")
            val btnImgDown = viewModel.getTexture("Game_End/${btnName}_Pressed.png")
            val btnImgStyle = ImageButton.ImageButtonStyle()
            btnImgStyle.imageUp = TextureRegionDrawable(btnImgUp)
            btnImgStyle.imageDown = TextureRegionDrawable(btnImgDown)
            val btn = ImageButton(btnImgStyle).apply {
                width = screenWidth * (scaleInfo[0])
                height = screenHeight * (scaleInfo[1])
                setPosition(screenWidth * 0.8f - width / 2, screenHeight * 0.54f - height / 2 - extraHeight)
            }
            extraHeight += btn.height*0.95f
            btn.onClick {
                when (btnName){
                    "Add_To_Leaderboard" -> {
                        if (nameField.text.isNotEmpty()){
                            btn.style.imageUp = TextureRegionDrawable(viewModel.getTexture("Game_End/Added_To_Leaderboard.png"))
                            btn.style.imageDown = TextureRegionDrawable(viewModel.getTexture("Game_End/Added_To_Leaderboard_Pressed.png"))
                            viewModel.addToLeaderBoard(nameField.text, score)
                        }
                        else if (nameField.text.isEmpty()){
                            errorMessageLabel.setText("You need to write your name to \nadd to the leaderboard")
                            errorMessageLabel.isVisible = true
                        }
                    }
                    "Main_Menu" -> {
                        viewModel.handleMainMenuClick()
                    }
                }
            }
            stage.addActor(btn)
        }
    }

    private fun createTextFieldsAndLabels(){
        loadFonts()
        makeErrorMessageLabel()
        makeScoreLabel()
        makeNameField()

        //add a restriction of 15 characters for the name
        nameField.textFieldFilter = TextField.TextFieldFilter { textField, _ ->
            textField.text.length < 15
        }
        nameField.addListener(object : ChangeListener(){
            override fun changed(event: ChangeEvent?, actor: Actor?){
                if (nameField.text.length >= 15){
                    errorMessageLabel.setText("Your name can´t be longer than 15 characters")
                    errorMessageLabel.isVisible = true
                }
                else {
                    errorMessageLabel.isVisible = false
                }
                println(nameField.text)
            }
        })
    }

    private fun loadFonts(){
        viewModel.loadFonts(screenHeight)
        nameAndScoreFont = viewModel.getFont("Passion_One/PassionOne-Regular.ttf")
        errorMessageFont = viewModel.getFont("Passion_One/PassionOne-Regular2.ttf")
    }

    private fun makeErrorMessageLabel(){
        val errorMessageStyle = LabelStyle().apply {
            font = errorMessageFont
            fontColor = Color.valueOf("b83229")
        }
        errorMessageLabel = Label("", errorMessageStyle).apply {
            setPosition(screenWidth*0.11f, screenHeight*0.28f)
            isVisible = false
        }
        stage.addActor(errorMessageLabel)
    }

    private fun makeNameField(){
        val nameFieldStyle = TextFieldStyle().apply {
            font = nameAndScoreFont
            fontColor = Color.valueOf("ff6d0a")
            messageFontColor = Color.valueOf("ff6d0a")

        }
        nameField = TextField("", nameFieldStyle).apply{
            setSize(screenWidth*0.32f, screenHeight*0.1f)
            setPosition(screenWidth*0.23f, screenHeight*0.337f)
            messageText = "Write your name here"
        }
        stage.addActor(nameField)
    }

    private fun makeScoreLabel(){
        val scoreLabelStyle = LabelStyle().apply {
            font = nameAndScoreFont
            fontColor = Color.valueOf("ff6d0a")
        }
        scoreLabel = Label("", scoreLabelStyle).apply {
            setPosition(screenWidth*0.31f, screenHeight*0.52f)
        }
        scoreLabel.setText(this.score)
        stage.addActor(scoreLabel)
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        batch.use {
            it.draw(background, 0f, 0f, screenWidth, screenHeight)
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
