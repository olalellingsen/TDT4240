package com.tdt4240.amazonwarriors.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import ktx.ashley.mapperFor

class CollisionComponent : Component {
    val bounds = Rectangle()
    var canMove = true
    val anchor = Vector2(0.5f, 0.5f)

    companion object {
        val mapper = mapperFor<CollisionComponent>()
    }
}
