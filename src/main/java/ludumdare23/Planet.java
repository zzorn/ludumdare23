package ludumdare23;

import net.zzorn.gameflow.entity.Entity;
import net.zzorn.utils.Vec3;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Shiera
 * Date: 21.4.2012
 * Time: 10:54
 * To change this template use File | Settings | File Templates.
 */
public class Planet implements Entity {
    private int width=100;
    private int height=100;

    @Override
    public void getPos(Vec3 posOut) {
        posOut.zero();
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y) {
       g.setColor(Color.GREEN);
       g.fillOval(x-(width/2),y-(height/2), width, height);
    }

    @Override
    public void update(double durationSeconds) {

    }

    @Override
    public void getVelocity(Vec3 velocityOut) {
       velocityOut.zero();

    }
}
