package ludumdare23;

import ludumdare23.state.GameStateManager;
import ludumdare23.state.PlayingLevelState;
import ludumdare23.state.StartScreenState;
import net.zzorn.gameflow.*;
import net.zzorn.gameflow.camera.Camera;
import net.zzorn.gameflow.camera.TrackingCamera;
import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.font.FixedBitmapFont;
import net.zzorn.gameflow.input.InputListenerAdapter;
import net.zzorn.gameflow.input.InputStatus;
import net.zzorn.gameflow.picture.Picture;
import net.zzorn.utils.ColorUtils$;
import net.zzorn.utils.Vec3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

/**
 * Ludumdare 23 game jam entry, main game class.
 */
public class Game extends GameBase {

    public static final double PLAYER_FLIGHT_HEIGHT = 400;

    private EntityGroup<EnemyShip>  enemyGroup         = new EntityGroup<EnemyShip>();
    private EntityGroup<PlayerShip> playerGroup        = new EntityGroup<PlayerShip>();
    private EntityGroup<Particle>   enemyBulletGroup   = new EntityGroup<Particle>();
    private EntityGroup<Particle>   playerBulletGroup  = new EntityGroup<Particle>();
    private EntityGroup<Planet>     planetGroup        = new EntityGroup<Planet>();
    private EntityGroup<Particle>   effectsGroup       = new EntityGroup<Particle>();

    private FixedBitmapFont font = null;
    private MessageScreenFacet messageScreen = null;
    private GameStateManager gameStateManager = null;

    private final TrackingCamera trackingCamera = new TrackingCamera(null, 0, 3);
    private final double minCameraScale = 0.2;
    private final double maxCameraScale = 0.6;

    private PlayerShip player = null;

    private Planet planet = null;
    private Random random = new Random(42);


    private final CollisionHandler<Particle, Damageable> BULLET_COLLISION_HANDLER = new CollisionHandler<Particle, Damageable>(){
        public void onCollision(Particle bullet, Damageable damageable) {
            boolean wasDestroyed = damageable.isDestroyed();
            damageable.damage(bullet.getDamage(), bullet.pos());
            bullet.remove();
        }
    };
    private HudFacet hudFacet;

    public Game() {
        super("LD23", 300.0, 800, 600, "", null);

    }

    public MessageScreenFacet getMessageScreen() {
        return messageScreen;
    }

