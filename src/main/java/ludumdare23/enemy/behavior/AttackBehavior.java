package ludumdare23.enemy.behavior;

import ludumdare23.Weapon;
import ludumdare23.enemy.EnemyShip;
import net.zzorn.gameflow.entity.Entity;
import net.zzorn.utils.Vec3;

/**
 *
 */
public class AttackBehavior extends BaseBehavior {

    public AttackBehavior(Entity target, double minDistance, double maxDistance) {
        super(target, minDistance, maxDistance);
    }

    @Override
    protected void doUpdate(double seconds, EnemyShip ship, Entity target, double distanceToTarget) {
        if (ship.isReadyToFire()) {
            // Only shoot if we are moving towards target
            Vec3 vectorToTarget = Vec3.fromTo(ship.pos(), target.pos());
            if (ship.velocity().dot(vectorToTarget) > 0) ship.fire(target.pos());
        }
    }
}
