package com.tdt4240.amazonwarriors.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.tdt4240.amazonwarriors.ecs.components.AttackComponent
import com.tdt4240.amazonwarriors.ecs.components.DeadComponent
import com.tdt4240.amazonwarriors.ecs.components.HealthComponent
import com.tdt4240.amazonwarriors.ecs.components.PositionComponent
import com.tdt4240.amazonwarriors.ecs.components.ProjectileComponent
import com.tdt4240.amazonwarriors.ecs.components.ScaleComponent
import com.tdt4240.amazonwarriors.ecs.components.Team
import com.tdt4240.amazonwarriors.ecs.components.TeamComponent
import com.tdt4240.amazonwarriors.ecs.components.TextureComponent
import com.tdt4240.amazonwarriors.utils.AudioManager
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with

class CombatSystem : EntitySystem() {
    private lateinit var entities: ImmutableArray<Entity>
    private lateinit var targetEntities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(allOf(
            AttackComponent::class,
            PositionComponent::class,
            HealthComponent::class,
            TeamComponent::class,
        ).get())

        targetEntities = engine.getEntitiesFor(allOf(
            PositionComponent::class,
            HealthComponent::class,
            TeamComponent::class,
        ).get())
    }

    override fun update(deltaTime: Float) {
        entities.forEach attacker@ { attacker ->

            val attackComponent = attacker[AttackComponent.mapper] ?: return@attacker
            val entityATeam = attacker[TeamComponent.mapper]?.team ?: return@attacker
            val entityAPosition = attacker[PositionComponent.mapper]?.position?.x ?: return@attacker
            attackComponent.timeSinceLastAttack += deltaTime

            var closestEntity: Entity? = null
            var minDistance = Float.MAX_VALUE

            targetEntities.forEach target@ { target ->
                if (attacker == target) return@target // Skip self
                val entityBTeam = target[TeamComponent.mapper]?.team ?: return@target
                if (entityATeam == entityBTeam) return@target // Skip same team

                val entityBPosition = target[PositionComponent.mapper]?.position?.x ?: return@target
                val distance = kotlin.math.abs(entityAPosition - entityBPosition)

                if (distance < minDistance) {
                    minDistance = distance
                    closestEntity = target
                }
            }

            closestEntity?.let {
                val targetHealthComponent = it[HealthComponent.mapper] ?: return@let
                if (minDistance < attackComponent.range && attackComponent.timeSinceLastAttack > attackComponent.attackSpeed) {
                    attackComponent.timeSinceLastAttack = 0f

                    if (attackComponent.isRanged) {
                        println("CombatSystem: Entity launched projectile!")
                        launchProjectile(attacker, it, attackComponent.damage)
                    } else {
                        targetHealthComponent.currentHealth -= attackComponent.damage
                        println("CombatSystem: Entity attacked! Health left: ${targetHealthComponent.currentHealth}")
                    }
                }
                if (targetHealthComponent.currentHealth <= 0f) {
                    it.add(DeadComponent())
                }
            }
        }
    }

    private fun launchProjectile(shooter: Entity, target: Entity, damage: Float) {
        val shooterPosition = shooter[PositionComponent.mapper]?.position ?: return
        val shooterTeam = shooter[TeamComponent.mapper]?.team ?: return
        val scale = when (shooterTeam) {
            Team.ALLY -> 2f
            Team.ENEMY -> 3f
        }

        engine.entity {
            with<PositionComponent> { position.set(shooterPosition.x, -100f) } // Start the projectile offscreen to keep it out of view until the movement system moves it.
            with<TextureComponent> { texture = when (shooterTeam) {
                Team.ALLY -> "Banana.png"
                Team.ENEMY -> "Jeff_Box.png"
            } }
            with<ScaleComponent> { this.scale = scale }
            with<ProjectileComponent> { this.damage = damage; this.target = target; this.team = shooterTeam }
            with<TeamComponent> { team = shooterTeam }
        }
        AudioManager.playThrowSound()
    }
}
