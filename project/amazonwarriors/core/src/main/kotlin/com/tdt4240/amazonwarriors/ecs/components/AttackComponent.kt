package com.tdt4240.amazonwarriors.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class AttackComponent : Component {
    var damage = 0f
    var range = 0f
    var attackSpeed = 1f // Time in seconds between attacks
    var timeSinceLastAttack = Float.MAX_VALUE // Time in seconds since last attack
    var isRanged = false

    companion object {
        val mapper = mapperFor<AttackComponent>()
    }
}
