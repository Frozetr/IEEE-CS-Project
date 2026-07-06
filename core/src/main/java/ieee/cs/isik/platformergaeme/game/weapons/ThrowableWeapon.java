package ieee.cs.isik.platformergaeme.game.weapons;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.TimeUtils;

import ieee.cs.isik.platformergaeme.game.CharacterEntity;
import ieee.cs.isik.platformergaeme.game.Entity;
import ieee.cs.isik.platformergaeme.game.Material;
import ieee.cs.isik.platformergaeme.game.WeaponEntity;
import ieee.cs.isik.platformergaeme.game.WeaponStats;

/**
 * A throwable, area-of-effect weapon (bomb, grenade, throwing knife, etc.).
 *
 * <h3>Usage flow</h3>
 * <ol>
 *   <li>{@link #use()} primes the weapon: it drops itself from the carrier and applies
 *       a throw impulse toward the mouse cursor (direction supplied by
 *       {@link #throwDirection}). The {@link #fuseTimer} begins counting down.</li>
 *   <li>While airborne (carrier is {@code null}), {@link #act(float)} ticks the fuse.</li>
 *   <li>When {@link #fuseTimer} reaches zero, {@link #explode()} is triggered:
 *       all {@link CharacterEntity} instances within {@link WeaponStats#range} metres
 *       receive damage and knock-back, and the weapon enqueues itself for removal.</li>
 * </ol>
 *
 * <p>{@code GameScreen} must set {@link #throwDirection} immediately before calling
 * {@link #use()} (or before {@link WeaponEntity#drop()} is called internally).</p>
 */
public class ThrowableWeapon extends WeaponEntity {
    private static final float DEFAULT_FUSE = 3f;

    public float fuseTimer;

    /**
     * Normalised direction toward which the weapon is thrown.
     * Must be set by {@code GameScreen} before calling {@link #use()}.
     */
    public Vector2 throwDirection = new Vector2(1f, 0f);

    private final List<Entity> toRemove;


    /**
     * @param id       unique entity id
     * @param type     entity type constant
     * @param name     display name (e.g. "Bomb")
     * @param health   initial HP
     * @param maxHealth maximum HP
     * @param body     Box2D rigid body
     * @param material render material
     * @param stats    weapon statistics
     * @param toRemove shared entity-removal queue (managed by {@code GameScreen})
     */
    public ThrowableWeapon(int id, int type, String name,
                           float health, float maxHealth,
                           @NotNull Body body, @NotNull Material material,
                           @NotNull WeaponStats stats,
                           @NotNull List<Entity> toRemove) {
        super(id, type, name, health, maxHealth, body, material, stats);
        this.toRemove  = toRemove;
        this.fuseTimer = DEFAULT_FUSE;
    }

   
    @Override
    public void use() {
        if (!canUse()) return;

        lastUsedTime = TimeUtils.millis() / 1000f;

        drop();
        body.applyLinearImpulse(
            throwDirection.cpy().nor().scl(stats.throwForce),
            body.getWorldCenter(),
            true
        );
        fuseTimer = DEFAULT_FUSE;
    }

   
    /**
     * Decrements the fuse timer while the weapon is not being carried,
     * and detonates when the fuse expires.
     *
     * @param delta seconds elapsed since the last frame
     */
    @Override
    public void act(float delta) {
        if (carrier != null) return; 

        fuseTimer -= delta;
        if (fuseTimer <= 0f) {
            explode();
        }
    }

    public void explode() {
        Vector2 centre = body.getPosition();
        float   r      = stats.range;

        body.getWorld().QueryAABB(fixture -> {
            Object userData = fixture.getBody().getUserData();
            if (!(userData instanceof CharacterEntity)) return true;

            CharacterEntity target = (CharacterEntity) userData;

            target.health -= stats.damage;

            Vector2 knockDir = target.body.getPosition()
                                          .cpy()
                                          .sub(centre)
                                          .nor();
            target.body.applyLinearImpulse(
                knockDir.scl(stats.knockbackForce),
                target.body.getWorldCenter(),
                true
            );

            return true;
        }, centre.x - r, centre.y - r, centre.x + r, centre.y + r);

        toRemove.add(this);
    }
}
