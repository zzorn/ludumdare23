package ludumdare23;

import net.zzorn.gameflow.GameBase;
import net.zzorn.gameflow.gamemap.GameMap;
import net.zzorn.gameflow.gamemap.camera.StationaryCamera;
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
        super("planeettapeli", 300, 800, 600, "");

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
        gameMap=new GameMap(new StationaryCamera(new Vec3(-this.canvas().getWidth()/2,-this.canvas().getHeight()/2,0)));
        gameMap.add(new FloatingParticle(planet));
        gameMap.add(planet);
    }

    /**
     * Called once in each mainloop, immediately before render.
     *
     * @param durationSec the duration of the previous frame, in seconds.
     */
    @Override
    public void update(double durationSec) {
        gameMap.update(durationSec);
    }

    /**
     * Render the game contents to the specified screen raster.
     *
     * @param screen the graphics to render to.
     */
    @Override
    public void draw(Graphics2D screen, int screenW, int screenH) {
        gameMap.draw(screen, screenW, screenH);
    }
}