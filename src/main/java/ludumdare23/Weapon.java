package ludumdare23;

import net.zzorn.gameflow.Facet;
import net.zzorn.gameflow.entity.Entity;
import net.zzorn.utils.Vec3;

import java.awt.*;

/**
 * Thing that SHOOTS!
 * At a target.
 */
public class Weapon {

    private final Planet planet;
    private Entity host = null;
    private double coolDownTime = 0.1;
    private double coolDownLeft = 0.0;
    private double shotSize = 5;
    private double shotSpeed = 100;
    private double shotMass = 10;
    private Color shotColor = Color.ORANGE;
    private double totalSeconds = 0;

    public Weapon(Planet planet, double coolDownTime, double shotSize, double shotSpeed, double shotMass, Color shotColor) {
        this.planet = planet;
        this.coolDownTime = coolDownTime;
        this.shotSize = shotSize;
        this.shotSpeed = shotSpeed;
        this.shotMass = shotMass;
        this.shotColor = shotColor;
    }

    public Entity getHost() { return host; }
    public void setHost(Entity host) { this.host = host; }

    void fire(Vec3 target) {
        if (host != null && coolDownLeft <= 0) {
            host.getGameMap().add(createShot(target));
            coolDownLeft = coolDownTime;
        }
    }

    void update(double durationSeconds) {
        totalSeconds += durationSeconds;
        coolDownLeft -= durationSeconds;
    }

    private Entity createShot(Vec3 target) {
        Vec3 startVel = Vec3.fromTo(host.pos(), target);
        startVel.setNormalized();
        startVel.setMul(shotSpeed );

        FloatingParticle shot = new FloatingParticle(planet, host.pos(), shotSize, startVel, shotMass, shotColor);
        return shot;
    }

}
