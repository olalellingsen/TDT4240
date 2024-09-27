package com.tdt4240.amazonwarriors.screens.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxScreen

abstract class BaseView: KtxScreen {

    protected val batch: SpriteBatch = SpriteBatch()
    protected val stage: Stage = Stage()
    protected val screenWidth = Gdx.graphics.width.toFloat()
    protected val screenHeight = Gdx.graphics.height.toFloat()

    override fun hide() {
        stage.clear()
    }
}
