package com.tdt4240.amazonwarriors.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class TextureComponent : Component {
    lateinit var texture: String

    companion object {
        val mapper = mapperFor<TextureComponent>()
    }
}
