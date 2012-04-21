package ludumdare23;

import net.zzorn.gameflow.input.InputHandler;
import net.zzorn.gameflow.input.InputListener;
import net.zzorn.utils.Vec3;

import java.awt.event.KeyEvent;

/**
 * The ship controlled by the player.
 */
public class PlayerShip extends Ship implements InputListener {

    private int upKey    = KeyEvent.VK_UP;
    private int downKey  = KeyEvent.VK_DOWN;
    private int leftKey  = KeyEvent.VK_LEFT;
    private int rightKey = KeyEvent.VK_RIGHT;

    private Vec3 forwardVector = new Vec3(0,-1,0);
    private Vec3 rightVector   = new Vec3(1,0,0);

    private double shipForwardThrust = 30;
    private double shipBackThrust = 30;
    private double shipSideThrust = 30;
    private final Planet planet;
    private double distance = 0;
    private double angle = 0;
    private final static double DEGREES_TO_RADIANS= 2*Math.PI/360;
    private double angVelosity=0;
    private double angAcc =0;
    private double angSlow = 0.9 ;

    public PlayerShip(Planet planet) {
        distance = planet.getRadius_m()+50;
        this.planet = planet;
        pos().setX(planet.pos().x());
        pos().setY(-distance + planet.pos().y());

    }

    @Override
    public void update(double durationSeconds) {
        super.update(durationSeconds);
        angVelosity +=angAcc*durationSeconds;
        angVelosity *=angSlow;
        if (angVelosity>=100) {
            angVelosity=100;
            angAcc=0;
        }
        if (angVelosity<=-100) {
            angVelosity=-100;
            angAcc= 0;
        }
        angle += angVelosity*durationSeconds;

        if (angle>=360) angle=angle-360;
        if (angle<0) angle=angle+360;

        pos().setX(distance*Math.cos(angle*DEGREES_TO_RADIANS));
        pos().setY(distance*Math.sin(angle*DEGREES_TO_RADIANS));
    }

    @Override
    public void onKeyPressed(int keyCode, InputHandler keyHandler, double durationSeconds) {}

    @Override
    public void onKeyReleased(int keyCode, InputHandler keyHandler, double durationSeconds) {}

    @Override
    public void onKeysUpdated(InputHandler keyHandler, double durationSeconds) {
        thrust().zero();
        angAcc=0;
        angSlow=0.99;

        if (keyHandler.isPressed(rightKey)) {
            angAcc += 20;
            angSlow = 0.999;

        }
        if (keyHandler.isPressed(leftKey))  {
            angAcc += -20;
            angSlow = 0.999;

        }


    }
}
