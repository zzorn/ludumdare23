package ludumdare23.state;

import ludumdare23.Game;
import net.zzorn.gameflow.BaseFacet;
import net.zzorn.gameflow.input.InputListener;
import net.zzorn.gameflow.input.InputStatus;
import net.zzorn.gameflow.input.MouseButton;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class GameStateManager extends BaseFacet implements InputListener {

    private Map<String, GameState> states = new LinkedHashMap<String, GameState>();
    private final Game game;
    private String nextStateName = null;
    private GameState activeState = null;

    public GameStateManager(Game game) {
        this.game = game;
    }

    public void addState(GameState state) {
        states.put(state.getName(), state);
        state.setGame(game);
        state.setStateManager(this);
    }

    public void changeState(String newStateName) {
        nextStateName = newStateName;
    }
    
    @Override
    public void update(double durationSeconds) {
        if (nextStateName != null) {
            // Get new state
            GameState newState = states.get(nextStateName);
            if (newState  == null && !nextStateName.isEmpty()) throw new Error("Unknown next game state name '"+nextStateName+"'");

            if (activeState != newState || (activeState != null && activeState.isAllowReEntry())) {
                // Exit old state
                if (activeState != null) activeState.onExit(this, game);

                // Set new state
                activeState = newState;
                nextStateName = null;

                // Enter new state
                if (activeState != null) activeState.onEnter(this, game);
            }

        }

        // Update current state
        if (activeState != null) activeState.update(this, game, durationSeconds);
    }

    public void onKeyPressed(int key, InputStatus inputStatus, double durationSeconds) {
        if (activeState != null) activeState.onKeyPressed(key, inputStatus, durationSeconds);
    }

    public void onKeyReleased(int key, InputStatus inputStatus, double durationSeconds) {
        if (activeState != null) activeState.onKeyReleased(key, inputStatus, durationSeconds);
    }

    public void onMouseMoved(int x, int y, InputStatus inputStatus, double durationSeconds) {
        if (activeState != null) activeState.onMouseMoved(x, y, inputStatus, durationSeconds);
    }

    public void onMouseButtonPressed(MouseButton button, int x, int y, InputStatus inputStatus, double durationSeconds) {
        if (activeState != null) activeState.onMouseButtonPressed(button, x, y, inputStatus, durationSeconds);
    }

    public void onMouseButtonReleased(MouseButton button, int x, int y, InputStatus inputStatus, double durationSeconds) {
        if (activeState != null) activeState.onMouseButtonReleased(button, x, y, inputStatus, durationSeconds);
    }

    public void onKeysUpdated(InputStatus inputStatus, double durationSeconds) {
        if (activeState != null) activeState.onKeysUpdated(inputStatus, durationSeconds);
    }

    public void onMouseWheelMoved(int wheelMovement, int x, int y, InputStatus inputStatus, double durationSeconds) {
        if (activeState != null) activeState.onMouseWheelMoved(wheelMovement, x, y, inputStatus, durationSeconds);
    }
}
