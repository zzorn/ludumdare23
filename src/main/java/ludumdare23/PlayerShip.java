package ludumdare23;

import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.picture.Picture;
import net.zzorn.gameflow.input.*;
import net.zzorn.utils.Vec3;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * The ship controlled by the player.
 */
public class PlayerShip extends InputListenerAdapter implements Entity {

    private int upKey    = KeyEvent.VK_UP;
    private int downKey  = KeyEvent.VK_DOWN;
    private int leftKey  = KeyEvent.VK_LEFT;
    private int rightKey = KeyEvent.VK_RIGHT;

    private final Planet planet;
    private double distance = 0;
    private double angle = 270;
    private final static double DEGREES_TO_RADIANS= 2*Math.PI/360;
    private double angVelosity=0;
    private double angAcc =0;
    private double angSlow = 0.98 ;
    private final Picture image;

    private Vec3 myPos = new Vec3(0,0,0);
    private Vec3 myVelocity = new Vec3(0,0,0);


    @Override
    public Vec3 pos() {
        return myPos;
    }

    @Override
    public Vec3 velocity() {
        return myVelocity;
    }

    public PlayerShip(Planet planet, Picture image ) {
        this.image = image;
        distance = planet.getRadius_m()+50;
        this.planet = planet;
        pos().setX(planet.pos().x());
        pos().setY(-distance + planet.pos().y());

    }

    @Override
    public void update(double durationSeconds) {
        angVelosity += angAcc * durationSeconds;
        angVelosity *= angSlow;
        if (angVelosity>=100) {
            angVelosity=100;
            angAcc=0;
        }
        if (angVelosity<=-100) {
            angVelosity=-100;
            angAcc= 0;
        }
        angle += angVelosity*durationSeconds;

        if (angle >= 360) angle = angle - 360;
        if (angle < 0) angle = angle + 360;

        pos().setX(distance * Math.cos(angle * DEGREES_TO_RADIANS));
        pos().setY(distance * Math.sin(angle * DEGREES_TO_RADIANS));
    }


    public void onKeysUpdated(InputStatus inputStatus, double durationSeconds) {
        angAcc=0;


        if (inputStatus.isKeyHeld(rightKey)) {
            angAcc += 70;
        }
        if (inputStatus.isKeyHeld(leftKey))  {
            angAcc += -70;
        }
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y, double scale) {
        image.draw(g,x-image.w()/2,y-image.h()/2);
    }
}
