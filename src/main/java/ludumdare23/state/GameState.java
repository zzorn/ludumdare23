package ludumdare23.state;

import ludumdare23.Game;
import net.zzorn.gameflow.input.InputListener;

/**
 * Represents some state of the game.
 */
public interface GameState extends InputListener {

    String getName();
    
    void onEnter(GameStateManager manager, Game game);

    void update(GameStateManager manager, Game game, double durationSeconds);

    void onExit(GameStateManager manager, Game game);

    void setGame(Game game);

    void setStateManager(GameStateManager stateManager);

    boolean isAllowReEntry();
}
