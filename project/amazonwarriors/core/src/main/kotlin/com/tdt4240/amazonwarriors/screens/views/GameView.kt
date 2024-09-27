package com.tdt4240.amazonwarriors.screens.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.tdt4240.amazonwarriors.ecs.components.CollisionComponent
import com.tdt4240.amazonwarriors.ecs.components.Team
import com.tdt4240.amazonwarriors.ecs.components.TeamComponent
import com.tdt4240.amazonwarriors.screens.viewmodels.GameViewModel
import com.tdt4240.amazonwarriors.utils.AudioManager
import com.tdt4240.amazonwarriors.utils.ScreenManager
import ktx.actors.onClick
import ktx.ashley.get

class GameView: BaseView() {

    private val viewModel = GameViewModel()
    private val debugShapeRenderer = ShapeRenderer()
    private val debug = false

    private val buttonNames = listOf(
        "Jaguar",
        "Monkey",
        "Settings"
    )

    private lateinit var moneyLabel: Label

    init {
        viewModel.loopSpawnEnemies()
    }

    override fun show(){
        Gdx.input.inputProcessor = stage
        createAndAddButtons()

        moneyLabel = viewModel.createMoneyLabel(screenHeight, screenWidth)
        stage.addActor(moneyLabel)

        AudioManager.playGameMusic()
    }

    override fun render(delta: Float) {
        updateMoneyLabel(viewModel.getMoney())
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewModel.update(delta)
        if (debug) createCollisionRectangles()
        stage.draw()
    }

    private fun createCollisionRectangles() {
        debugShapeRenderer.projectionMatrix = viewModel.model.camera.combined
        debugShapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        val entities = viewModel.getEntities()
        for (entity in entities) {
            if (entity[TeamComponent.mapper]?.team == Team.ENEMY) {
                debugShapeRenderer.color = Color.RED
            } else {
                debugShapeRenderer.color = Color.BLUE
            }
            val collision = entity[CollisionComponent.mapper] ?: continue
            debugShapeRenderer.rect(collision.bounds.x, collision.bounds.y, collision.bounds.width, collision.bounds.height)
        }
        debugShapeRenderer.end()
    }

    override fun resize(width: Int, height: Int) {
        viewModel.handleResize(width, height)
    }


    override fun dispose() {
        debugShapeRenderer.dispose()
        viewModel.dispose()
    }

    private fun createAndAddButtons() {
        var extraWidth = 200f // Used to ensure correct distance between play button and the others
        for (buttonName in buttonNames) {
            val buttonImageUp = viewModel.getTexture("Game_UI/${buttonName}_Button.png")
            val buttonImageDown = viewModel.getTexture("Game_UI/${buttonName}_Button_Pressed.png")
            val imageButtonStyle = ImageButton.ImageButtonStyle().apply {
                imageUp = TextureRegionDrawable(buttonImageUp)
                imageDown = TextureRegionDrawable(buttonImageDown)
            }
            val button = ImageButton(imageButtonStyle).apply {
                if (buttonName == "Settings") {
                    width = screenWidth * 0.09f
                    height = screenHeight * 0.18f
                    setPosition(screenWidth * 0.6f - width / 2 + extraWidth, screenHeight * 0.87f - height / 2)
                }
                else {
                    width = screenWidth * 0.135f
                    height = screenHeight * 0.3f
                    setPosition(screenWidth * 0.6f - width / 2 + extraWidth, screenHeight * 0.85f - height / 2)
                }
            }
            extraWidth += button.width * 0.9f
            button.onClick {
                if (buttonName == "Settings") {
                    ScreenManager.push(SettingsView())
                }
                else {
                    viewModel.handleBuyUnit(buttonName)
                }
            }
            stage.addActor(button)
        }
    }

    private fun updateMoneyLabel(money: String) {
        moneyLabel.setText("Money: $money")
    }
}
