package ieee.cs.isik.platformergaeme.game;

import com.badlogic.gdx.physics.box2d.Body;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents consumable entities like potions, foods Ωetc.
 */
public abstract class ConsumableEntity extends CarriableEntity {
    public ConsumableEntity(int id, int type, String name, float health, float maxHealth, @NotNull Body body, @NotNull Material material) {
        super(id, type, name, health, maxHealth, body, material);
    }

    @Override
    public abstract void use();

    @Override
    public void drop() {
        carrier.consumable = null;
        super.drop(); // carrier get been set to null after this call, so order is important
    }
}
