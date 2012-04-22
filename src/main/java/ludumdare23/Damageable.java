package ludumdare23;

import net.zzorn.gameflow.entity.Entity;

/**
 *
 */
public interface Damageable extends Entity {

    double getMaxHitPoints();

    double getHitPoints();

    void damage(double amount);

    boolean isDestroyed();

}
