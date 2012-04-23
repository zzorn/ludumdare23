package ludumdare23.state;

import ludumdare23.Game;
import net.zzorn.gameflow.input.InputStatus;
import net.zzorn.gameflow.input.MouseButton;

/**
 *
 */
public class BaseGameState implements GameState {
    private final String name;
    private final boolean allowReEntry;
    private Game game;
    private GameStateManager stateManager;

    public BaseGameState(String name, boolean allowReEntry) {
        this.name = name;
        this.allowReEntry = allowReEntry;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public boolean isAllowReEntry() {
        return allowReEntry;
    }

    public GameStateManager getStateManager() {
        return stateManager;
    }

    @Override
    public void setStateManager(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public void onEnter(GameStateManager manager, Game game) {}

    @Override
    public void update(GameStateManager manager, Game game, double durationSeconds) {}

    @Override
    public void onExit(GameStateManager manager, Game game) {}


    public void onKeyPressed(int key, InputStatus inputStatus, double durationSeconds) {
    }

    public void onKeyReleased(int key, InputStatus inputStatus, double durationSeconds) {
    }

    public void onMouseMoved(int x, int y, InputStatus inputStatus, double durationSeconds) {
    }

    public void onMouseButtonPressed(MouseButton button, int x, int y, InputStatus inputStatus, double durationSeconds) {
    }

    public void onMouseButtonReleased(MouseButton button, int x, int y, InputStatus inputStatus, double durationSeconds) {
    }

    public void onKeysUpdated(InputStatus inputStatus, double durationSeconds) {
    }

    public void onMouseWheelMoved(int mouseWheelMovement, int x, int y, InputStatus inputStatus, double durationSeconds) {
    }
}
