package ludumdare23.enemy.behavior;

import ludumdare23.enemy.EnemyShip;
import net.zzorn.utils.Vec3;

/**
 *
 */
public interface Behavior {

    void update(double seconds, EnemyShip ship);

    boolean isActive();

    void setActive(boolean active);
}
