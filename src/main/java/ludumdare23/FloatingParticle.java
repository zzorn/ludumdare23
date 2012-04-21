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
    private  Color color;
    private double rad_m=10;
    private double prevSurfaceDist =1;
    private boolean onSurface=false;


    public double getMass_kg() {
        return mass_kg;
    }

    public FloatingParticle(Planet planet, Vec3 startPos, double radius, Vec3 velocity,double mass_kg, Color color  ){
        this.planet = planet;
        rad_m = radius;
        this.mass_kg = mass_kg;
        this.color = color;
        pos().set(startPos );
        velocity().set(velocity);

    }

    @Override
    public void update(double durationSeconds) {
        double surfaceDist = planet.getSurfaceDist(pos(), rad_m);

        if (velocity().length()< 5 && surfaceDist<=10){
            onSurface=true;


            velocity().zero();
            acc.zero();
        }

        if  (onSurface==false) {
            acc.set(planet.pos());
            acc.setMinus(pos());
            double distance=acc.length();
            if (distance==0) distance=1;



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



            velocity().setPlusMul(acc, durationSeconds);
            pos().setPlusMul(velocity(),durationSeconds);





            if (surfaceDist <=0  ){
                //put on surface if inside planet
                Vec3 normal=planet.normalAt(pos());
                normal.setMul(surfaceDist);
                pos().setMinus(normal);

            }

            if (surfaceDist <=0 && prevSurfaceDist>0 ) {
                  // calculate bounce
                Vec3 v=planet.normalAt(pos());
                double angle = v.angleBetween(velocity());
                if (((0.5)*mass_kg*velocity().length()*velocity().length())>100000) {
                    this.color = Color.BLACK;
                    velocity().zero();
                }
                else
                   v.setMul(velocity().length() * Math.cos(angle) * -2);
                   velocity().setPlus(v);
                   velocity().setMul(0.8);
                   if (velocity().length()<5) onSurface=true;

            }

            prevSurfaceDist = surfaceDist;


        }

        if (onSurface==true) {


            Vec3 normal=planet.normalAt(pos());
            normal.setMul(surfaceDist);
            pos().setMinus(normal);
        }
    }

        @Override
        public void draw(Graphics2D g, int screenW, int screenH, int x, int y) {
            int r= (int) rad_m ;
            if (onSurface==true && color != Color.BLACK )g.setColor(Color.RED);
            else
            g.setColor(color);
            g.fillOval(x-r,y-r,2*r,2*r);


         }

}
