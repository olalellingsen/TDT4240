package com.tdt4240.amazonwarriors.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class DeadComponent : Component {

    companion object {
        val mapper = mapperFor<DeadComponent>()
    }
}
