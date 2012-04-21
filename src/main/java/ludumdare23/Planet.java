package ludumdare23;

import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.entity.Entity3D;
import net.zzorn.utils.Vec3;
import sun.font.TrueTypeFont;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Shiera
 * Date: 21.4.2012
 * Time: 10:54
 * To change this template use File | Settings | File Templates.
 */
public class Planet extends Entity3D {
    private int width = 100;
    private int height = 100;
    private double mass_kg = 100000000000000000.0;
    private Vec3 pos = new Vec3(0, 0, 0);
    private Vec3 velocity = new Vec3(0, 0, 0);

    public double getRadius_m() {
        return radius_m;
    }

    private double radius_m = 1000;


    public double getMass_kg() {
        return mass_kg;
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y, double scale) {
        g.setColor(Color.GREEN);
        int r = (int) (radius_m * scale);
        g.fillOval(x - r, y - r, 2 * r, 2 * r);
    }

    @Override
    public void update(double durationSeconds) {

    }


    public double getSurfaceDist (Vec3 point, double radius) {
        double midDist = point.distance(pos());
        double distance = midDist - radius_m - radius;
        return (distance);
    }




    public Vec3 normalAt(Vec3 point) {
        Vec3 normal = new Vec3(0, 0, 0);
        normal.set(point);
        normal.setMinus(pos());
        normal.normalizeLocal();
        return normal;
    }
}
