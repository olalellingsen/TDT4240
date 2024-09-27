package com.tdt4240.amazonwarriors.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.tdt4240.amazonwarriors.ecs.components.DeadComponent
import com.tdt4240.amazonwarriors.ecs.components.HealthComponent
import com.tdt4240.amazonwarriors.ecs.components.PositionComponent
import com.tdt4240.amazonwarriors.ecs.components.ProjectileComponent
import com.tdt4240.amazonwarriors.ecs.components.TeamComponent
import ktx.ashley.allOf
import ktx.ashley.get

class ProjectileCollisionSystem : EntitySystem() {

    private lateinit var projectiles: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        projectiles = engine.getEntitiesFor(allOf(
            ProjectileComponent::class,
            PositionComponent::class,
            TeamComponent::class
        ).get())
    }

    override fun update(deltaTime: Float) {
        projectiles.forEach { projectile ->
            val projectileComponent = projectile[ProjectileComponent.mapper] ?: return@forEach
            val projectilePosition = projectile[PositionComponent.mapper]?.position ?: return@forEach
            val target = projectileComponent.target ?: return@forEach
            val targetPosition = target[PositionComponent.mapper]?.position ?: return@forEach

            if (skipFriendlyFire(projectile, target)) return@forEach

            if (projectilePosition.dst(targetPosition) < 7f) {
                damageTarget(target, projectileComponent)
                engine.removeEntity(projectile)
            }
        }
    }

    private fun damageTarget(
        target: Entity,
        projectileComponent: ProjectileComponent,
    ) {
        val targetHealthComponent = target[HealthComponent.mapper] ?: return
        targetHealthComponent.currentHealth -= projectileComponent.damage
        println("ProjectileCollisionSystem: Projectile hit target! Target health: ${targetHealthComponent.currentHealth}")
        if (targetHealthComponent.currentHealth <= 0f) {
            target.add(DeadComponent())
        }
    }

    private fun skipFriendlyFire(projectile: Entity?, target: Entity): Boolean {
        val projectileTeam = projectile?.get(TeamComponent.mapper)?.team
        val targetTeam = target[TeamComponent.mapper]?.team
        return projectileTeam == targetTeam
    }
}
