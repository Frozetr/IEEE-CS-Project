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
    private final Body body;

    public Entity(final int id, final int type, final String name, final float health, final float maxHealth, @NotNull final Body body) {
        // Initialize the super class IEntity
        super(id, type, name, health, maxHealth);

        this.body = body;
    }

    @NotNull
    public Body getBody() {
        return body;
    }

    /// Clean up resources
    @Override
    public void dispose() {
        body.getWorld().destroyBody(body);
    }
}
