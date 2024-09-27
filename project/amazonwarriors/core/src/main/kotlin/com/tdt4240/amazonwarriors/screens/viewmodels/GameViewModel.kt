package com.tdt4240.amazonwarriors.screens.viewmodels

import RenderSystem
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Timer
import com.tdt4240.amazonwarriors.ecs.components.AttackComponent
import com.tdt4240.amazonwarriors.ecs.components.BaseComponent
import com.tdt4240.amazonwarriors.ecs.components.CollisionComponent
import com.tdt4240.amazonwarriors.ecs.components.CurrencyComponent
import com.tdt4240.amazonwarriors.ecs.components.HealthComponent
import com.tdt4240.amazonwarriors.ecs.components.PositionComponent
import com.tdt4240.amazonwarriors.ecs.components.ScaleComponent
import com.tdt4240.amazonwarriors.ecs.components.SpeedComponent
import com.tdt4240.amazonwarriors.ecs.components.Team
import com.tdt4240.amazonwarriors.ecs.components.TeamComponent
import com.tdt4240.amazonwarriors.ecs.components.TextureComponent
import com.tdt4240.amazonwarriors.ecs.systems.BaseHealthSystem
import com.tdt4240.amazonwarriors.ecs.systems.CollisionSystem
import com.tdt4240.amazonwarriors.ecs.systems.CombatSystem
import com.tdt4240.amazonwarriors.ecs.systems.DeathSystem
import com.tdt4240.amazonwarriors.ecs.systems.MovementSystem
import com.tdt4240.amazonwarriors.ecs.systems.ProjectileCollisionSystem
import com.tdt4240.amazonwarriors.ecs.systems.ProjectileMovementSystem
import com.tdt4240.amazonwarriors.screens.models.GameModel
import com.tdt4240.amazonwarriors.utils.AudioManager
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.assets.disposeSafely
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders
import ktx.graphics.use
import kotlin.random.Random
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle

class GameViewModel {
    internal val model = GameModel()
    private val assetManager = AssetManager()
    private lateinit var backgroundTexture: Texture

    private lateinit var moneyLabelFont : BitmapFont
    private lateinit var moneyLabelStyle : LabelStyle

    init {
        model.spriteBatch.projectionMatrix = model.camera.combined
        loadGameAssets()
        createBases()

        // Add systems here as they are created
        model.engine.apply {
            addSystem(CollisionSystem())
            addSystem(MovementSystem())
            addSystem(ProjectileMovementSystem())
            addSystem(ProjectileCollisionSystem())
            addSystem(CombatSystem())
            addSystem(DeathSystem())
            addSystem(RenderSystem(model.spriteBatch, 1/model.pixelsPerUnit, model, assetManager))
            addSystem(BaseHealthSystem())
        }
    }

    private fun loadMoneyLabelFont(screenHeight: Float){
        assetManager.registerFreeTypeFontLoaders()
        assetManager.registerFreeTypeFontLoaders(fileExtensions = arrayOf(".custom"))
        assetManager.registerFreeTypeFontLoaders(replaceDefaultBitmapFontLoader = true)
        assetManager.loadFreeTypeFont("Passion_One/PassionOne-Regular.ttf")  {
            size = (screenHeight * 0.075f).toInt()
            borderWidth = (size/25).toFloat()
            borderColor = Color.valueOf("4d2d13")
        }
        assetManager.finishLoading()
        moneyLabelFont = assetManager.get("Passion_One/PassionOne-Regular.ttf")
    }

    private fun createBases() {
        val boundSize = 10f
        val baseHealth = 1000f
        createMotherTree(boundSize, baseHealth)
        createAmazonBase(boundSize, baseHealth)
    }

    private fun createMotherTree(boundSize: Float, baseHealth: Float) {
        model.engine.entity {
            with<BaseComponent> { healthBar = "healthbar_pics/healthbar10.png" }
            with<PositionComponent> { position.set(5f, 2f) }
            with<CollisionComponent> { bounds.set(0f, 2f, boundSize, boundSize); anchor.set(boundSize/2, boundSize/2) }
            with<TextureComponent> { texture = "Mother_Tree.png" }
            with<HealthComponent> { maxHealth = baseHealth; currentHealth = baseHealth }
            with<TeamComponent> { team = Team.ALLY }
            with<CurrencyComponent> { money = 100 }
            with<ScaleComponent> { scale = 2.85f }
        }
    }

    private fun createAmazonBase(boundSize: Float, baseHealth: Float) {
        model.engine.entity {
            with<BaseComponent>()
            with<PositionComponent> { position.set(95f, 2f) }
            with<CollisionComponent> { bounds.set(95f, 2f, boundSize, boundSize); anchor.set(boundSize/2, boundSize/2) }
            with<HealthComponent> { maxHealth = baseHealth; currentHealth = baseHealth }
            with<TeamComponent> { team = Team.ENEMY }
        }
    }

    fun spawnRandomEnemy() {
        if (Random.nextBoolean()) {
            spawnMeleeUnit(Team.ENEMY)
        } else {
            spawnRangedUnit(Team.ENEMY)
        }
    }

