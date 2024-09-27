package com.tdt4240.amazonwarriors.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class ScaleComponent : Component {
    var scale = 1f

    companion object {
        val mapper = mapperFor<ScaleComponent>()
    }
}
