package ieee.cs.isik.platformergaeme.game;

/**
 * Immutable data object that describes every numeric property of a weapon.
 * Pass an instance into any {@link WeaponEntity} constructor so that tuning
 * can be done centrally (e.g. in {@link WeaponFactory}) without touching logic code.
 */
public final class WeaponStats {

    /** HP subtracted from the target on each successful hit. */
    public final float damage;

    /** Maximum reach in Box2D metres (1 m = {@code GameScreen.meter2pixel} px). */
    public final float range;

    /** Minimum seconds that must elapse between consecutive uses. */
    public final float coolDown;

    /** Impulse magnitude (N·s) applied to the weapon body when thrown with Q. */
    public final float throwForce;

    /** Impulse magnitude (N·s) applied to a hit target, directed away from the attacker. */
    public final float knockbackForce;

    /**
     * Maximum number of uses / rounds.
     * Use {@code -1} to indicate unlimited ammunition.
     */
    public final int ammo;

    /** High-level category that drives which weapon sub-class handles {@code use()}. */
    public final WeaponType weaponType;

    /**
     * Constructs a fully-specified, immutable {@code WeaponStats} record.
     *
     * @param damage         hit-point reduction per hit
     * @param range          reach in Box2D metres
     * @param coolDown       seconds between uses
     * @param throwForce     throw impulse magnitude
     * @param knockbackForce knock-back impulse applied to the target
     * @param ammo           max ammo count; {@code -1} for infinite
     * @param weaponType     melee, ranged, or throwable
     */
    public WeaponStats(float damage, float range, float coolDown,
                       float throwForce, float knockbackForce,
                       int ammo, WeaponType weaponType) {
        this.damage         = damage;
        this.range          = range;
        this.coolDown       = coolDown;
        this.throwForce     = throwForce;
        this.knockbackForce = knockbackForce;
        this.ammo           = ammo;
        this.weaponType     = weaponType;
    }
}
