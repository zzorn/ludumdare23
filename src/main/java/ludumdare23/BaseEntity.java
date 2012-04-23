package ludumdare23;

import net.zzorn.gameflow.EntityGroup;
import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.input.InputListenerAdapter;
import net.zzorn.utils.Vec3;

/**
 *
 */
public abstract class BaseEntity extends InputListenerAdapter implements Entity, Damageable {

    private EntityGroup<? extends Entity> group = null;
    protected Vec3 pos      = new Vec3(0,0,0);
    protected Vec3 velocity = new Vec3(0,0,0);
    protected Vec3 acc      = new Vec3(0,0,0);

    private double maxHitPoints = 100;
    private double hitPoints = maxHitPoints;
    private boolean destroyed = false;

    public double getHitPointsPercent() {
        return Math.min(1, Math.max(0, hitPoints / maxHitPoints));
    }

    public double getHitPoints() {
        return hitPoints;
    }

    public double getMaxHitPoints() {
        return maxHitPoints;
    }

    public void setMaxHitPoints(double maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public void setHitPoints(double hitPoints) {
        this.hitPoints = hitPoints;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void damage(double amount) {
        hitPoints -= amount;
        if (!destroyed && hitPoints <= 0) {
            destroyed = true;
            onDestroyed();
        }
    }

    public void heal(double amount) {
        if (!isDestroyed()) {
            hitPoints += amount;
            if (hitPoints > maxHitPoints) hitPoints = maxHitPoints;
        }
    }


    public EntityGroup<? extends Entity> getGroup() {
        return group;
    }

    public void setGroup(EntityGroup<? extends Entity> group) {
        this.group = group;
    }

    @Override
    public Vec3 pos() {
        return pos;
    }

    @Override
    public Vec3 velocity() {
        return velocity;
    }

    public void remove() {
        EntityGroup group = getGroup();
        if (group != null) group.remove(this);
    }


    @Override
    public double surfaceDistance(Entity other) {
        return pos.distance(other.pos()) - getRadius() - other.getRadius();
    }

    @Override
    public boolean overlaps(Entity other) {
        return surfaceDistance(other) < 0;
    }

    @Override
    public boolean overlaps(Vec3 location, double radius) {
        return pos.distance(location) - getRadius() - radius < 0;
    }

    protected void onDestroyed() {

    }



}
