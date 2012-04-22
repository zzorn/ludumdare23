package ludumdare23;

import net.zzorn.gameflow.entity.Entity3D;
import net.zzorn.utils.Vec3;

import java.awt.*;

/**
 * A particle thrown up by an explosion
 */
public class Particle extends Entity3D {
    private Vec3 acc = new Vec3(0, 0, 0);
    private Vec3 airResVec = new Vec3(0, 0, 0);
    private double mass_kg = 1;
    private final Planet planet;
    private Color color;
    private double radius = 10;
    private double prevSurfaceDist = 1;
    private double airResistanceAmount = 10.0;
    private boolean onSurface = false;
    private double lifeTimeLeft = 10.0;
    private final Color deadColor = new Color(152, 68, 9);
    private double damage = 0;


    public double getMass_kg() {
        return mass_kg;
    }

    public double getRadius() {
        return radius;
    }

    public double getDamage() {
        return damage;
    }

    public Particle(Planet planet, Vec3 startPos, double radius, Vec3 velocity, double mass_kg, Color color, double airResistanceAmount, double damage, double lifeTimeLeft) {
        this.planet = planet;
        this.radius = radius;
        this.mass_kg = mass_kg;
        this.color = color;
        this.airResistanceAmount = airResistanceAmount;
        this.damage = damage;
        this.lifeTimeLeft = lifeTimeLeft;
        pos().set(startPos);
        velocity().set(velocity);

    }

    @Override
    public void update(double durationSeconds) {
        double surfaceDist = planet.getSurfaceDist(pos(), radius);

        if (velocity().length() < 5 && surfaceDist <= 10) {
            onSurface = true;


            velocity().zero();
            acc.zero();
        }

        if (!onSurface) {
            acc.set(planet.pos());
            acc.setMinus(pos());
            double distance = acc.length();
            if (distance == 0) distance = 0.001;

            double a = ((6.67E-11 * planet.getMass_kg()) / (distance * distance));
            if (distance < planet.getRadius()) {
                a = ((6.67E-11 * distance) / (planet.getRadius() * planet.getRadius()));
            }

            acc.setNormalized();
            acc.setMul(a);

            // Apply air resistance
            double airRes = -airResistanceAmount / (distance * distance);
            airResVec.set(velocity());
            airResVec.setMul(airRes);
            acc.setPlus(airResVec);


            velocity().setPlusMul(acc, durationSeconds);
            pos().setPlusMul(velocity(), durationSeconds);


            if (surfaceDist <=0  ){
                //put on surface if inside planet
                Vec3 normal=planet.normalAt(pos());
                normal.setMul(surfaceDist);
                pos().setMinus(normal);

            }

            if (surfaceDist <= 0 && prevSurfaceDist > 0) {
                // calculate bounce
                Vec3 v = planet.normalAt(pos());
                double angle = v.angleBetween(velocity());

                /*
                if (((0.5)*mass_kg* velocity().length()* velocity().length())>100000) {
                    this.color = deadColor;
                    velocity().zero();
                }
                else
                */
                v.setMul(velocity().length() * Math.cos(angle) * -2);
                velocity().setPlus(v);
                velocity().setMul(0.5);
                if (velocity().length() < 5) onSurface = true;

            }

            if (surfaceDist <= 0 &&  prevSurfaceDist <= 0) {
                velocity().zero();
            }


            prevSurfaceDist = surfaceDist;


        }

        if (onSurface) {
            Vec3 normal = planet.normalAt(pos());
            normal.setMul(surfaceDist);
            pos().setMinus(normal);
        }

        // Remove when particle is too old
        lifeTimeLeft -= durationSeconds;
        if (lifeTimeLeft <= 0) {
            getGroup().remove(this);
        }
    }

    @Override
    public void draw(Graphics2D g, int screenW, int screenH, int x, int y, double scale) {
        int r = (int) (radius * scale);
        //if (onSurface && color != Color.BLACK )g.setColor(Color.RED);
        //else
        g.setColor(color);
        g.fillOval(x - r, y - r, 2 * r, 2 * r);
        //g.fillRect(x-r,y-r,2*r,2*r);
    }
}
