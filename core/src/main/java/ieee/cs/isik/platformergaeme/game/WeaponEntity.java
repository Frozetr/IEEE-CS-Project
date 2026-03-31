package ieee.cs.isik.platformergaeme.game;

import com.badlogic.gdx.physics.box2d.Body;
import org.jetbrains.annotations.NotNull;

public abstract class WeaponEntity extends CarriableEntity {

    public final float coolDown;

    public WeaponEntity(int id, int type, String name, float health, float maxHealth, @NotNull Body body, @NotNull Material material, final float coolDown) {
        super(id, type, name, health, maxHealth, body, material);
        this.coolDown = coolDown;
    }

    @Override
    public abstract void use();

    @Override
    public void drop() {
        carrier.weapon = null;
        super.drop();
    }
}
