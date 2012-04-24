package ludumdare23.state;

import ludumdare23.Game;
import net.zzorn.gameflow.input.InputStatus;
import net.zzorn.gameflow.input.MouseButton;

/**
 *
 */
public class NextLevelState extends BaseGameState{
    private double timeUntilCloseable;

    public NextLevelState() {
        super("LevelCompleted", false);
    }

    @Override
    public void onEnter(GameStateManager manager, Game game) {
        getGame().getMessageScreen().showMessage("Planet Saved!"," ", 0, 2, 8);
        getGame().getPlayer().setControlsEnabled(false);
        timeUntilCloseable = 2;
    }

    @Override
    public void update(GameStateManager manager, Game game, double durationSeconds) {
        if (timeUntilCloseable > 0) {
            timeUntilCloseable -= durationSeconds;

            if (timeUntilCloseable <= 0) {
                getGame().getMessageScreen().showMessage("Planet Saved!","Press any key to continue to the next planet!", 0, 2, 8);
            }
        }
    }

    @Override
    public void onExit(GameStateManager manager, Game game) {
        getGame().getMessageScreen().clearMessage();

        // Start next level
        getGame().nextLevel();
        getGame().getPlayer().setControlsEnabled(true);

    }

    @Override
    public void onKeyPressed(int key, InputStatus inputStatus, double durationSeconds) {
        startNext();
    }

    @Override
    public void onMouseButtonPressed(MouseButton button, int x, int y, InputStatus inputStatus, double durationSeconds) {
        startNext();
    }

    private void startNext() {
        if (timeUntilCloseable < 0) {
            getStateManager().changeState("Playing");
        }
    }

}
