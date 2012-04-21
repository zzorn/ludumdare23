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
    private PlayerShip player;
    private final Picture picture;
    private double maxThrust;
    private double maxSpeed;

    private Vec3 pos      = new Vec3(0,0,0);
    private Vec3 velocity = new Vec3(0,0,0);
    private Vec3 acc      = new Vec3(0,0,0);

    public EnemyShip(Planet planet, PlayerShip player, Picture picture, double maxThrust, double maxSpeed) {
        this.planet = planet;
        this.player = player;
        this.picture = picture;
        this.maxThrust = maxThrust;
        this.maxSpeed = maxSpeed;
    }

    @Override
    public Vec3 pos() {
        return pos;
    }

    @Override
    public Vec3 velocity() {
        return velocity;
    }

    @Override
    public void update(double durationSeconds) {
        // Accelerate towards the player
        acc.set(player.pos());
        acc.setMinus(pos);
        acc.setNormalized(); // Now we have direction towards player in acc, multiply with our thrust to get actual acceleration
        acc.setMul(maxThrust);

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

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y, double scale) {
        picture.draw(g,x-picture.w()/2,y-picture.h()/2);
    }


}


