package ludumdare23.enemy.behavior;

import ludumdare23.enemy.EnemyShip;
import net.zzorn.gameflow.entity.Entity;

/**
 *
 */
public abstract class BaseBehavior implements Behavior {

    private Entity target;
    private double minDistance;
    private double maxDistance;
    private boolean active = true;

    public BaseBehavior(Entity target, double minDistance, double maxDistance) {
        this.target = target;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void update(double seconds, EnemyShip ship) {
        if (active) {
            double distanceToTarget = ship.surfaceDistance(target);
            if (distanceToTarget >= minDistance && distanceToTarget <= maxDistance) {
                doUpdate(seconds, ship, target, distanceToTarget);
            }
        }
    }

    protected abstract void doUpdate(double seconds, EnemyShip ship, Entity target, double distanceToTarget);

}
