package com.tdt4240.amazonwarriors.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class BaseComponent: Component {

    lateinit var healthBar: String
    companion object {
        val mapper = mapperFor<BaseComponent>()
    }
}
