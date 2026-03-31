package ieee.cs.isik.platformergaeme.game;

import com.badlogic.gdx.physics.box2d.Body;
import org.jetbrains.annotations.NotNull;

public abstract class CarriableEntity extends Entity {

    /// this defines the carrier of this CarrierEntity, can be null
    public CharacterEntity carrier;

    public CarriableEntity(int id, int type, String name, float health, float maxHealth, @NotNull Body body, @NotNull Material material) {
        super(id, type, name, health, maxHealth, body, material);
    }

    /**
     * drops it's self
     * don't forget that this function makes carrier null!
     */
    public void drop() {
        carrier = null;
        // TODO: Must remove physics joints if exists
    }

    /**
     * This function uses the entity (e.g. throwing a bomb, consuming health potion)
     */
    public abstract void use();
}
