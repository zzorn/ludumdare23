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

    private int upKey    = KeyEvent.VK_UP;
    private int downKey  = KeyEvent.VK_DOWN;
    private int leftKey  = KeyEvent.VK_LEFT;
    private int rightKey = KeyEvent.VK_RIGHT;
    private int clockwiseKey  = KeyEvent.VK_D;
    private int counterclockwiseKey = KeyEvent.VK_A;
    private int clockwiseKey2  = KeyEvent.VK_X;
    private int counterclockwiseKey2 = KeyEvent.VK_C;
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
    private final Picture picture;
    private boolean firePressed = false;
    private boolean controlsEnabled = true;
    private Vec3 target = new Vec3(0,0,0);

    public void setControlsEnabled(boolean controlsEnabled) {
        this.controlsEnabled = controlsEnabled;

        if (!controlsEnabled) {
            angAcc = 0;
            firePressed = false;
        }
    }

    public PlayerShip(Game game, Planet planet, Picture picture) {
        super(game, 3);
        this.picture = picture;
        distance = planet.getRadius()+Game.PLAYER_FLIGHT_HEIGHT;
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

        if (firePressed && isReadyToFire()) {
            fire(target);
        }

    }

    public void resetAtLevelStart() {
        restoreLife();
        angle = 270;
        angVelosity = 0;
        angAcc = 0;
    }


    public void onKeysUpdated(InputStatus inputStatus, double durationSeconds) {
        if (controlsEnabled) {
            angAcc=0;

            boolean clockwise = false;
            boolean counterClockwise = false;

            if (inputStatus.isKeyHeld(rightKey)) {
                if (angle>=270) clockwise = true;
                if (angle>=0 && angle<90) counterClockwise = true;
                if (angle>=90 && angle<180) counterClockwise = true;
                if (angle>=180 && angle<270) clockwise = true;
            }

            if (inputStatus.isKeyHeld(leftKey))  {
                if (angle>=270) counterClockwise = true;
                if (angle>=0 && angle<90) clockwise = true;
                if (angle>=90 && angle<180) clockwise = true;
                if (angle>=180 && angle<270) counterClockwise = true;

            }

            if (inputStatus.isKeyHeld(upKey))  {
                if (angle>=270) counterClockwise = true;
                if (angle>=0 && angle<90) counterClockwise = true;
                if (angle>=90 && angle<180) clockwise = true;
                if (angle>=180 && angle<270) clockwise = true;

            }

            if (inputStatus.isKeyHeld(downKey))  {
                if (angle>=270) clockwise = true;
                if (angle>=0 && angle<90) clockwise = true;
                if (angle>=90 && angle<180) counterClockwise = true;
                if (angle>=180 && angle<270) counterClockwise = true;

            }

            if (inputStatus.isKeyHeld(clockwiseKey) || inputStatus.isKeyHeld(clockwiseKey2)) clockwise = true;
            if (inputStatus.isKeyHeld(counterclockwiseKey) || inputStatus.isKeyHeld(counterclockwiseKey2)) counterClockwise = true;

            if (clockwise)        angAcc += maxAngAcc;
            if (counterClockwise) angAcc -= maxAngAcc;

            firePressed = inputStatus.isKeyHeld(fireKey) || inputStatus.isMouseButtonHeld(fireButton);
        }
    }

    @Override
    public void onKeyPressed(int key, InputStatus inputStatus, double durationSeconds) {
        if (controlsEnabled && key == fireKey) onFirePressed();
    }

    @Override
    public void onMouseButtonPressed(MouseButton button, int x, int y, InputStatus inputStatus, double durationSeconds) {
        if (controlsEnabled && button == fireButton) onFirePressed();
    }

    private void onFirePressed() {
        // Reload the weapon(s) instantly when the fire button is pressed down, it feels more responsive.
        // NOTE: Not very balanced if weapons are picked up
        for (Weapon weapon : getWeapons()) {
            weapon.instantReload();
        }
    }

    @Override
    public void onMouseMoved(int x, int y, InputStatus inputStatus, double durationSeconds) {
        // Update targeted spot
        getGame().getCamera().screenPosToWorldPos(x, y, target);
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y, double scale) {
        picture.drawCentered(g, x, y, scale);
    }


    @Override
    protected void onDestroyed() {
        getGame().spawnExplosion(pos(), velocity(), getRadius(), 80, 60, 0.6, 6, 600);
        getGame().spawnExplosion(pos(), velocity(), getRadius(), 25, 10, 1, 10, 1500);
        remove();
        getGame().getGameStateManager().changeState("GameOver");
    }
}