    public TrackingCamera getTrackingCamera() {
        return trackingCamera;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    public Planet getPlanet() {
        return planet;
    }

    public HudFacet getHudFacet() {
        return hudFacet;
    }

    /**
     * Called before the main loop starts, after the screen has been created.
     */
    @Override
    public void init() {

        // Create font
        font = new FixedBitmapFont(pictureStore().getImage(
                "images/spacefont.png"),
                8,
                11,
                "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ0123456789!?,.():- ",
                "abcdefghijklmnopqrstuvwxyzåäö0123456789!?,.[];_ ",
                12*4-1);

        // Setup entity groups (in drawing order)
        addFacet(planetGroup);
        addFacet(effectsGroup);
        addFacet(enemyBulletGroup);
        addFacet(playerBulletGroup);
        addFacet(enemyGroup);
        addFacet(playerGroup);

        // HUD
        hudFacet = new HudFacet(this);
        addFacet(hudFacet);

        // Setup message screen
        messageScreen = new MessageScreenFacet(font);
        addFacet(messageScreen);
        //messageScreen.showMessage("Get Ready!", 3, 10);

        // Set nice cursor
        setCursor("images/kursor.png");

        // Setup camera
        setCamera(getTrackingCamera());
        getTrackingCamera().setCameraScale(0.4);


        // Game state setup
        gameStateManager = new GameStateManager(this);
        inputHandler().addListener(gameStateManager);
        addFacet(gameStateManager);
        gameStateManager.addState(new StartScreenState());
        gameStateManager.addState(new PlayingLevelState());
        gameStateManager.changeState("StartScreen");



        // Print inputs for debugging purposes
        //inputHandler().addListener(new PrintingInputListener());

        // Register quit key and page up and page down keys to change the camera zoom
        inputHandler().addListener(new InputListenerAdapter(){
            @Override
            public void onKeyPressed(int key, InputStatus inputStatus, double durationSeconds) {
                if (key == KeyEvent.VK_ESCAPE) stop();
                if (key == KeyEvent.VK_PAGE_DOWN) zoomIn();
                if (key == KeyEvent.VK_PAGE_UP)  zoomOut();
            }

            @Override
            public void onMouseWheelMoved(int mouseWheelMovement, int x, int y, InputStatus inputStatus, double durationSeconds) {
                zoomCamera(mouseWheelMovement);
            }
        });

        // Set up collisions
        enemyBulletGroup.onCollideWith(planetGroup, BULLET_COLLISION_HANDLER);
        enemyBulletGroup.onCollideWith(playerGroup, BULLET_COLLISION_HANDLER);
        playerBulletGroup.onCollideWith(planetGroup, BULLET_COLLISION_HANDLER);
        playerBulletGroup.onCollideWith(enemyGroup, BULLET_COLLISION_HANDLER);
    }

    private void zoomCamera(int steps) {
        for (int i = 0; i < Math.abs(steps); i++) {
            if (steps < 0) zoomOut();
            else zoomIn();
        }
    }

    private void zoomIn() {
        double camScale = getTrackingCamera().cameraScale();
        if (camScale > minCameraScale) getTrackingCamera().setCameraScale(camScale * 0.75);
    }

    private void zoomOut() {
        double camScale = getTrackingCamera().cameraScale();
        if (camScale < maxCameraScale) getTrackingCamera().setCameraScale(camScale * 1.5);
    }

    public PlayerShip getPlayer() {
        return player;
    }

    public void loadLevel(int levelNum) {
        // Create planet
        planet = new Planet(this);

        planet.setMaxHitPoints(4000);
        planet.setHitPoints(4000);
        planetGroup.add(planet);
        hudFacet.setPlanet(planet);
    }

    public void createPlayer() {
        // Create players ship
        player = new PlayerShip(this, planet, this.pictureStore().get("images/playership.png", 2.0));
        inputHandler().addListener(player);
        player.setWeapon(new Weapon(planet, playerBulletGroup, 0.08, 10, 1500, 1, Color.CYAN, 8, 0.5, 500, 25));
        player.setMaxHitPoints(300);
        player.setHitPoints(300);
        playerGroup.add(player);
        getTrackingCamera().setTrackedEntity(player);
        hudFacet.setPlayer(player);
    }

    public void createEnemies() {
        // Add enemies
        for (int i = 0; i < 10; i++) {
            // Planet shooters
            enemyGroup.add(createPlanetEnemy(planet, planet, "images/enemyship1.png", 1500, 2,
                    createWeapon(planet, 5, 15.0, 200.0, new Color(255, 131, 0), 1, 0, 30)));
        }
        for (int i = 0; i < 10; i++) {
            // Player shooters
            enemyGroup.add(createPlanetEnemy(planet, player, "images/enemyship2.png", 400, 10,
                    createWeapon(planet, 0.1, 10.0, 800.0, new Color(255, 195, 0), 8, 2, 5)));
        }
    }
    

    @Override
    public void render(Graphics2D screen, Camera camera) {
        super.render(screen, camera);
        //font.drawText(screen, "ABC", 100, 100, 0, 0, 10);

    }

    private EnemyShip createPlanetEnemy(Planet planet, Entity target, String image, double brakeDistance, int maxBrakeAcc, Weapon weapon) {

        // Randomize enemy start position and speed
        Vec3 startPos = createRandomVec(10000);
        Vec3 startVelocity = createRandomVec(1000);

        // Create enemy ship
        double maxSpeed = randomValue(10, 1000);
        double enginePower = randomValue(10, 1000);
        double shootDistance = randomValue(200, 5000);
        Picture picture = pictureStore().get(image, 2.0);
        EnemyShip enemyShip = new EnemyShip(this, planet, target, picture, startPos, startVelocity, enginePower, maxSpeed, brakeDistance, maxBrakeAcc, shootDistance);
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

    private Color createRandomColor(double hue, double hueVar, double sat, double satVar, double lum, double lumVar) {
        double h = random.nextGaussian() * hueVar + hue;
        double s = random.nextGaussian() * satVar + sat;
        double l = random.nextGaussian() * lumVar + lum;

        return new Color(ColorUtils$.MODULE$.HSLtoRGB(h, s, l, 1), false);
    }


    public void spawnExplosion(Vec3 pos, Vec3 velocity, double area, double particleSize, double particleAmount, double particleHeat, double particleLifeTime, double speed) {
        double amount  = gaussianValue(particleAmount, 0.3, 1, 100);
        double heat = Math.min(1, Math.max(0, particleHeat));
        for (int i = 0; i < (int) amount; i++) {
            double radius = gaussianValue(particleSize, 0.4, 1, 1000);
            double lifeTime = gaussianValue(particleLifeTime, 1, 0.1, 1000);
            double mass = gaussianValue(5, 0.3, 0.1, 100);
            double airDrag = gaussianValue(10000, 1.0, 0.1, 100000);
            double damage = 0;
            Vec3 vel = createRandomVec(gaussianValue(speed, 0.8, 0, 3000));
            vel.setPlus(velocity);
            Vec3 p = createRandomVec(gaussianValue(area, 0.2, 0, 3000));
            p.setPlus(pos);
            Color color = createRandomColor(
                    0.01+ heat*0.2, 0.01,
                    0.3 + heat*0.8, 0.2,
                    0.4 +heat*0.35, 0.1);
            effectsGroup.add(new Particle(getPlanet(), p, radius, vel, mass, color, airDrag, damage, lifeTime));
        }
    }

}