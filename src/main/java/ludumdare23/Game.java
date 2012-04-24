package ludumdare23;

import ludumdare23.enemy.CampaignFactory;
import ludumdare23.enemy.EnemyCampaign;
import ludumdare23.enemy.EnemyShip;
import ludumdare23.state.*;
import net.zzorn.gameflow.*;
import net.zzorn.gameflow.camera.Camera;
import net.zzorn.gameflow.camera.TrackingCamera;
import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.font.FixedBitmapFont;
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

    private final TrackingCamera trackingCamera = new TrackingCamera(null, 0.5, 3);
    private final double minCameraScale = 0.2;
    private final double maxCameraScale = 0.6;

    private PlayerShip player = null;

    private Planet planet = null;
    private Random random = new Random(42);

    private int level = 1;
    private double levelClock = 0;

    private CampaignFactory campaignFactory;

    private final CollisionHandler<Particle, Damageable> BULLET_COLLISION_HANDLER = new CollisionHandler<Particle, Damageable>(){
        public void onCollision(Particle bullet, Damageable damageable) {
            boolean wasDestroyed = damageable.isDestroyed();
            damageable.damage(bullet.getDamage(), bullet.pos());
            bullet.remove();
        }
    };
    private HudFacet hudFacet;
    private EnemyCampaign enemyCampaign;

    public Game() {
        super("LD23", 300.0, 800, 600, "", null);

    }

    public MessageScreenFacet getMessageScreen() {
        return messageScreen;
    }

    public TrackingCamera getTrackingCamera() {
        return trackingCamera;
    }

    public int getLevel() {
        return level;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public Planet getPlanet() {
        return planet;
    }

    public HudFacet getHudFacet() {
        return hudFacet;
    }

    public EntityGroup<Particle> getEnemyBulletGroup() {
        return enemyBulletGroup;
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
                "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ0123456789!?,.():- abcdefghijklmnopqrstuvwxyzåäö",
                "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ0123456789!?,.[];_ abcdefghijklmnopqrstuvwxyzåäö",
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
        gameStateManager.addState(new GameOverState());
        gameStateManager.addState(new NextLevelState());
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

        // Setup factory for enemies
        campaignFactory = new CampaignFactory(random, this);
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
        random.setSeed(levelNum*13);
        level = levelNum;
        planetGroup.clear();
        clearEntities();

        // Create planet
        planet = new Planet(this, random);

        planet.setMaxHitPoints(4000);
        planet.setHitPoints(4000);
        planetGroup.add(planet);
        hudFacet.setPlanet(planet);

    }

    public void nextLevel() {
        player.resetAtLevelStart();
        loadLevel(level + 1);
    }

    public void restartLevel() {
        clearEntities();

        planetGroup.clear();
        planet.restore();
        planetGroup.add(planet);

        playerGroup.clear();
        createPlayer();
    }

    private void clearEntities() {
        effectsGroup.clear();
        enemyGroup.clear();
        enemyBulletGroup.clear();
        playerBulletGroup.clear();
    }

    public void createPlayer() {
        // Create players ship
        player = new PlayerShip(this, planet, this.pictureStore().get("images/playership.png", 2.0));
        inputHandler().addListener(player);
        Weapon standardWeapon = new Weapon(planet, playerBulletGroup, 0.075, 15, 1200, Color.CYAN, 10, 0, 300, 25);
        //Weapon radnWEap = CampaignFactory.createWeapon(this, playerBulletGroup, random, 0.5, 0.5, 0);
        player.addWeapon(standardWeapon);
        player.setMaxHitPoints(300);
        player.setHitPoints(300);
        playerGroup.add(player);
        getTrackingCamera().setTrackedEntity(player);
        hudFacet.setPlayer(player);
    }

    public void createEnemies(int level) {
        int number = (int)(((double)level / ((double)level + 10)) * 50) + level;

        // Add enemies
        for (int i = 0; i < (int)(0.7*number); i++) {
            // Player shooters
            enemyGroup.add(createEnemy(planet, player, "images/enemyship2.png", 400, 10,
                    createWeapon(planet, 0.1, 10.0, 800.0, new Color(255, 195, 0), 8, 2, 5), 2.0, 50));
        }
        for (int i = 0; i < (int)(0.3*number); i++) {
            // Planet shooters
            enemyGroup.add(createEnemy(planet, planet, "images/enemyship1.png", 500, 2,
                    createWeapon(planet, 5, 15.0, 200.0, new Color(255, 131, 0), 1, 0, 30), 2.0, 100));
        }
        if (level >= 3) for (int i = 0; i < (int)(0.2*number); i++) {
            enemyGroup.add(createEnemy(planet, player, "images/enemyship4.png", 700, 1,
                    createWeapon(planet, 0.05, 12.0, 1200.0, new Color(255, 200, 0), 10, 3, 5), 2.0, 200));
        }
        if (level >= 5) for (int i = 0; i < (int)(0.05*number); i++) {
            enemyGroup.add(createEnemy(planet, planet, "images/mothership1.png", 1000, 1,
                    createWeapon(planet, 1, 30.0, 1000.0, new Color(255, 255, 0), 3, 5, 50), 8.0, 400));
        }
    }
    
    @Override
    public void render(Graphics2D screen, Camera camera) {
        super.render(screen, camera);
        //font.drawText(screen, "ABC", 100, 100, 0, 0, 10);

    }

    private EnemyShip createEnemy(Planet planet, Entity target, String image, double brakeDistance, int maxBrakeAcc, Weapon weapon, double picScale, double hitpoints) {

        // Randomize enemy start position and speed
        Vec3 startPos = MathUtils.createRandomVec(random, 3000);
        Vec3 startVelocity = MathUtils.createRandomVec(random, 1000);

        // Create enemy ship
        double maxSpeed = MathUtils.randomValue(random, 100, 1000);
        double enginePower = MathUtils.randomValue(random, 100, 1000);
        double shootDistance = MathUtils.randomValue(random, 300, 1000);
        Picture picture = pictureStore().get(image, picScale);
        //EnemyShip enemyShip = new EnemyShip(this, planet, target, picture, startPos, startVelocity, enginePower, maxSpeed, brakeDistance, maxBrakeAcc, shootDistance);
        EnemyShip enemyShip = new EnemyShip(this, planet, picture, enginePower, maxSpeed, 2, brakeDistance+shootDistance, brakeDistance, target);
        enemyShip.pos().set(startPos);
        enemyShip.velocity().set(startVelocity);
        enemyShip.addWeapon(weapon);

        enemyShip.setMaxHitPoints(hitpoints);
        enemyShip.setHitPoints(hitpoints);
        /*
        enemyShip.addBehavior(new AvoidBehavior(target, 0, 200, 1));
        enemyShip.addBehavior(new AvoidBehavior(planet, 0, 100, 0.8));
        enemyShip.addBehavior(new FollowBehavior(target, 100, 100000, 1));
        enemyShip.addBehavior(new AttackBehavior(target, 0, shootDistance));
        */
        return enemyShip;
    }

    private Weapon createWeapon(Planet planet, double coolDownTime, double size, double speed, Color color, double clipSize, double clipReloadTime, double damage) {
        coolDownTime = MathUtils.gaussianValue(random, coolDownTime, 0.2, 0.01, 100);
        size         = MathUtils.gaussianValue(random, size, 0.2, 0.01, 100);
        damage       = MathUtils.gaussianValue(random, damage, 0.1, 0.01, 1000);
        speed        = MathUtils.gaussianValue(random, speed, 0.2, 0, 100000);
        clipSize     = MathUtils.gaussianValue(random, clipSize, 0.2, 1, 10000);
        clipReloadTime = MathUtils.gaussianValue(random, clipReloadTime, 0.2, 0, 10000);
//        return new Weapon(planet, enemyBulletGroup, coolDownTime, size, speed, size * 10, color, (int) clipSize, clipReloadTime, 100000, damage);
        return new Weapon(planet, enemyBulletGroup, coolDownTime, size, speed, color, (int) clipSize, clipReloadTime, 100000, damage);
    }

    public void spawnExplosion(Vec3 pos, Vec3 velocity, double area, double particleSize, double particleAmount, double particleHeat, double particleLifeTime, double speed) {
        double amount  = MathUtils.gaussianValue(random, particleAmount, 0.3, 1, 100);
        double heat = Math.min(1, Math.max(0, particleHeat));
        for (int i = 0; i < (int) amount; i++) {
            double radius = MathUtils.gaussianValue(random, particleSize, 0.4, 1, 1000);
            double lifeTime = MathUtils.gaussianValue(random, particleLifeTime, 1, 0.1, 1000);
            double mass = MathUtils.gaussianValue(random, 5, 0.3, 0.1, 100);
            double airDrag = MathUtils.gaussianValue(random, 10000, 1.0, 0.1, 100000);
            double damage = 0;
            Vec3 vel = MathUtils.createRandomVec(random, MathUtils.gaussianValue(random, speed, 0.8, 0, 3000));
            vel.setPlus(velocity);
            Vec3 p = MathUtils.createRandomVec(random, MathUtils.gaussianValue(random, area, 0.2, 0, 3000));
            p.setPlus(pos);
            Color color = MathUtils.createRandomColor(random,
                    0.01 + heat * 0.2, 0.01,
                    0.3 + heat * 0.8, 0.2,
                    0.4 + heat * 0.35, 0.1);
            effectsGroup.add(new Particle(getPlanet(), p, radius, vel, mass, color, airDrag, damage, lifeTime));
        }
    }

    public void addEnemy(EnemyShip enemyShip) {
        enemyGroup.add(enemyShip);
    }

    public void startLevel() {
        levelClock =0;
        getPlayer().resetAtLevelStart();
        createEnemies(getLevel());
        /*
        if (enemyCampaign != null) removeFacet(enemyCampaign); // Remove old campaign
        enemyCampaign = campaignFactory.createCampaign(getLevel());
        addFacet(enemyCampaign);
        */
    }

    public boolean isLevelCleared() {
        return enemyGroup.entities().isEmpty() && levelClock > 5;
        //return enemyCampaign.isOver();
    }

    public void update(double durationSec) {
        levelClock += durationSec;
    }
}