import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.tdt4240.amazonwarriors.ecs.components.BaseComponent
import com.tdt4240.amazonwarriors.ecs.components.HealthComponent
import com.tdt4240.amazonwarriors.ecs.components.PositionComponent
import com.tdt4240.amazonwarriors.ecs.components.ScaleComponent
import com.tdt4240.amazonwarriors.ecs.components.TextureComponent
import com.tdt4240.amazonwarriors.screens.models.GameModel
import ktx.ashley.allOf
import ktx.ashley.get

class RenderSystem(private val spriteBatch: SpriteBatch, private val unitsPerPixel: Float, private val gameModel: GameModel, private val assetManager: AssetManager) : EntitySystem() {
    private lateinit var entities: ImmutableArray<Entity>
    private lateinit var motherTree: Entity

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(allOf(
            PositionComponent::class,
            TextureComponent::class
        ).get())
        motherTree = engine.getEntitiesFor(allOf(HealthComponent::class, BaseComponent::class).get()).first()
    }

    override fun update(deltaTime: Float) {
        for (entity in entities) {
            val position = entity[PositionComponent.mapper] ?: continue
            val texture = assetManager.get<Texture>(entity[TextureComponent.mapper]?.texture) ?: continue
            val scale = entity[ScaleComponent.mapper]?.scale ?: 1f
            val baseComponent = entity.get(BaseComponent.mapper)
            val offset = if (baseComponent != null) 15f else 0f

            val textureWidth = texture.width * unitsPerPixel * scale
            val textureHeight = texture.height * unitsPerPixel * scale
            spriteBatch.draw(texture, position.position.x-offset, position.position.y, textureWidth, textureHeight)
        }
        drawHealthBar()
    }

    private fun drawHealthBar() {
        val healthBarTexture = assetManager.get<Texture>(motherTree[BaseComponent.mapper]?.healthBar ?: return)

        val healthBarWidth = gameModel.worldWidth * 0.4f
        val healthBarHeight = gameModel.worldHeight * 0.2f
        val healthBarX = gameModel.worldWidth * 0.03f
        val healthBarY = gameModel.worldHeight * 0.97f - healthBarHeight

        spriteBatch.draw(healthBarTexture, healthBarX, healthBarY, healthBarWidth, healthBarHeight)
    }
}
