package com.tdt4240.amazonwarriors.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import ktx.ashley.mapperFor

class PositionComponent : Component {
    val position: Vector2 = Vector2(0f, 2f)

    companion object {
        val mapper = mapperFor<PositionComponent>()
    }
}
