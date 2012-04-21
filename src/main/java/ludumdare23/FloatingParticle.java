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

    public double getMass_kg() {
        return mass_kg;
    }

    public FloatingParticle(Planet planet){
        this.planet = planet;
        pos().set(100,100,0);
        velocity().set(10, -20,0);

    }

    @Override
    public void update(double durationSeconds) {
        acc.set(planet.pos());
        acc.setMinus(pos());
        double distance=acc.length();
        double a=((6.67E-11*planet.getMass_kg())/(distance*distance));
        double airRes=(-1.0/(distance*distance));
        acc.normalizeLocal();
        acc.setMul(a);
        airResVec.set(velocity());
        airResVec.setMul(airRes);
        acc.setPlus(airResVec);
        velocity().setPlusMul(acc, durationSeconds);
        pos().setPlusMul(velocity(),durationSeconds);
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y) {
        g.setColor(Color.BLUE);
        g.fillRect(x,y,50,50);


    }
}
