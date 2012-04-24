package ludumdare23.enemy;

import net.zzorn.gameflow.BaseFacet;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class EnemyCampaign extends BaseFacet {

    private List<EnemyWave> unstartedWaves = new ArrayList<EnemyWave>();
    private List<EnemyWave> activeWaves = new ArrayList<EnemyWave>();
    private boolean over = false;

    public void addWave(EnemyWave wave) {
        unstartedWaves.add(wave);
    }

    public void update(double seconds) {
        boolean allReadyForNextWave = true;
        if (activeWaves.isEmpty()) {
            activateNextWave();
        }

        for (EnemyWave activeWave : activeWaves) {
            activeWave.update(seconds);
            if (!activeWave.isReadyForNextWave()) allReadyForNextWave = false;
        }

        if (allReadyForNextWave) activateNextWave();
    }

    private void activateNextWave() {
        if (!unstartedWaves.isEmpty()) {
            activeWaves.add(unstartedWaves.remove(0));
        }
    }

    public boolean isOver() {
        if (!unstartedWaves.isEmpty()) return false;

        for (EnemyWave activeWave : activeWaves) {
            if (!activeWave.isCleared()) return false;
        }

        return true;
    }
}
