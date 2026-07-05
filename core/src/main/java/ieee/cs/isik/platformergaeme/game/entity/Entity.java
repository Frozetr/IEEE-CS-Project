package ieee.cs.isik.platformergaeme.game.entity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import ieee.cs.isik.platformergaeme.game.materials.Material;import org.jetbrains.annotations.NotNull;


/**
 * This class represents any dynamic object in the game that going to be or has been registered to a server.
 */
public abstract class Entity implements Disposable {

    /// It's a unique integer, Neither of Entities can share same id.
    public final int id;

    /// Represents type of the entity
    public final int type;

    /// Represents name of the entity
    public String name;

    /// Represents health of entity. When reaches 0, entity dies.
    public float health;

    /// Represents maxHealth of entity. If it's 0, that means entity is immortal.
    public float maxHealth;

    /// Every entity should have a body so they can interact with each other physically
    @NotNull
    public final Body body;
    /// Holds color filter and Texture data(s) for render
    @NotNull
    public Material material;

    public boolean lookingLeft = false;

    public Entity(final int id, final int type, final String name, final float health, final float maxHealth, @NotNull final Body body, @NotNull Material material) {
        // Initialize the super class IEntity
        this.id = id;
        this.type = type;
        this.name = name;
        this.health = health;
        this.maxHealth = maxHealth;
        this.body = body;
        this.material = material;
    }

    /// Clean up resources
    @Override
    public void dispose() {
        body.getWorld().destroyBody(body);
    }
}
