package ludumdare23.enemy.behavior;

import ludumdare23.enemy.EnemyShip;
import net.zzorn.gameflow.entity.Entity;
import net.zzorn.utils.Vec3;

/**
 * Try to go to a specific location relative to a target.
 */
public class FollowRelativelyBehavior extends BaseBehavior {
    private final Vec3 forward= new Vec3(0,0,0);
    private final Vec3 right= new Vec3(0,0,0);
    private final Vec3 targetPos= new Vec3(0,0,0);

    private double power;
    private Vec3 relativePos;

    public FollowRelativelyBehavior(Entity target, double minDistance, double maxDistance, double power, Vec3 relativePos) {
        super(target, minDistance, maxDistance);
        this.power = power;
        this.relativePos = relativePos;
    }

    @Override
    protected void doUpdate(double seconds, EnemyShip ship, Entity target, double distanceToTarget) {
        Vec3 tv = target.velocity();
        forward.set(tv);
        forward.setNormalized();
        right.set(tv.y(), -tv.x(), tv.z());

        forward.setMul(relativePos.x());
        right.setMul(relativePos.y());

        targetPos.set(target.pos());
        targetPos.setPlus(forward);
        targetPos.setPlus(right);

        ship.steerTowards(targetPos, power);
    }
}
