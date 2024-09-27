package com.tdt4240.amazonwarriors.screens.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.tdt4240.amazonwarriors.screens.viewmodels.LeaderboardViewModel
import com.tdt4240.amazonwarriors.utils.AudioManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ktx.actors.onClick
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use
import kotlin.coroutines.CoroutineContext

class LeaderboardView: BaseView(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private var scoreFont = BitmapFont()

    private val viewModel = LeaderboardViewModel()
    private lateinit var background: Texture

    private val btnNamesAndScales = mapOf(
        "Back" to arrayOf(0.12f, 0.16f)
    )

    override fun show() {
        viewModel.loadTextures()
        background = viewModel.getTexture("Leaderboard.png")

        stage.clear()
        Gdx.input.inputProcessor = stage
        loadFonts()
        createAndAddButtons()
        launch {
            val leaderboardData = viewModel.fetchLeaderboardData()
            Gdx.app.postRunnable {
                displayLeaderboard(leaderboardData)
            }
        }
    }
    private fun createAndAddButtons() {
        for ((btnName, scaleInfo) in btnNamesAndScales) {
            val btnImgUp = viewModel.getTexture("${btnName}_Button.png")
            val btnImgDown = viewModel.getTexture("${btnName}_Button_Pressed.png")
            val btnImgStyle = ImageButton.ImageButtonStyle()
            btnImgStyle.imageUp = TextureRegionDrawable(btnImgUp)
            btnImgStyle.imageDown = TextureRegionDrawable(btnImgDown)
            val btn = ImageButton(btnImgStyle).apply {
                width = screenWidth * (scaleInfo[0])
                height = screenHeight * (scaleInfo[1])
                setPosition(screenWidth*0.09f - width/2, screenHeight*0.88f - height/2)
            }
            btn.onClick {
                viewModel.handleBtnClick()
                AudioManager.playClickBack()
            }
            stage.addActor(btn)
        }
    }

    private fun loadFonts(){
        viewModel.loadFonts(screenHeight)
        scoreFont = viewModel.getFont("Passion_One/PassionOne-Regular.ttf")
    }


    private fun displayLeaderboard(leaderboardData: List<String>) {
        val leaderboardTable = Table()
        for (entry in leaderboardData) {
            val label = Label(entry, Label.LabelStyle().apply {
                this.font = scoreFont
                this.fontColor = Color.BLACK
            })
            leaderboardTable.add(label).expandX().fillX().padBottom(10f)
            leaderboardTable.row()
        }

        val scrollPane = ScrollPane(leaderboardTable)
        scrollPane.setSize(screenWidth * 0.6f, screenHeight * 0.45f)
        scrollPane.setPosition(screenWidth * 0.3f, screenHeight * 0.2f)

        stage.addActor(scrollPane)
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
        job.cancel()
        viewModel.dispose()
    }
}
