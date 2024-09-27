package com.tdt4240.amazonwarriors.ecs.components

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class TeamComponent : Component {
    var team = Team.ALLY

    companion object {
        val mapper = mapperFor<TeamComponent>()
    }
}

enum class Team {
    ALLY, ENEMY
}
