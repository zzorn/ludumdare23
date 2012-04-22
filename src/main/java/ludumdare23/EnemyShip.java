package ludumdare23;

import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.picture.Picture;
import net.zzorn.utils.Vec3;

import java.awt.*;

/**
 * Enemy ship that attacks the planet and the player
 */
public class EnemyShip implements Entity {
    private final Planet planet;
    private Entity target;
    private final Picture picture;
    private double maxThrust;
    private double maxSpeed;
    private boolean onSurface=false;
    // distance from target
    private double distance = 200;
    // brake multiply
    private double brake =1;

    private Vec3 pos      = new Vec3(0,0,0);
    private Vec3 velocity = new Vec3(0,0,0);
    private Vec3 acc      = new Vec3(0,0,0);

    public EnemyShip(Planet planet, Entity target, Picture picture, Vec3 startPos, Vec3 startVelocity, double maxThrust, double maxSpeed, double distance, double maxBrakeAcc) {
        this.planet = planet;
        this.target = target;
        this.picture = picture;
        this.maxThrust = maxThrust;
        this.maxSpeed = maxSpeed;
        this.distance = distance;
        this.brake = -maxBrakeAcc;
        pos.set(startPos);
        velocity.set(startVelocity);


    }

    @Override
    public Vec3 pos() {
        return pos;
    }

    @Override
    public void update(double durationSeconds) {
        double surfaceDist = planet.getSurfaceDist(pos(), picture.w()/2 );
        if (surfaceDist<=0) {
            //onSurface=true;
            Vec3 normal=planet.normalAt(pos());
            normal.setMul(surfaceDist);
            pos().setMinus(normal);

        }




        if (!onSurface){
            // Accelerate towards the target
            double distanceToTarget=pos().distance(target.pos());
            if (distanceToTarget>distance){
                acc.set(target.pos());
                acc.setMinus(pos);
                acc.setNormalized(); // Now we have direction towards player in acc, multiply with our thrust to get actual acceleration
                acc.setMul(maxThrust);
            }
            else{
                acc.set(target.pos());
                acc.setMinus(pos);
                acc.setNormalized(); // Now we have direction towards player in acc, multiply with our thrust to get actual acceleration
                acc.setMul(brake*maxThrust);
            }

            if (surfaceDist<=100) {
                //onSurface=true;
                Vec3 normal=planet.normalAt(pos());
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
        }
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y, double scale) {
        picture.drawCentered(g, x, y, scale);
    }


}


