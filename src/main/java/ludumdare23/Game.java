package ludumdare23;

import net.zzorn.gameflow.GameBase;
import net.zzorn.gameflow.camera.TrackingCamera;
import net.zzorn.gameflow.gamemap.GameMap;
import net.zzorn.gameflow.input.PrintingInputListener;
import net.zzorn.utils.Vec3;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Shiera
 * Date: 21.4.2012
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */
public class Game extends GameBase {

    private GameMap gameMap=null;
    private Planet planet=new Planet();

    public Game() {
        super("planeettapeli", 300.0, 1200, 1000, "");

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
        PlayerShip player = new PlayerShip(planet, this.pictureStore().get("images/playership.png"));
        gameMap=new GameMap(new TrackingCamera(player,10, 10));
        addFacet(gameMap);
        gameMap.add(new FloatingParticle(planet, new Vec3(planet.getRadius_m()+50,0,0) , 20 ,new Vec3(90, 100,0 ),1,Color.BLUE));
        gameMap.add(new FloatingParticle(planet, new Vec3(planet.getRadius_m()+50,0,0) , 20 ,new Vec3(10, 0,0 ),2,Color.GRAY));
        gameMap.add(new FloatingParticle(planet, new Vec3(planet.getRadius_m()+550,0,0) , 20 ,new Vec3(-100, 20,0 ),100,Color.YELLOW));
        gameMap.add(planet);


        inputHandler().addListener(player);
        gameMap.add(player);

        // Show inputs
        inputHandler().addListener(new PrintingInputListener());
    }

    /**
     * Called once in each mainloop, immediately before render.
     *
     * @param durationSec the duration of the previous frame, in seconds.
     */
    @Override
    public void update(double durationSec) {
    }

    /**
     * Render the game contents to the specified screen raster.
     *
     * @param screen the graphics to render to.
     */
    @Override
    public void render(Graphics2D screen, int screenW, int screenH) {
    }
}