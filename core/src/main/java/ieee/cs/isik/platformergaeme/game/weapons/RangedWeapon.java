package ieee.cs.isik.platformergaeme.game.weapons;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

import ieee.cs.isik.platformergaeme.game.BulletEntity;
import ieee.cs.isik.platformergaeme.game.Entity;
import ieee.cs.isik.platformergaeme.game.Material;
import ieee.cs.isik.platformergaeme.game.WeaponEntity;
import ieee.cs.isik.platformergaeme.game.WeaponStats;

/**
 * A ranged weapon (pistol, bow, etc.) that fires {@link BulletEntity} projectiles.
 *
 * <h3>Shooting</h3>
 * {@link #use()} spawns a {@link BulletEntity} from the carrier's position and
 * pushes it into {@link #entityBuffer}. {@code GameScreen} must drain this buffer
 * into the main entity list after every {@code use()} call.
 *
 * <h3>Reloading</h3>
 * {@link #reload(float)} starts an asynchronous reload. {@link #act(float)} must
 * be called every frame (by {@code GameScreen}) to tick the reload timer; when the
 * timer expires, {@link #currentAmmo} is reset to {@link WeaponStats#ammo}.
 */
public class RangedWeapon extends WeaponEntity {

    private static final float BULLET_SPEED  = 20f;

   
    public boolean reloading;

    public float reloadTimeLeft;

    /**
     * Bullets created by the most recent {@link #use()} call.
     * {@code GameScreen} reads and clears this buffer after every attack.
     */
    public final List<Entity> entityBuffer = new ArrayList<>();

    private int bulletIdCounter = 0;

    public boolean facingRight = true;

    /**
     * Shared removal queue owned by {@code GameScreen}. Injected via
     * {@link #setToRemoveList(List)} right after the weapon is equipped
     * (see {@code GameScreen} — "Duzeltme 4c"). Defaults to a throwaway
     * list so {@link #use()} never NPEs even if injection is skipped.
     */
    private List<Entity> toRemove = new ArrayList<>();

    /**
     * Injects the real {@code toRemove} queue from {@code GameScreen} so that
     * bullets spawned by this weapon are actually cleaned up after they are
     * destroyed. Must be called by {@code GameScreen} right after equipping.
     *
     * @param toRemove the shared entity-removal queue managed by {@code GameScreen}
     */
    public void setToRemoveList(@NotNull List<Entity> toRemove) {
        this.toRemove = toRemove;
    }

    /**
     * @param id       unique entity id
     * @param type     entity type constant
     * @param name     display name (e.g. "Pistol")
     * @param health   initial HP
     * @param maxHealth maximum HP
     * @param body     Box2D rigid body for the pickup object
     * @param material render material
     * @param stats    weapon statistics
     */
    public RangedWeapon(int id, int type, String name,
                        float health, float maxHealth,
                        @NotNull Body body, @NotNull Material material,
                        @NotNull WeaponStats stats) {
        super(id, type, name, health, maxHealth, body, material, stats);
        this.reloading     = false;
        this.reloadTimeLeft = 0f;
    }

    /**
     * Fires one bullet in the direction the carrier is facing.
     * Does nothing if {@link #canUse()} returns {@code false} or a reload is active.
     */
    @Override
    public void use() {
        if (!canUse() || reloading) return;

        // Consume ammo
        if (stats.ammo != -1) {
            currentAmmo--;
        }

        lastUsedTime = TimeUtils.millis() / 1000f;

        float vx = carrier.body.getLinearVelocity().x;
        boolean goingRight = (vx != 0f) ? (vx > 0f) : facingRight;
        Vector2 direction = new Vector2(goingRight ? 1f : -1f, 0f);

        Vector2 startPos = carrier.body.getPosition().cpy();

        World world = carrier.body.getWorld();

        BulletEntity bullet = new BulletEntity(
            id * 1000 + (bulletIdCounter++),
            world,
            carrier,
            startPos,
            direction,
            BULLET_SPEED,
            stats.damage,
            stats.range,
            toRemove
        );

        entityBuffer.add(bullet);
    }

   
    /**
     * Initiates an asynchronous reload.
     * While reloading, {@link #use()} will refuse to fire.
     *
     * @param duration reload duration in seconds
     */
    public void reload(float duration) {
        if (reloading) return; // already reloading
        reloading      = true;
        reloadTimeLeft = duration;
    }

    /**
     * Must be called every frame by {@code GameScreen} when this weapon is equipped.
     * Decrements the reload timer and restores ammo when it reaches zero.
     *
     * @param delta seconds elapsed since the last frame
     */
    @Override
    public void act(float delta) {
        if (!reloading) return;

        reloadTimeLeft -= delta;
        if (reloadTimeLeft <= 0f) {
            currentAmmo    = stats.ammo;
            reloading      = false;
            reloadTimeLeft = 0f;
        }
    }
}
