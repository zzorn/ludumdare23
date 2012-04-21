package ludumdare23;

import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.entity.Entity3D;
import net.zzorn.utils.Vec3;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Shiera
 * Date: 21.4.2012
 * Time: 10:54
 * To change this template use File | Settings | File Templates.
 */
public class Planet extends Entity3D {
    private int width=100;
    private int height=100;
    private double mass_kg=1000000000000000.0;
    private Vec3 pos = new Vec3(0,0,0);
    private Vec3 velocity = new Vec3(0,0,0);

    public double getMass_kg() {
        return mass_kg;
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y) {
       g.setColor(Color.GREEN);
       g.fillOval(x-(width/2),y-(height/2), width, height);
    }

    @Override
    public void update(double durationSeconds) {

    }

}
