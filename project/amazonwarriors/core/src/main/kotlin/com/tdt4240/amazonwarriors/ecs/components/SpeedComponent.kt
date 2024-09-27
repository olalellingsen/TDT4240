package com.tdt4240.amazonwarriors.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class SpeedComponent : Component {
    var speed: Float = 0f

    companion object {
        val mapper = mapperFor<SpeedComponent>()
    }
}
