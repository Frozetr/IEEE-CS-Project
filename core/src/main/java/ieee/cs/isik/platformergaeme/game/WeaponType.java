package ieee.cs.isik.platformergaeme.game;

/**
 * Categorises weapon behaviour at a high level.
 * Used by {@link WeaponStats} to decide which weapon sub-system handles the weapon.
 */
public enum WeaponType {
    /** Close-range physical attack (sword, axe, fist). */
    MELEE,
    /** Projectile-based attack (pistol, bow). */
    RANGED,
    /** Thrown object that detonates after a fuse (bomb, grenade). */
    THROWABLE
}
