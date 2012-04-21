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

    @Override
    public void onKeyPressed(int keyCode, InputHandler keyHandler, double durationSeconds) {}

    @Override
    public void onKeyReleased(int keyCode, InputHandler keyHandler, double durationSeconds) {}

    @Override
    public void onKeysUpdated(InputHandler keyHandler, double durationSeconds) {
        thrust().zero();

        if (keyHandler.isPressed(upKey))    thrust().setPlusMul(forwardVector,  shipForwardThrust);
        if (keyHandler.isPressed(downKey))  thrust().setPlusMul(forwardVector, -shipBackThrust);
        if (keyHandler.isPressed(rightKey)) thrust().setPlusMul(rightVector,    shipSideThrust);
        if (keyHandler.isPressed(leftKey))  thrust().setPlusMul(rightVector,   -shipSideThrust);
    }
}
