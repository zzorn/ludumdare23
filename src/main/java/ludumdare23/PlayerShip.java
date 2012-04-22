package ludumdare23;

import net.zzorn.gameflow.picture.Picture;
import net.zzorn.gameflow.input.*;
import net.zzorn.utils.Vec3;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * The ship controlled by the player.
 */
public class PlayerShip extends Ship {

    private int upKey    = KeyEvent.VK_W;
    private int downKey  = KeyEvent.VK_S;
    private int leftKey  = KeyEvent.VK_A;
    private int rightKey = KeyEvent.VK_D;
    private int fireKey  = KeyEvent.VK_SPACE;
    private MouseButton fireButton = LeftMouseButton$.MODULE$;

    private final Planet planet;
    private double distance = 0;
    private double angle = 270;
    private final static double DEGREES_TO_RADIANS= 2*Math.PI/360;
    private double angVelosity=0;
    private double maxAngAcc=200;
    private double maxAngVel=100;
    private double angAcc =0;
    private double angSlow = 3;
    private final Game game;
    private final Picture picture;
    private boolean firePressed = false;
    private Vec3 target = new Vec3(0,0,0);

    public PlayerShip(Game game, Planet planet, Picture picture) {
        this.game = game;
        this.picture = picture;
        distance = planet.getRadius()+200;
        this.planet = planet;
        pos().setX(planet.pos().x());
        pos().setY(-distance + planet.pos().y());

    }

    @Override
    protected void onUpdate(double durationSeconds) {
        angVelosity += angAcc * durationSeconds;
        angVelosity *= Math.max(0, 1.0 - angSlow * durationSeconds);

        if (angVelosity>=maxAngVel) {
            angVelosity=maxAngVel;
            //angAcc=0;
        }
        if (angVelosity<=-maxAngVel) {
            angVelosity=-maxAngVel;
            //angAcc= 0;
        }
        angle += angVelosity*durationSeconds;

        if (angle >= 360) angle -= 360;
        if (angle < 0) angle += 360;

        pos().setX(distance * Math.cos(angle * DEGREES_TO_RADIANS));
        pos().setY(distance * Math.sin(angle * DEGREES_TO_RADIANS));

        if (firePressed && getWeapon() != null && getWeapon().isReadyToFire()) {
            getWeapon().fire(target);
        }

    }


    public void onKeysUpdated(InputStatus inputStatus, double durationSeconds) {
        angAcc=0;

        if (inputStatus.isKeyHeld(rightKey)) {
            angAcc += maxAngAcc;
        }
        if (inputStatus.isKeyHeld(leftKey))  {
            angAcc += -maxAngAcc;
        }

        firePressed = inputStatus.isKeyHeld(fireKey) || inputStatus.isMouseButtonHeld(fireButton);
    }

    @Override
    public void onMouseMoved(int x, int y, InputStatus inputStatus, double durationSeconds) {
        // Update targeted spot
        game.getCamera().screenPosToWorldPos(x, y, target);
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y, double scale) {
        picture.drawCentered(g, x, y, scale);
    }



}
