package com.tdt4240.amazonwarriors.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Vector2
import com.tdt4240.amazonwarriors.ecs.components.PositionComponent
import com.tdt4240.amazonwarriors.ecs.components.ProjectileComponent
import ktx.ashley.allOf
import ktx.ashley.get
import kotlin.math.PI
import kotlin.math.sin

class ProjectileMovementSystem : EntitySystem() {
    private lateinit var entities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(allOf(
            ProjectileComponent::class,
            PositionComponent::class
        ).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val position = entity[PositionComponent.mapper]?.position ?: continue
            val projectile = entity[ProjectileComponent.mapper] ?: continue
            val targetPositionComponent = projectile.target?.get(PositionComponent.mapper)

            projectile.elapsedTime += deltaTime

            val t = projectile.elapsedTime / projectile.totalTime // Normalized time [0,1]
            val deltaTimeProportion = deltaTime / projectile.totalTime

            if (targetPositionComponent != null) {
                val targetPosition = targetPositionComponent.position
                val newPosition = calculateNewPosition(t, deltaTimeProportion, position.x, targetPosition.x, projectile)

                position.set(newPosition)
                if (t >= 1) {
                    println("ProjectileMovementSystem: Projectile reached target!")
                    engine.removeEntity(entity)
                }

            } else {
                println("ProjectileMovementSystem: Projectile has no target!")
                engine.removeEntity(entity)
            }
        }
    }

    private fun calculateNewPosition(
        t: Float,
        deltaTimeProportion: Float,
        positionX: Float,
        targetPositionX: Float,
        projectile: ProjectileComponent
    ): Vector2 {
        val distanceX = targetPositionX - positionX
        val estimatedTotalDistanceX = distanceX / (1-t)
        val deltaX = deltaTimeProportion * estimatedTotalDistanceX
        val newX = positionX + deltaX

        // Use sinusoidal function for Y movement to simulate smooth arc
        val newY = projectile.peakHeight * sin(PI * t).toFloat() + 5f
        return Vector2(newX, newY)
    }
}
