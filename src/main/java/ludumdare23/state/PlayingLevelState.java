package ludumdare23.state;

import ludumdare23.Game;
import net.zzorn.gameflow.input.InputStatus;

/**
 *
 */
public class PlayingLevelState extends BaseGameState {

    public PlayingLevelState() {
        super("Playing", false);
    }

    @Override
    public void onEnter(GameStateManager manager, Game game) {
        game.startLevel();
    }

    @Override
    public void onExit(GameStateManager manager, Game game) {
    }

    @Override
    public void update(GameStateManager manager, Game game, double durationSeconds) {
        // Check if all waves done
        if (getGame().isLevelCleared()) manager.changeState("LevelCompleted");
    }

    @Override
    public void onKeyPressed(int key, InputStatus inputStatus, double durationSeconds) {
    }
}
