package com.tdt4240.amazonwarriors.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.tdt4240.amazonwarriors.ecs.components.BaseComponent
import com.tdt4240.amazonwarriors.ecs.components.CurrencyComponent
import com.tdt4240.amazonwarriors.ecs.components.DeadComponent
import com.tdt4240.amazonwarriors.ecs.components.Team
import com.tdt4240.amazonwarriors.ecs.components.TeamComponent
import com.tdt4240.amazonwarriors.utils.AudioManager
import com.tdt4240.amazonwarriors.utils.ScreenManager
import ktx.ashley.allOf
import ktx.ashley.get

/*
* This system is added so that it is possible to add features that react to death
* such as death animations
*/
class DeathSystem : EntitySystem() {
    private lateinit var entities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(allOf(DeadComponent::class).get())
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            println("DeathSystem: ${entity.components}")
            // Get the player base
            val playerBase = engine.getEntitiesFor(allOf(BaseComponent::class, TeamComponent::class, CurrencyComponent::class).get()).first()
            val currencyComponent = playerBase[CurrencyComponent.mapper] ?: return

            if (entity[BaseComponent.mapper] != null && entity[TeamComponent.mapper]?.team == Team.ALLY) {
                println("DeathSystem: Game Over!")
                ScreenManager.setEndGame(currencyComponent.totalMoney)
                AudioManager.playMenuMusic()
            } else {
                val team = entity[TeamComponent.mapper]?.team ?: return
                if (team == Team.ENEMY) {
                    currencyComponent.money += 15 // Reward for killing an enemy
                    currencyComponent.totalMoney += 15
                }
                println("DeathSystem: Entity died!")
                engine.removeEntity(entity)
            }
        }
    }
}
