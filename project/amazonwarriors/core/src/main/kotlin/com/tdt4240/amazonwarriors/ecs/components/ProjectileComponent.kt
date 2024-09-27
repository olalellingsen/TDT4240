package com.tdt4240.amazonwarriors.ecs.components
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor

class ProjectileComponent : Component {
    val totalTime = 2f
    var elapsedTime = 0f
    var damage = 0f
    var target: Entity? = null
    var team: Team? = null
    var peakHeight: Float = 20f

    companion object {
        val mapper = mapperFor<ProjectileComponent>()
    }
}
