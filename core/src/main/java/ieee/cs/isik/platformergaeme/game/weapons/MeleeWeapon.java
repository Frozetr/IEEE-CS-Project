package ieee.cs.isik.platformergaeme.game.weapons;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import ieee.cs.isik.platformergaeme.game.CharacterEntity;
import ieee.cs.isik.platformergaeme.game.Material;
import ieee.cs.isik.platformergaeme.game.WeaponEntity;
import ieee.cs.isik.platformergaeme.game.WeaponStats;

/**
 * A melee weapon (sword, axe, punch, etc.).
 *
 * <p>When {@link #use()} is called, an axis-aligned bounding box centred on the
 * carrier is queried against the Box2D world.  Every {@link CharacterEntity} found
 * inside that box (except the carrier themselves) receives {@link WeaponStats#damage}
 * HP of damage and a knock-back impulse directed away from the carrier.</p>
 */
public class MeleeWeapon extends WeaponEntity {

    private final World world;


    /**
     * @param id     unique entity id
     * @param type   entity type constant
     * @param name   display name (e.g. "Sword")
     * @param health initial HP of the weapon object itself
     * @param maxHealth maximum HP
     * @param body   Box2D rigid body for the pickup/dropped weapon
     * @param material render material
     * @param stats  weapon statistics (damage, range, coolDown, …)
     * @param world  the shared Box2D world
     */
    public MeleeWeapon(int id, int type, String name,
                       float health, float maxHealth,
                       @NotNull Body body, @NotNull Material material,
                       @NotNull WeaponStats stats,
                       @NotNull World world) {
        super(id, type, name, health, maxHealth, body, material, stats);
        this.world = world;
    }

    /**
     * Executes the melee attack:
     * <ol>
     *   <li>Guards against cool-down or empty ammo via {@link #canUse()}.</li>
     *   <li>Updates {@link #lastUsedTime}.</li>
     *   <li>Queries an AABB of radius {@link WeaponStats#range} around the carrier.</li>
     *   <li>Applies damage and knock-back to every nearby {@link CharacterEntity}.</li>
     * </ol>
     */
    @Override
    public void use() {
        if (!canUse()) return;

        lastUsedTime = TimeUtils.millis() / 1000f;

        Vector2 attackerPos = carrier.body.getPosition();
        float   r           = stats.range;

        final float lowerX = attackerPos.x - r;
        final float lowerY = attackerPos.y - r;
        final float upperX = attackerPos.x + r;
        final float upperY = attackerPos.y + r;

        world.QueryAABB(fixture -> {
            Object userData = fixture.getBody().getUserData();
            if (!(userData instanceof CharacterEntity)) return true; 

            CharacterEntity target = (CharacterEntity) userData;
            if (target == carrier) return true;

            target.health -= stats.damage;

            Vector2 knockDir = target.body.getPosition()
                                          .cpy()
                                          .sub(attackerPos)
                                          .nor();
            target.body.applyLinearImpulse(
                knockDir.scl(stats.knockbackForce),
                target.body.getWorldCenter(),
                true
            );

            return true; 
        }, lowerX, lowerY, upperX, upperY);
    }
}
