package ludumdare23.state;

import ludumdare23.Game;
import net.zzorn.gameflow.input.InputStatus;
import net.zzorn.gameflow.input.MouseButton;

/**
 *
 */
public class GameOverState extends BaseGameState {

    private double timeUntilCloseable;

    public GameOverState() {
        super("GameOver", false);
    }

    @Override
    public void onEnter(GameStateManager manager, Game game) {
        getGame().getMessageScreen().showMessage("Game Over!"," ", 0, 2, 8);
        getGame().getPlayer().setControlsEnabled(false);
        timeUntilCloseable = 4;
    }

    @Override
    public void update(GameStateManager manager, Game game, double durationSeconds) {
        if (timeUntilCloseable > 0) {
            timeUntilCloseable -= durationSeconds;

            if (timeUntilCloseable <= 0) {
                getGame().getMessageScreen().showMessage("Game Over!","Press any key to retry level...", 0, 2, 8);
            }
        }
    }

    @Override
    public void onExit(GameStateManager manager, Game game) {
        // Restart level
        getGame().restartLevel();
        getGame().getMessageScreen().showMessage("Good Luck!","", 2, 2, 4);
        getGame().getPlayer().setControlsEnabled(true);
    }

    @Override
    public void onKeyPressed(int key, InputStatus inputStatus, double durationSeconds) {
        retry();
    }

    @Override
    public void onMouseButtonPressed(MouseButton button, int x, int y, InputStatus inputStatus, double durationSeconds) {
        retry();
    }

    private void retry() {
        if (timeUntilCloseable < 0) {
            getStateManager().changeState("Playing");
        }
    }
}