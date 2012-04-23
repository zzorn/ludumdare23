package ludumdare23;

import net.zzorn.gameflow.entity.Entity;
import net.zzorn.utils.Vec3;

/**
 *
 */
public interface Damageable extends Entity {

    double getMaxHitPoints();

    double getHitPoints();

    void damage(double amount, Vec3 pos);

    boolean isDestroyed();

    void restoreLife();

}
