package ludumdare23.enemy;

import ludumdare23.Game;
import ludumdare23.MathUtils;
import ludumdare23.Planet;
import ludumdare23.Ship;
import ludumdare23.enemy.behavior.Behavior;
import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.picture.Picture;
import net.zzorn.utils.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Enemy ship that attacks the planet and the player
 */
public class EnemyShip extends Ship {
    private final Planet planet;
    private final Picture picture;
    private double maxThrust;
    private double maxSpeed;
    private final double shootDistance;
    private final double brakeDistance;
    private final Entity target;
    private double explosionSize = 0.5;

    private List<Behavior> behaviors = new ArrayList<Behavior>();

    public EnemyShip(Game game, Planet planet, Picture picture, double maxThrust, double maxSpeed, int maxNumberOfWeapons, double shootDistance, double brakeDistance, Entity target) {
        super(game, maxNumberOfWeapons);
        this.planet = planet;
        this.picture = picture;
        this.maxThrust = maxThrust;
        this.maxSpeed = maxSpeed;
        this.shootDistance = shootDistance;
        this.brakeDistance = brakeDistance;
        this.target = target;
    }

    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
    }

    public void removeBehavior(Behavior behavior) {
        behaviors.remove(behavior);
    }

    @Override
    public Vec3 pos() {
        return pos;
    }


    public void steerTowards(Vec3 pos, double power) {
        power = MathUtils.clampMinusOneToOne(power);

        Vec3 acc = getAcc();
        acc.set(pos());
        acc.setMinus(pos());
        acc.setNormalized(); // Now we have direction towards target in acc, multiply with our thrust to get actual acceleration
        acc.setMul(getMaxThrust() * power);
    }


    protected void onNewUpdate(double durationSeconds) {

        // Do not run into planet
        double surfaceDist = planet.getSurfaceDist(pos(), picture.w()/2 );
        if (surfaceDist < 0) {
            Vec3 normal=planet.normalAt(pos());
            normal.setMul(surfaceDist);
            pos().setMinus(normal);
        }

        // Do behaviors
        for (Behavior behavior : behaviors) {
            behavior.update(durationSeconds, this);
        }


        // Update velocity based on acceleration
        velocity.setPlusMul(acc, durationSeconds);

        // Clamp velocity to maxSpeed
        double velSize = velocity.length();
        if (velSize > maxSpeed) {
            velocity.setNormalized();
            velocity.setMul(maxSpeed);
        }

        // Update position based on velocity
        pos.setPlusMul(velocity, durationSeconds);
    }


    protected void onUpdate(double durationSeconds) {
        double surfaceDist = planet.getSurfaceDist(pos(), picture.w()/2 );
        if (surfaceDist<=0) {
            //onSurface=true;
            Vec3 normal=planet.normalAt(pos());
            normal.setMul(surfaceDist);
            pos().setMinus(normal);
        }


        // Accelerate towards the target
        double distanceToTarget = surfaceDistance(target);
        if (distanceToTarget> brakeDistance){
            // Drive towards target
            acc.set(target.pos());
            acc.setMinus(pos);
            acc.setNormalized(); // Now we have direction towards player in acc, multiply with our thrust to get actual acceleration
            acc.setMul(maxThrust);
        }
        else {
            // Brake when too close
            acc.set(target.pos());
            acc.setMinus(pos);
            acc.setNormalized();
            acc.setMul(2*maxThrust);
        }

        if (surfaceDist<=100) {
            // Avoid running into planet
            Vec3 normal = planet.normalAt(pos());
            normal.setMul(2*maxThrust);
            acc.setPlus(normal);
        }


        // Update velocity based on acceleration
        velocity.setPlusMul(acc, durationSeconds);

        // Clamp velocity to maxSpeed
        double velSize = velocity.length();
        if (velSize > maxSpeed) {
            velocity.setNormalized();
            velocity.setMul(maxSpeed);
        }

        // Update position based on velocity
        pos.setPlusMul(velocity, durationSeconds);


        // Fire if we are close enough and weapon ready
        if (distanceToTarget <= shootDistance && isReadyToFire()) {
            // Only shoot if we are moving towards target
            Vec3 vectorToTarget = Vec3.fromTo(pos(), target.pos());
            if (velocity().dot(vectorToTarget) > 0) fire(target.pos());
        }
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y, double scale) {
        picture.drawCentered(g, x, y, scale);
    }

    @Override
    protected void onDestroyed() {
        getGame().spawnExplosion(pos(), velocity(), getRadius(), 60*explosionSize, 40*explosionSize, 0.8*explosionSize, 8*explosionSize, 300*explosionSize);
        getGame().spawnExplosion(pos(), velocity(), getRadius(), 40*explosionSize, 10*explosionSize, 0.4 + 0.4*explosionSize, 4*explosionSize, 1500*explosionSize);
        remove();
    }

    public double getMaxThrust() {
        return maxThrust;
    }
}


