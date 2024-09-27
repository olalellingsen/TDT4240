package com.tdt4240.amazonwarriors.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.tdt4240.amazonwarriors.ecs.components.BaseComponent
import com.tdt4240.amazonwarriors.ecs.components.HealthComponent
import com.tdt4240.amazonwarriors.ecs.components.TeamComponent
import ktx.ashley.allOf
import ktx.ashley.get

class BaseHealthSystem: EntitySystem() {

    private lateinit var bases: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        bases = engine.getEntitiesFor(
            allOf(
                HealthComponent::class,
                BaseComponent::class,
                TeamComponent::class
            ).get()
        )
    }

    override fun update(deltaTime: Float) {
        bases.forEach { base ->
            val health = base[HealthComponent.mapper] ?: return@forEach
            val baseComponent = base[BaseComponent.mapper] ?: return@forEach
            baseComponent.healthBar = healthBar(health)
            }
        }


    private fun healthBar(health: HealthComponent): String {
        val currentHealth = health.currentHealth
        val maxHealth = health.maxHealth
        val healthPercent : Float = currentHealth / maxHealth
        val healthBarTexture: String = if (currentHealth == maxHealth) {
            "healthbar_pics/healthbar10.png" // Full health
        } else if (healthPercent >= 0.9f) {
            "healthbar_pics/healthbar9.png" // Mid health
        }
        else if (healthPercent >= 0.8f) {
            "healthbar_pics/healthbar8.png" // Mid health
        }
        else if (healthPercent >= 0.7f) {
            "healthbar_pics/healthbar7.png" // Mid health
        }
        else if (healthPercent >= 0.6f) {
            "healthbar_pics/healthbar6.png" // Mid health
        }
        else if (healthPercent >= 0.5f) {
            "healthbar_pics/healthbar5.png" // Mid health
        }
        else if (healthPercent >= 0.4f) {
            "healthbar_pics/healthbar4.png" // Mid health
        }
        else if (healthPercent >= 0.3f) {
            "healthbar_pics/healthbar3.png" // Mid health
        }
        else if (healthPercent >= 0.2f) {
            "healthbar_pics/healthbar2.png" // Mid health
        }
        else {
            "healthbar_pics/healthbar1.png" // Low health
        }
        return healthBarTexture
    }
}
