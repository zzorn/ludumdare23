package ludumdare23;

import net.zzorn.gameflow.entity.Entity;
import net.zzorn.utils.Vec3;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Shiera
 * Date: 21.4.2012
 * Time: 22:03
 * To change this template use File | Settings | File Templates.
 */
public class EnemyShip extends Entity {
    private double velocity=1;


    private Vec3 myPos = new Vec3(0,0,0);
    private Vec3 myVelocity = new Vec3(0,0,0);

    @Override
    public Vec3 pos() {
        return myPos;
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y) {

    }

    @Override
    public Vec3 velocity() {
        return myVelocity;

    @Override
    public void update(double durationSeconds) {
        super.update(durationSeconds);    //To change body of overridden methods use File | Settings | File Templates.
    }
}