    private fun spawnMeleeUnit(
        team: Team,
    ) {
        val scale = 1.5f
        val boundSize = if (team == Team.ALLY) 5f * scale else 10f * scale
        val speed = 5f
        val health = 150f
        val damage = if (team == Team.ALLY) 15f else 15f * model.difficultyScaling
        val range = 10f
        val startPosition = if (team == Team.ALLY) 7f else 92f
        val randomOffset = Random.nextFloat() * 0.01f - 0.005f // Random offset to avoid units spawning on top of each other

        model.engine.entity {
            with<PositionComponent> { position.set(startPosition + randomOffset, 2f) }
            with<SpeedComponent> {
                this.speed = when (team) {
                    Team.ALLY -> speed
                    Team.ENEMY -> -speed
                }
            }
            with<CollisionComponent> { bounds.set(startPosition + randomOffset, 2f, boundSize, boundSize); anchor.set(boundSize/2, boundSize/2) }
            with<TextureComponent> {
                texture = when (team) {
                    Team.ALLY -> "Jaguar.png"
                    Team.ENEMY -> "Tractor_Enemy.png"
                }
            }
            with<HealthComponent> { maxHealth = health; currentHealth = health }
            with<AttackComponent> { this.damage = damage; this.range = range; isRanged = false }
            with<TeamComponent> { this.team = team }
            with<ScaleComponent> {this.scale = scale}
        }
        when(team) {
            Team.ALLY -> AudioManager.playJaguarSound()
            Team.ENEMY -> AudioManager.playTruckSound()
        }
    }

    private fun spawnRangedUnit(team: Team) {
        val scale = 1.5f
        val boundSize = if (team == Team.ALLY) 5f * scale else 10f * scale
        val speed = 5f
        val health = 80f
        val damage = if (team == Team.ALLY) 10f else 10f * model.difficultyScaling
        val range = 25f

        val startPosition = if (team == Team.ALLY) 7f else 92f
        val randomOffset = Random.nextFloat() * 0.01f - 0.005f // Random offset to avoid units spawning on top of each other

        model.engine.entity {
            with<PositionComponent> { position.set(startPosition + randomOffset, 2f) }
            with<SpeedComponent> {
                this.speed = when (team) {
                    Team.ALLY -> speed
                    Team.ENEMY -> -speed
                }
            }
            with<CollisionComponent> { bounds.set(startPosition + randomOffset, 2f, boundSize, boundSize); anchor.set(boundSize/2, boundSize/2) }
            with<TextureComponent> {
                texture = when (team) {
                    Team.ALLY -> "Monkey.png"
                    Team.ENEMY -> "Jeff_Enemy.png"
                }
            }
            with<HealthComponent> { maxHealth = health; currentHealth = health }
            with<AttackComponent> { this.damage = damage; this.range = range; isRanged = true }
            with<TeamComponent> { this.team = team }
            with<ScaleComponent> {this.scale = scale}
        }
        when(team) {
            Team.ALLY -> AudioManager.playMonkeySound()
            Team.ENEMY -> AudioManager.playJeffSound()
        }
    }

    fun handleBuyUnit(unitName: String) {
        val playerBase = model.engine.getEntitiesFor(allOf(BaseComponent::class, TeamComponent::class, CurrencyComponent::class).get()).first()
        val currencyComponent = playerBase[CurrencyComponent.mapper] ?: return
        val cost = when (unitName) {
            "Jaguar" -> 10
            "Monkey" -> 15
            else -> return
        }
        if (currencyComponent.money < cost) {
            println("Not enough money")
            return
        }
        currencyComponent.money -= cost
        when (unitName) {
            "Jaguar" -> {
                spawnMeleeUnit(Team.ALLY)
                println("Summoned melee unit")
            }
            "Monkey" -> {
                spawnRangedUnit(Team.ALLY)
                println("Summoned ranged unit")
            }
        }
    }

    fun getMoney(): String {
        val playerBase = model.engine.getEntitiesFor(allOf(BaseComponent::class, TeamComponent::class, CurrencyComponent::class).get()).first()
        val currencyComponent = playerBase[CurrencyComponent.mapper] ?: return "Error"
        return currencyComponent.money.toString()
    }

    fun getEntities(): ImmutableArray<Entity> = model.engine.entities

    fun update(delta: Float) {
        model.apply {
            camera.update()
            spriteBatch.projectionMatrix = camera.combined
            spriteBatch.use {
                spriteBatch.draw(backgroundTexture, 0f, 0f, worldWidth, worldHeight)
                engine.update(delta)
            }
            stage.act()
            stage.draw()
        }
    }

    fun handleResize(width: Int, height: Int) {
        model.apply {
            viewport.update(width, height, true)
            stage.viewport.update(width, height, true)
        }
    }

    fun dispose() {
        model.apply {
            spriteBatch.disposeSafely()
            stage.disposeSafely()
        }
        assetManager.disposeSafely()
    }

    fun loopSpawnEnemies() {
        Timer.schedule(object : Timer.Task() {
            override fun run() {
                spawnRandomEnemy()
                loopSpawnEnemies()
                model.difficultyScaling *= 1.1f
            }
        }, 5f)
    }

    private fun loadGameAssets() {
        model.texturePaths.forEach { path ->
            assetManager.load(path, Texture::class.java)
        }
        assetManager.finishLoading()
        backgroundTexture = assetManager.get("Game_Background.png")
    }

    fun createMoneyLabel(screenHeight: Float, screenWidth: Float): Label {
        createMoneyLabelStyle(screenHeight)
        return Label("", moneyLabelStyle).apply {
            setPosition(screenWidth * 0.06f, screenHeight * 0.76f)
        }
    }

    private fun createMoneyLabelStyle(screenHeight: Float){
        loadMoneyLabelFont(screenHeight)
        moneyLabelStyle = LabelStyle().apply {
            font = moneyLabelFont
            fontColor = Color.valueOf("fcc015")
        }
    }

    fun getTexture(texturePath: String): Texture = assetManager.get(texturePath)

}
