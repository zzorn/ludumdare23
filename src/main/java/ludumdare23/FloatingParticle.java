package ludumdare23;

import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.entity.Entity3D;
import net.zzorn.utils.Vec2;
import net.zzorn.utils.Vec3;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Shiera
 * Date: 21.4.2012
 * Time: 10:27
 * To change this template use File | Settings | File Templates.
 */
public class FloatingParticle extends Entity3D {
    private Vec3 acc=new Vec3(0,0,0);
    private Vec3 airResVec=new Vec3 (0,0,0);
    private double mass_kg=1;
    private final Planet planet;
    private double rad_m=25;
    private boolean prevInside=false;

    public double getMass_kg() {
        return mass_kg;
    }

    public FloatingParticle(Planet planet){
        this.planet = planet;
        pos().set((2*planet.getRadius_m()),0,0 );
        velocity().set(0, 170,0);

    }

    @Override
    public void update(double durationSeconds) {
        acc.set(planet.pos());
        acc.setMinus(pos());
        double distance=acc.length();
        double a=((6.67E-11*planet.getMass_kg())/(distance*distance));
        if (distance<planet.getRadius_m()){
            a=((6.67E-11*distance)/(planet.getRadius_m()*planet.getRadius_m()));
        }
        double airRes=(-1.0/(distance*distance));
        acc.normalizeLocal();
        acc.setMul(a);
        airResVec.set(velocity());
        airResVec.setMul(airRes);
        acc.setPlus(airResVec);
        velocity().$plus$times$eq(acc, durationSeconds);
        pos().$plus$times$eq(velocity(),durationSeconds);
        boolean inside = planet.isInside()

        if (inside && prevInside==false ){
            velocity().setMul(-1);
        }

        prevInside=inside;


    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y) {
        int r= (int) rad_m ;
        g.setColor(Color.BLUE);
        g.fillOval(x-r,y-r,2*r,2*r);


    }
}
