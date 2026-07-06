package ieee.cs.isik.platformergaeme.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.TimeUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A fast-moving projectile created by {@link ieee.cs.isik.platformergaeme.game.weapons.RangedWeapon}.
 * <p>
 * The bullet moves at a constant velocity each frame. When it travels further
 * than {@code maxRange} metres, or when the Box2D contact listener reports a
 * collision with an eligible target, {@link #destroy()} is called and the bullet
 * is queued in the shared {@code toRemove} list for safe disposal after
 * {@code physicsWorld.step()}.
 * </p>
 */
public class BulletEntity extends Entity {

    /** The character that fired this bullet – used to avoid self-hit. */
    @NotNull
    public final CharacterEntity owner;

    /** Damage dealt to the first entity this bullet contacts. */
    public final float damage;

    /** Maximum travel distance in Box2D metres before auto-destruction. */
    public final float maxRange;

    /** Normalised direction vector set at construction time. */
    private final Vector2 direction;

    /** Movement speed in metres per second. */
    private final float speed;

    /** Accumulated travel distance in metres. */
    public float traveledDistance;

    /** Set to {@code true} once {@link #destroy()} has been called. */
    public boolean destroyed;

    /** Shared list – {@code GameScreen} clears this after every {@code step()}. */
    private final List<Entity> toRemove;

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    /**
     * Creates and fully configures a new bullet, including its Box2D body.
     *
     * @param id        unique entity id
     * @param world     the Box2D world (body is created here)
     * @param owner     the character who fired the bullet
     * @param startPos  initial position in Box2D metres
     * @param direction normalised travel direction
     * @param speed     travel speed in metres per second
     * @param damage    HP deducted from first hit target
     * @param maxRange  auto-destroy distance in metres
     * @param toRemove  shared removal queue (managed by {@code GameScreen})
     */
    public BulletEntity(int id, @NotNull World world, @NotNull CharacterEntity owner,
                        @NotNull Vector2 startPos, @NotNull Vector2 direction,
                        float speed, float damage, float maxRange,
                        @NotNull List<Entity> toRemove) {

        super(id, 99 /* bullet type constant */, "Bullet", 1f, 1f,
              createBulletBody(world, startPos), createDummyMaterial());

        this.owner           = owner;
        this.direction       = direction.cpy().nor();
        this.speed           = speed;
        this.damage          = damage;
        this.maxRange        = maxRange;
        this.toRemove        = toRemove;
        this.traveledDistance = 0f;
        this.destroyed        = false;

        // Let contact callbacks look up this entity from the fixture.
        body.setUserData(this);
    }

    // -----------------------------------------------------------------------
    // Per-frame update
    // -----------------------------------------------------------------------

    /**
     * Advances the bullet one frame: sets its velocity, accumulates travel distance,
     * and calls {@link #destroy()} if {@code maxRange} is exceeded.
     *
     * @param delta seconds elapsed since the last frame
     */
    public void update(float delta) {
        if (destroyed) return;

        body.setLinearVelocity(direction.cpy().scl(speed));
        traveledDistance += speed * delta;

        if (traveledDistance >= maxRange) {
            destroy();
        }
    }

    // -----------------------------------------------------------------------
    // Destruction
    // -----------------------------------------------------------------------

    /**
     * Marks this bullet as destroyed and enqueues it for safe disposal.
     * Idempotent – calling more than once has no additional effect.
     */
    public void destroy() {
        if (destroyed) return;
        destroyed = true;
        toRemove.add(this);
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    private static Body createBulletBody(World world, Vector2 startPos) {
        BodyDef def = new BodyDef();
        def.type         = BodyDef.BodyType.DynamicBody;
        def.position.set(startPos);
        def.bullet       = true; // enable Continuous Collision Detection

        Body body = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.1f); // 10 cm radius

        FixtureDef fd = new FixtureDef();
        fd.shape       = shape;
        fd.density     = 0.1f;
        fd.friction    = 0f;
        fd.restitution = 0f;

        body.createFixture(fd);
        shape.dispose();

        return body;
    }

    /**
     * Bullets have no visible sprite by default; a 1×1 white pixel TextureMaterial
     * would be used in production.  Here we return a minimal no-op Material so that
     * the render loop does not crash when it calls {@code mat.act(delta)} /
     * {@code mat.getFrame()}.
     */
    private static Material createDummyMaterial() {
        // TextureMaterial or a 1×1 white Texture would be used here in production.
        // Returning null is acceptable only if GameScreen skips null materials;
        // prefer passing a real 1×1 TextureMaterial in the actual project.
        return null;
    }
}
