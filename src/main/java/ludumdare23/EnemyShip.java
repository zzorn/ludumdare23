package ludumdare23;

import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.picture.Picture;
import net.zzorn.utils.Vec3;

import java.awt.*;

/**
 * Enemy ship that attacks the planet and the player
 */
public class EnemyShip extends Ship {
    private final Planet planet;
    private Entity target;
    private final Picture picture;
    private double maxThrust;
    private double maxSpeed;
    private boolean onSurface=false;
    // distance from target
    private double brakeDistance = 200;
    private double shootDistance = 500;
    // brake multiply
    private double brake =-1;

    private double explosionSize = 0.5;

    public EnemyShip(Game game, Planet planet, Entity target, Picture picture, Vec3 startPos, Vec3 startVelocity, double maxThrust, double maxSpeed, double brakeDistance, double maxBrakeAcc, double shootDistance) {
        super(game);
        this.planet = planet;
        this.target = target;
        this.picture = picture;
        this.maxThrust = maxThrust;
        this.maxSpeed = maxSpeed;
        this.brakeDistance = brakeDistance;
        this.shootDistance = shootDistance;
        this.brake = -maxBrakeAcc;
        pos.set(startPos);
        velocity.set(startVelocity);


    }

    @Override
    public Vec3 pos() {
        return pos;
    }

    @Override
    protected void onUpdate(double durationSeconds) {
        double surfaceDist = planet.getSurfaceDist(pos(), picture.w()/2 );
        if (surfaceDist<=0) {
            //onSurface=true;
            Vec3 normal=planet.normalAt(pos());
            normal.setMul(surfaceDist);
            pos().setMinus(normal);
        }


        if (!onSurface){
            // Accelerate towards the target
            double distanceToTarget = pos().distance(target.pos());
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
                acc.setMul(brake*maxThrust);
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
            if (getWeapon() != null && distanceToTarget <= shootDistance && getWeapon().isReadyToFire()) {
                // Only shoot if we are moving towards target
                Vec3 vectorToTarget = Vec3.fromTo(pos(), target.pos());
                if (velocity().dot(vectorToTarget) > 0) getWeapon().fire(target.pos());
            }
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
}


