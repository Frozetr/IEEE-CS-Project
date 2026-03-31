package ieee.cs.isik.platformergaeme.game;

import com.badlogic.gdx.physics.box2d.Body;
import org.jetbrains.annotations.NotNull;


/**
 * This class represents data of a character,
 * A character must carry a weapon.
 * A character can carry at most one consumable.
 */
public class CharacterEntity extends Entity {

    /// Weapons can be used repeatedly, can be null
    public WeaponEntity weapon;

    /// Consumable can be used just one time, can be null
    public ConsumableEntity consumable;

    public CharacterEntity(int id, int type, String name, float health, float maxHealth, @NotNull Body body, @NotNull Material material, WeaponEntity weapon, ConsumableEntity consumable) {
        super(id, type, name, health, maxHealth, body, material);
        this.weapon = weapon;
        this.consumable = consumable;
    }
}
