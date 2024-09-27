package com.tdt4240.amazonwarriors.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class HealthComponent  : Component {
    var maxHealth = 100f
    var currentHealth = 0f

    companion object {
        val mapper = mapperFor<HealthComponent>()
    }

}
