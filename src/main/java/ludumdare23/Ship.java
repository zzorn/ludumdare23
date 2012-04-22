package ludumdare23;

import net.zzorn.utils.Vec3;

/**
 *
 */
public abstract class Ship extends BaseEntity implements Damageable {

    private Weapon weapon = null;
    private double radius = 10;

    public double getRadius() { return radius; }
    public Weapon getWeapon() { return weapon; }

    public void setWeapon(Weapon weapon) {
        if(this.weapon != null) this.weapon.setHost(null);
        this.weapon = weapon;
        if(this.weapon != null) this.weapon.setHost(this);
    }

    public void fire(Vec3 target) {
        if (weapon != null) weapon.fire(target);
    }

    @Override
    public final void update(double durationSeconds) {
        if (weapon != null) weapon.update(durationSeconds);
        onUpdate(durationSeconds);
    }

    protected abstract void onUpdate(double durationSeconds);
}
