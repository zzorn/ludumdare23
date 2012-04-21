package ludumdare23;

import net.zzorn.gameflow.entity.Entity;
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
public class Planet implements Entity {
    private double mass_kg=100000000000000000.0;

    public double getRadius_m() {
        return radius_m;
    }

    private double radius_m=100;


    public double getMass_kg() {
        return mass_kg;
    }

    @Override
    public void getPos(Vec3 posOut) {
        posOut.zero();
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y) {
       g.setColor(Color.GREEN);
       int r= (int) radius_m ;
       g.fillOval(x-r,y-r, 2*r, 2*r);
    }

    @Override
    public void update(double durationSeconds) {

    }

    @Override
    public void getVelocity(Vec3 velocityOut) {
       velocityOut.zero();

    }
    public boolean isInside(Vec3 point, double radius){

      double distance= midDist-radius_m-radius;
        return (distance<=0 );


    }
}
