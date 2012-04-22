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
    private double clipReloadTimeLeft = 0.0;
    private double shotSize = 5;
    private double shotSpeed = 100;
    private double shotMass = 10;
    private int clipSize = 10;
    private int clipLeft = clipSize;
    private double clipReloadTime = 3;
    private double shotAirResistance = 10000;
    private Color shotColor = Color.ORANGE;
    private double totalSeconds = 0;

    public Weapon(Planet planet, double coolDownTime, double shotSize, double shotSpeed, double shotMass, Color shotColor, int clipSize, double clipReloadTime, double shotAirResistance) {
        this.planet = planet;
        this.coolDownTime = coolDownTime;
        this.shotSize = shotSize;
        this.shotSpeed = shotSpeed;
        this.shotMass = shotMass;
        this.shotColor = shotColor;
        this.clipSize = clipSize;
        this.clipReloadTime = clipReloadTime;
        this.shotAirResistance = shotAirResistance;
    }

    public Entity getHost() { return host; }
    public void setHost(Entity host) { this.host = host; }

    public void fire(Vec3 target) {
        if (host != null && isReadyToFire()) {
            host.getGameMap().add(createShot(target));
            coolDownLeft = coolDownTime;
            clipLeft--;
            if (clipLeft <= 0) {
                clipLeft = clipSize;
                clipReloadTimeLeft = clipReloadTime;
            }
        }
    }

    public void update(double durationSeconds) {
        totalSeconds += durationSeconds;
        coolDownLeft -= durationSeconds;
        clipReloadTimeLeft -= durationSeconds;
        if (coolDownLeft < -1) coolDownLeft = -1;
        if (clipReloadTimeLeft < -1) clipReloadTimeLeft = -1;
    }

    public boolean isReadyToFire() {
        return coolDownLeft <= 0 && clipReloadTimeLeft <= 0;
    }

    private Entity createShot(Vec3 target) {
        Vec3 startVel = Vec3.fromTo(host.pos(), target);
        startVel.setNormalized();
        startVel.setMul(shotSpeed);
        startVel.setPlus(host.velocity());

        FloatingParticle shot = new FloatingParticle(planet, host.pos(), shotSize, startVel, shotMass, shotColor, shotAirResistance);
        return shot;
    }

}
