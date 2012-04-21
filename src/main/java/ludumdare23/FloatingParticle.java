package ludumdare23;

import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.entity.Entity2D;
import net.zzorn.gameflow.entity.Entity2D$class;
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
public class FloatingParticle implements Entity {
    private Vec2 pos=new Vec2(0,0);
    private Vec2 velocity=new Vec2(0,0);
    private Vec2 acc=new Vec2(0,2);

    public FloatingParticle(Planet planet){
        pos.set(100,100);
        velocity.set(10,-20);

    }

    @Override
    public void getPos(Vec3 worldPosOut) {
        worldPosOut.set(pos.x(),pos.y(),0);
    }

    @Override
    public void getVelocity(Vec3 velocityOut) {
       velocityOut.set(velocity.x(),velocity.y(),0);
    }

    @Override
    public void update(double durationSeconds) {
        velocity.$plus$times$eq(acc, durationSeconds);
        pos.$plus$times$eq(velocity,durationSeconds);
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y) {
        g.setColor(Color.BLUE);
        g.fillRect(x,y,50,50);


    }
}
