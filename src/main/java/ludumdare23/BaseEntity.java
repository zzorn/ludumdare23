package ludumdare23;

import net.zzorn.gameflow.entity.Entity;
import net.zzorn.gameflow.gamemap.GameMap;
import net.zzorn.gameflow.input.InputListenerAdapter;
import net.zzorn.utils.Vec3;

/**
 *
 */
public abstract class BaseEntity extends InputListenerAdapter implements Entity {

    private GameMap gameMap = null;
    protected Vec3 pos      = new Vec3(0,0,0);
    protected Vec3 velocity = new Vec3(0,0,0);
    protected Vec3 acc      = new Vec3(0,0,0);

    public GameMap getGameMap() {
        return gameMap;
    }

    @Override
    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public Vec3 pos() {
        return pos;
    }

    @Override
    public Vec3 velocity() {
        return velocity;
    }


}
