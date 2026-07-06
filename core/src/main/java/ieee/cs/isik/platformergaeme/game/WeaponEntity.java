package ieee.cs.isik.platformergaeme.game;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.TimeUtils;

public abstract class WeaponEntity extends CarriableEntity {

    public final float coolDown;
    public final WeaponStats stats;
    public int currentAmmo;
    public float lastUsedTime;

    public WeaponEntity(int id, int type, String name,
                        float health, float maxHealth,
                        @NotNull Body body, @NotNull Material material,
                        @NotNull WeaponStats stats) {
        super(id, type, name, health, maxHealth, body, material);
        this.stats        = stats;
        this.coolDown     = stats.coolDown;
        this.currentAmmo  = stats.ammo;
        this.lastUsedTime = 0f;
    }

    public boolean canUse() {
        float nowSeconds = TimeUtils.millis() / 1000f;
        boolean coolDownOk = (nowSeconds - lastUsedTime) >= stats.coolDown;
        boolean ammoOk     = (stats.ammo == -1) || (currentAmmo > 0);
        return coolDownOk && ammoOk;
    }

    public void onEquip(CharacterEntity carrier) {
        // Duzeltme: tasinirken silah fizik simulasyonundan cikarilir,
        // yer cekiminden etkilenmez ve baska cisimlerle carpismaz.
        body.setActive(false);
    }

    public void onUnequip() {}

    public void act(float delta) {}

    @Override
    public abstract void use();

    @Override
    public void drop() {
        carrier.weapon = null;
        // Duzeltme: birakilirken/firlatilirken fizik yeniden aktif edilir.
        // Bu satir drop() icine tasindi (onUnequip'e degil) cunku
        // ThrowableWeapon.use() drop()'u dogrudan cagirir, onUnequip'i cagirmaz;
        // aksi halde bomba/atilabilir silah firlatildiginda body pasif kalip
        // uygulanan itki (impulse) hicbir etki yaratmazdi.
        body.setActive(true);
        super.drop();
    }
}