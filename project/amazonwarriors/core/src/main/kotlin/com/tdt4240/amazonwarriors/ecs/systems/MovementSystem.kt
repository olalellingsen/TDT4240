package com.tdt4240.amazonwarriors.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.tdt4240.amazonwarriors.ecs.components.CollisionComponent
import com.tdt4240.amazonwarriors.ecs.components.PositionComponent
import com.tdt4240.amazonwarriors.ecs.components.SpeedComponent
import ktx.ashley.allOf
import ktx.ashley.get

class MovementSystem : EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(allOf(
            PositionComponent::class,
            SpeedComponent::class,
            CollisionComponent::class
        ).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val position = entity[PositionComponent.mapper] ?: continue
            val speed = entity[SpeedComponent.mapper] ?: continue
            val collision = entity[CollisionComponent.mapper] ?: continue

            if (collision.canMove) {
                position.position.add(speed.speed * deltaTime, 0f)
                collision.bounds.setPosition(position.position)
            }
            collision.canMove = true
        }
    }
}
