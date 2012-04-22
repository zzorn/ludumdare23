package ludumdare23;

import net.zzorn.gameflow.CollisionHandler;
import net.zzorn.gameflow.EntityGroup;
import net.zzorn.gameflow.GameBase;
import net.zzorn.gameflow.camera.TrackingCamera;
import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.input.InputListenerAdapter;
import net.zzorn.gameflow.input.InputStatus;
import net.zzorn.gameflow.picture.Picture;
import net.zzorn.utils.Vec3;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Ludumdare 23 game jam entry, main game class.
 */
public class Game extends GameBase {

    private static final CollisionHandler<Particle, Damageable> BULLET_COLLISION_HANDLER = new CollisionHandler<Particle, Damageable>(){
        public void onCollision(Particle bullet, Damageable damageable) {
            damageable.damage(bullet.getDamage());
            bullet.remove();
        }
    };

    private EntityGroup<EnemyShip>  enemyGroup         = new EntityGroup<EnemyShip>();
    private EntityGroup<PlayerShip> playerGroup        = new EntityGroup<PlayerShip>();
    private EntityGroup<Particle>   enemyBulletGroup   = new EntityGroup<Particle>();
    private EntityGroup<Particle>   playerBulletGroup  = new EntityGroup<Particle>();
    private EntityGroup<Planet>     planetGroup        = new EntityGroup<Planet>();

    private Random random = new Random(42);


    public Game() {
        super("LD23", 300.0, 1000, 800, "", null);

    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    /**
     * Called before the main loop starts, after the screen has been created.
     */
    @Override
    public void init() {

        // Setup entity groups (in drawing order)
        addFacet(planetGroup);
        addFacet(enemyBulletGroup);
        addFacet(playerBulletGroup);
        addFacet(enemyGroup);
        addFacet(playerGroup);

        // Set nice cursor
        setCursor("images/kursor.png");

        // Create planet
        Planet planet = new Planet();
        planet.setMaxHitPoints(1000);
        planet.setHitPoints(1000);
        planetGroup.add(planet);

        // Create players ship
        PlayerShip player = new PlayerShip(this, planet, this.pictureStore().get("images/playership.png", 2.0));
        inputHandler().addListener(player);
        player.setWeapon(new Weapon(planet, playerBulletGroup, 0.08, 10, 1500, 1, Color.CYAN, 8, 0.5, 500, 25));
        playerGroup.add(player);

        // Create a camera that tracks the player
        final TrackingCamera camera = new TrackingCamera(player , 0, 1.5);
        camera.setCameraScale(0.4);
        setCamera(camera);


        // Add enemies
        for (int i = 0; i < 5; i++) {
            // Planet shooters
            enemyGroup.add(createPlanetEnemy(planet, planet, "images/enemyship1.png", 1500, 2,
                    createWeapon(planet, 5, 15.0, 200.0, new Color(255, 131, 0), 1, 0, 30)));
        }
        for (int i = 0; i < 10; i++) {
            // Player shooters
            enemyGroup.add(createPlanetEnemy(planet, player, "images/enemyship2.png", 400, 10,
                    createWeapon(planet, 0.1, 10.0, 500.0, new Color(255, 195, 0), 8, 2, 5)));
        }


        // Print inputs for debugging purposes
        //inputHandler().addListener(new PrintingInputListener());

        // Register page up and page down keys to change the camera zoom
        inputHandler().addListener(new InputListenerAdapter(){
            @Override
            public void onKeysUpdated(InputStatus inputStatus, double durationSeconds) {
                if (inputStatus.isKeyHeld(KeyEvent.VK_PAGE_UP))   camera.setCameraScale(camera.cameraScale() * 0.5);
                if (inputStatus.isKeyHeld(KeyEvent.VK_PAGE_DOWN)) camera.setCameraScale(camera.cameraScale() * 2.0);
            }
        });

        // Set up collisions
        enemyBulletGroup.onCollideWith(playerGroup, BULLET_COLLISION_HANDLER);
        enemyBulletGroup.onCollideWith(planetGroup, BULLET_COLLISION_HANDLER);
        playerBulletGroup.onCollideWith(enemyGroup, BULLET_COLLISION_HANDLER);
        playerBulletGroup.onCollideWith(planetGroup, BULLET_COLLISION_HANDLER);
    }


    private EnemyShip createPlanetEnemy(Planet planet, Entity target, String image, int distance, int maxBrakeAcc, Weapon weapon) {

        // Randomize enemy start position and speed
        Vec3 startPos = createRandomVec(10000);
        Vec3 startVelocity = createRandomVec(1000);

        // Create enemy ship
        double maxSpeed = randomValue(10, 1000);
        double enginePower = randomValue(10, 1000);
        double shootDistance = randomValue(200, 5000);
        Picture picture = pictureStore().get(image, 2.0);
        EnemyShip enemyShip = new EnemyShip(planet, target, picture, startPos, startVelocity, enginePower, maxSpeed, distance, maxBrakeAcc, shootDistance);
        enemyShip.setWeapon(weapon);
        return enemyShip;
    }

    private Weapon createWeapon(Planet planet, double coolDownTime, double size, double speed, Color color, double clipSize, double clipReloadTime, double damage) {
        coolDownTime = gaussianValue(coolDownTime, 0.2, 0.01, 100);
        size         = gaussianValue(size,         0.2, 0.01, 100);
        damage       = gaussianValue(damage,       0.1, 0.01, 1000);
        speed        = gaussianValue(speed,        0.2, 0,   100000);
        clipSize     = gaussianValue(clipSize,     0.2, 1,   10000);
        clipReloadTime = gaussianValue(clipReloadTime, 0.2, 0,   10000);
        return new Weapon(planet, enemyBulletGroup, coolDownTime, size, speed, size * 10, color, (int) clipSize, clipReloadTime, 100000, damage);
    }

    private double randomValue(double start, double end) {
        return random.nextDouble() * (end - start) + start;
    }

    private double gaussianValue(double average, double deviation, double min, double max) {
        double v = random.nextGaussian() * average * deviation + average;
        if (v < min) return min;
        else if (v > max) return max;
        else return v;
    }

    private Vec3 createRandomVec(double area) {
        Vec3 randomPos = Vec3.random();
        randomPos.setMul(area);
        randomPos.setZ(0);
        return randomPos;
    }
}