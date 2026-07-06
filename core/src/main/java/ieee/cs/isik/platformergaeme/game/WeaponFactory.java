package ieee.cs.isik.platformergaeme.game;

import com.badlogic.gdx.physics.box2d.*;
import ieee.cs.isik.platformergaeme.game.weapons.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Static factory for creating pre-configured weapon instances.
 *
 * <p>Every method creates a {@link WeaponStats} record, a standard Box2D body
 * (DynamicBody, 0.3 m × 0.3 m box, density 1), and the appropriate concrete
 * {@link WeaponEntity} sub-class.</p>
 *
 * <p><b>Usage example:</b>
 * <pre>{@code
 * MeleeWeapon sword = WeaponFactory.createSword(gameScreen.physicsWorld, nextId());
 * gameScreen.addWeaponToWorld(sword, 5f, 2f);
 * }</pre>
 * </p>
 */
public final class WeaponFactory {

    // Prevent instantiation
    private WeaponFactory() {}

    // -----------------------------------------------------------------------
    // Sword (melee)
    // -----------------------------------------------------------------------

    /**
     * Creates a sword: high damage, moderate range, half-second cool-down.
     *
     * @param world the Box2D world
     * @param id    unique entity id
     * @return a ready-to-place {@link MeleeWeapon}
     */
    public static MeleeWeapon createSword(@NotNull World world, int id) {
        WeaponStats stats = new WeaponStats(
            25f,           // damage
            1.2f,          // range (metres)
            0.6f,          // coolDown (seconds)
            8f,            // throwForce
            5f,            // knockbackForce
            -1,            // ammo (infinite)
            WeaponType.MELEE
        );

        Body body = createStandardBody(world);

        return new MeleeWeapon(
            id, 1, "Sword",
            50f, 50f,
            body, null /* assign a real Material in production */,
            stats, world
        );
    }

    // -----------------------------------------------------------------------
    // Pistol (ranged)
    // -----------------------------------------------------------------------

    /**
     * Creates a pistol: 12-round magazine, moderate damage and range.
     *
     * @param world the Box2D world
     * @param id    unique entity id
     * @return a ready-to-place {@link RangedWeapon}
     */
    public static RangedWeapon createPistol(@NotNull World world, int id) {
        WeaponStats stats = new WeaponStats(
            15f,           // damage
            10f,           // range (metres) — also bullet maxRange
            0.4f,          // coolDown (seconds)
            6f,            // throwForce
            2f,            // knockbackForce
            12,            // ammo
            WeaponType.RANGED
        );

        Body body = createStandardBody(world);

        return new RangedWeapon(
            id, 2, "Pistol",
            30f, 30f,
            body, null /* assign a real Material in production */,
            stats
        );
    }

    // -----------------------------------------------------------------------
    // Bomb (throwable)
    // -----------------------------------------------------------------------

    /**
     * Creates a bomb: single-use, high area damage, large blast radius.
     *
     * @param world    the Box2D world
     * @param id       unique entity id
     * @param toRemove the shared entity-removal queue from {@code GameScreen}
     * @return a ready-to-place {@link ThrowableWeapon}
     */
    public static ThrowableWeapon createBomb(@NotNull World world, int id,
                                              @NotNull List<Entity> toRemove) {
        WeaponStats stats = new WeaponStats(
            50f,           // damage
            2f,            // range / blast radius (metres)
            0f,            // coolDown (no cooldown — single use)
            10f,           // throwForce
            8f,            // knockbackForce
            1,             // ammo (one throw)
            WeaponType.THROWABLE
        );

        Body body = createStandardBody(world);

        return new ThrowableWeapon(
            id, 3, "Bomb",
            20f, 20f,
            body, null /* assign a real Material in production */,
            stats, toRemove
        );
    }

    // -----------------------------------------------------------------------
    // Shared body factory
    // -----------------------------------------------------------------------

    /**
     * Creates a standard weapon pickup body: DynamicBody, 0.3 m × 0.3 m box,
     * density 1, friction 0.5, restitution 0.
     *
     * @param world the Box2D world
     * @return the new body (transform at origin — caller must set position)
     */
    private static Body createStandardBody(@NotNull World world) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;

        Body body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.15f, 0.15f); // half-extents → full size 0.3 m × 0.3 m

        FixtureDef fd = new FixtureDef();
        fd.shape       = shape;
        fd.density     = 1f;
        fd.friction    = 0.5f;
        fd.restitution = 0f;

        body.createFixture(fd);
        shape.dispose();

        return body;
    }
}
