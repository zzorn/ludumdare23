package ludumdare23;

import net.zzorn.gameflow.GameBase;
import net.zzorn.gameflow.camera.TrackingCamera;
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
        final TrackingCamera camera = new TrackingCamera(player, 75, 2);
        camera.setCameraScale(0.4);

        // Set up the map
        gameMap=new GameMap(camera);
        gameMap.add(new FloatingParticle(planet, new Vec3(planet.getRadius_m()+50,0,0) , 20 ,new Vec3(90, 100,0 ),1,Color.BLUE));
        gameMap.add(new FloatingParticle(planet, new Vec3(planet.getRadius_m()+50,0,0) , 20 ,new Vec3(10, 0,0 ),2,Color.GRAY));
        gameMap.add(new FloatingParticle(planet, new Vec3(planet.getRadius_m()+550,0,0) , 20 ,new Vec3(-100, 20,0 ),100,Color.YELLOW));
        gameMap.add(planet);
        gameMap.add(player);
        addFacet(gameMap);

        // Add enemies
        for (int i = 0; i < 10; i++) {
            gameMap.add(createEnemy(planet, player));
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


    private EnemyShip createEnemy(Planet planet, PlayerShip player) {

        // Randomize enemy start position and speed
        Vec3 startPos = createRandomVec(10000);
        Vec3 startVelocity = createRandomVec(1000);

        // Create enemy ship
        double maxSpeed = randomValue(10, 1000);
        double enginePower = randomValue(10, 1000);
        Picture picture = pictureStore().get("images/enemyship1.png");
        return new EnemyShip(planet, player, picture, startPos, startVelocity, enginePower, maxSpeed);
    }

    private double randomValue(double start, double end) {
        return random.nextDouble() * (end - start) + start;
    }

    private Vec3 createRandomVec(double area) {
        Vec3 randomPos = Vec3.random();
        randomPos.setMul(area);
        randomPos.setZ(0);
        return randomPos;
    }
}