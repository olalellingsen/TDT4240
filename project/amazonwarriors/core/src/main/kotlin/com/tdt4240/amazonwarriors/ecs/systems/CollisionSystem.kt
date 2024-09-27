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

class CollisionSystem: EntitySystem() {
    private lateinit var entities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(allOf(
            CollisionComponent::class,
            PositionComponent::class,
        ).get())
    }

    override fun update(deltaTime: Float) {
        for (i in 0 until entities.size()) {
            for (j in 0 until entities.size()) {
                if (i == j) continue
                val entityA = entities[i]
                val entityB = entities[j]

                val collisionA = entityA[CollisionComponent.mapper] ?: continue
                val collisionB = entityB[CollisionComponent.mapper] ?: continue
                val positionA = entityA[PositionComponent.mapper]?.position?.cpy()?.add(collisionA.anchor) ?: continue
                val positionB = entityB[PositionComponent.mapper]?.position?.cpy()?.add(collisionB.anchor) ?: continue
                val speedA = entityA[SpeedComponent.mapper]?.speed ?: 0f

                if (collisionA.bounds.overlaps(collisionB.bounds) && collisionA.canMove) {
                    collisionA.canMove = checkCollisionDirection(speedA, positionB.x, positionA.x)
                }
            }
        }
    }

    private fun checkCollisionDirection(speed: Float, x1: Float, x2: Float): Boolean {
        val movingRight = speed > 0
        val movingLeft = speed < 0
        val aIsRightOfB = x1 > x2
        val aIsLeftOfB = x1 < x2
        return movingRight && aIsLeftOfB || movingLeft && aIsRightOfB
    }
}
