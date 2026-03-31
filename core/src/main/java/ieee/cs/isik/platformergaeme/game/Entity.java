package ieee.cs.isik.platformergaeme.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import org.jetbrains.annotations.NotNull;


/**
 * This class represents any dynamic object in the game that going to be or has been registered to a server.
 * This class is inherited from {@link IEntity} and has extra data that required by client side.
 *
 * @see IEntity
 */
public abstract class Entity extends IEntity implements Disposable {

    /// Every entity should have a body so they can interact with each other physically
    @NotNull
    public final Body body;

    /// Holds color filter and Texture data(s) for render
    @NotNull
    public Material material;

    public Entity(final int id, final int type, final String name, final float health, final float maxHealth, @NotNull final Body body, @NotNull Material material) {
        // Initialize the super class IEntity
        super(id, type, name, health, maxHealth);

        this.body = body;
        this.material = material;
    }

    /// Clean up resources
    @Override
    public void dispose() {
        body.getWorld().destroyBody(body);
    }
}
