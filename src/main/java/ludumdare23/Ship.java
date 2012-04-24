package ludumdare23;

import net.zzorn.utils.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class Ship extends BaseEntity implements Damageable {

    private List<Weapon> weapons = new ArrayList<Weapon>();
    private double radius = 30;
    private int maxNumberOfWeapons = 3;

    protected Ship(Game game, int maxNumberOfWeapons) {
        super(game);
        this.maxNumberOfWeapons = maxNumberOfWeapons;
    }

    public double getRadius() { return radius; }

    public List<Weapon> getWeapons() { return weapons; }

    public void addWeapon(Weapon weapon) {
        if (weapons.size() >= maxNumberOfWeapons) {
            Weapon oldWeapon = weapons.get((int) Math.round(Math.random() * weapons.size()));
            oldWeapon.setHost(null);
            weapons.remove(oldWeapon);
        }

        weapon.setHost(this);
        weapons.add(weapon);
    }

    public void fire(Vec3 target) {
        for (Weapon weapon : weapons) {
            weapon.fire(target);
        }
    }

    public boolean isReadyToFire() {
        for (Weapon weapon : weapons) {
            if (weapon.isReadyToFire()) return true;
        }
        return false;
    }

    @Override
    public final void update(double durationSeconds) {
        for (Weapon weapon : weapons) {
            weapon.update(durationSeconds);
        }
        onUpdate(durationSeconds);
    }

    protected abstract void onUpdate(double durationSeconds);
}
