package ludumdare23.enemy.behavior;

import ludumdare23.enemy.EnemyShip;
import net.zzorn.gameflow.entity.Entity;

/**
 *
 */
public class FollowBehavior extends BaseBehavior {

    private final double power;

    public FollowBehavior(Entity target, double minDistance, double maxDistance, double power) {
        super(target, minDistance, maxDistance);
        this.power = power;
    }

    @Override
    protected void doUpdate(double seconds, EnemyShip ship, Entity target, double distanceToTarget) {
        ship.steerTowards(target.pos(), power);
    }
}
