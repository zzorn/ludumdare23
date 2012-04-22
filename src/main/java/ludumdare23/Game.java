package ludumdare23;

import net.zzorn.gameflow.GameBase;
import net.zzorn.gameflow.camera.TrackingCamera;
import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.gamemap.GameMap;
import net.zzorn.gameflow.input.InputListenerAdapter;
import net.zzorn.gameflow.input.InputStatus;
import net.zzorn.gameflow.input.PrintingInputListener;
import net.zzorn.gameflow.picture.Picture;
import net.zzorn.utils.Vec3;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Ludumdare 23 game jam entry, main game class.
 */
public class Game extends GameBase {

    private GameMap gameMap=null;
    private Planet planet=new Planet();

    private Random random = new Random(42);


    public Game() {
        super("LD23", 300.0, 1000, 800, "");

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

        // Create players ship
        PlayerShip player = new PlayerShip(planet, this.pictureStore().get("images/playership.png"));
        inputHandler().addListener(player);

        // Create a camera that tracks the player
        final TrackingCamera camera = new TrackingCamera(player , 75, 2);
       camera.setCameraScale(0.4);


        // Set up the map
        gameMap=new GameMap(camera);
        gameMap.add(planet);
        gameMap.add(player);
        addFacet(gameMap);

        // Add enemies
        for (int i = 0; i < 5; i++) {
            // Planet shooters
            gameMap.add(createPlanetEnemy(planet, planet, "images/enemyship1.png", 1500, 2,
                    createWeapon(planet, 5, 15.0, 200.0, new Color(255, 131, 0), 1, 0)));
        }
        for (int i = 0; i < 5; i++) {
            // Player shooters
            gameMap.add(createPlanetEnemy(planet, player, "images/enemyship2.png", 400, 10,
                    createWeapon(planet, 0.1, 10.0, 500.0, new Color(255, 195, 0), 8, 2)));
        }


        // Print inputs for debugging purposes
        inputHandler().addListener(new PrintingInputListener());

        // Register page up and page down keys to change the camera zoom
        inputHandler().addListener(new InputListenerAdapter(){
            @Override
            public void onKeysUpdated(InputStatus inputStatus, double durationSeconds) {
                if (inputStatus.isKeyHeld(KeyEvent.VK_PAGE_UP))   camera.setCameraScale(camera.cameraScale() * 0.5);
                if (inputStatus.isKeyHeld(KeyEvent.VK_PAGE_DOWN)) camera.setCameraScale(camera.cameraScale() * 2.0);
            }
        });
    }


    private EnemyShip createPlanetEnemy(Planet planet, Entity target, String image, int distance, int maxBrakeAcc, Weapon weapon) {

        // Randomize enemy start position and speed
        Vec3 startPos = createRandomVec(10000);
        Vec3 startVelocity = createRandomVec(1000);

        // Create enemy ship
        double maxSpeed = randomValue(10, 1000);
        double enginePower = randomValue(10, 1000);
        double shootDistance = randomValue(200, 5000);
        Picture picture = pictureStore().get(image);
        EnemyShip enemyShip = new EnemyShip(planet, target, picture, startPos, startVelocity, enginePower, maxSpeed, distance, maxBrakeAcc, shootDistance);
        enemyShip.setWeapon(weapon);
        return enemyShip;
    }

    private Weapon createWeapon(Planet planet, double coolDownTime, double size, double speed, Color color, double clipSize, double clipReloadTime) {
        coolDownTime = gaussianValue(coolDownTime, 0.2, 0.01, 100);
        size         = gaussianValue(size,         0.2, 0.01, 100);
        speed        = gaussianValue(speed,        0.2, 0,   100000);
        clipSize     = gaussianValue(clipSize,     0.2, 1,   10000);
        clipReloadTime = gaussianValue(clipReloadTime, 0.2, 0,   10000);
        return new Weapon(planet, coolDownTime, size, speed, size * 10, color, (int) clipSize, clipReloadTime, 100000);
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