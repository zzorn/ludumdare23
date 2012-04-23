package ludumdare23.state;

import ludumdare23.Game;
import net.zzorn.gameflow.input.InputStatus;

/**
 *
 */
public class StartScreenState extends BaseGameState {

    private static final double CAMERA_START_DIST = 1000;
    private static final double CAMERA_MOVE_TIME = 5;

    private double totalTime = 0;
    
    private double camYTarget = 0;
    private double camYStart = 0;

    public StartScreenState() {
        super("StartScreen");
    }

    @Override
    public void onEnter(GameStateManager manager, Game game) {
        game.getMessageScreen().showMessage(
                "Save The Planets!",
                "In the interest of improving interstellar TV reception,\n" +
                "the Galactic Ministry of Edutainment and Propaganda\n" +
                "has decided to clear out some obscure woodland worlds.\n" +
                "\n" +
                "As the Planetary Preservation Society, you can not allow\n" +
                "this callous destruction of fascinating ecosystems and\n" +
                "diversiful primitive cultures.\n" +
                "\n" +
                "It is time to stand up and defend the planets!\n" +
                "\n" +
                "  Controls:\n" +
                "    X, C:       Drive (counter) clockwise around planet, or\n"+
                "    W, A, S, D: Drive in direction around planet.\n" +
                "    Aim with mouse, fire with SPACE or left mouse.\n" +
                "\n" +
                "    You can also zoom with PGUP, PGDOWN, Mouse wheel.\n" +
                "    ESC: exit.\n" +
                "\n" +
                "Press any key to start...",
                0, 1.5, 4.0);

        game.loadLevel(1);

        camYTarget = -getGame().getPlanet().getRadius() - Game.PLAYER_FLIGHT_HEIGHT;
        camYStart = camYTarget - CAMERA_START_DIST;
        game.getTrackingCamera().cameraPos().set(0, camYStart, 0);
    }

    @Override
    public void update(GameStateManager manager, Game game, double durationSeconds) {
        totalTime += durationSeconds;

        double relTime = Math.min(1.0, totalTime / CAMERA_MOVE_TIME);
        double relPos = 0.5 - 0.5 * Math.cos(relTime * Math.PI);
        double camY = camYStart * (1.0 - relPos) + camYTarget * relPos;
        game.getTrackingCamera().cameraPos().set(0, camY, 0);
    }

    @Override
    public void onExit(GameStateManager manager, Game game) {
        game.getMessageScreen().clearMessage();
        game.createPlayer();
    }

    @Override
    public void onKeyPressed(int key, InputStatus inputStatus, double durationSeconds) {
        getStateManager().changeState("Playing");
    }
}
