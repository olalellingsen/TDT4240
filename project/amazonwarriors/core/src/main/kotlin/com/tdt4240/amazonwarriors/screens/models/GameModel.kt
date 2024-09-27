package com.tdt4240.amazonwarriors.screens.models

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport

class GameModel {
    val worldWidth = 100f
    val worldHeight = Gdx.graphics.height / (Gdx.graphics.width / worldWidth)
    val pixelsPerUnit = 100f

    // Create the camera and viewport to handle scaling
    val camera = OrthographicCamera(worldWidth, worldHeight)
    val viewport = FitViewport(worldWidth, worldHeight, camera)
    val texturePaths = listOf(
        "Jaguar.png",
        "Monkey.png",
        "Banana.png",
        "Mother_Tree.png",
        "Tractor_Enemy.png",
        "Jeff_Enemy.png",
        "Jeff_Box.png",
        "Game_Background.png",
        "Game_UI/Jaguar_Button.png",
        "Game_UI/Jaguar_Button_Pressed.png",
        "Game_UI/Monkey_Button.png",
        "Game_UI/Monkey_Button_Pressed.png",
        "Game_UI/Settings_Button.png",
        "Game_UI/Settings_Button_Pressed.png",
        "healthbar_pics/healthbar10.png",
        "healthbar_pics/healthbar9.png",
        "healthbar_pics/healthbar8.png",
        "healthbar_pics/healthbar7.png",
        "healthbar_pics/healthbar6.png",
        "healthbar_pics/healthbar5.png",
        "healthbar_pics/healthbar4.png",
        "healthbar_pics/healthbar3.png",
        "healthbar_pics/healthbar2.png",
        "healthbar_pics/healthbar1.png",
    )

    val engine = Engine()
    val spriteBatch = SpriteBatch()
    val stage = Stage(viewport, spriteBatch)

    var difficultyScaling = 1f
}
