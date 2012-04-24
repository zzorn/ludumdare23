package ludumdare23.enemy;

import ludumdare23.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class EnemyWave {

    private final List<EnemySpawner> spawners = new ArrayList<EnemySpawner>();
    private final double nextWaveAtDestroyedPart;
    private final double nextWaveAfterSeconds;
    private double totalTime = 0;

    public EnemyWave(double nextWaveAtDestroyedPart, double nextWaveAfterSeconds) {
        this.nextWaveAtDestroyedPart = MathUtils.clamp(nextWaveAtDestroyedPart, 0, 0.99);
        this.nextWaveAfterSeconds = nextWaveAfterSeconds;
    }

    public void addSpawner(EnemySpawner spawner) {
        spawners.add(spawner);
    }

    public int getNumberLeft() {
        int alive = 0;
        for (EnemySpawner spawner : spawners) {
            alive += spawner.getShipsAlive();
        }
        return alive;
    }

    public int getTotalNumber() {
        int total = 0;
        for (EnemySpawner spawner : spawners) {
            total += spawner.getTotalNumber();
        }
        return total;
    }

    public boolean isCleared() {
        return getNumberLeft() <= 0;
    }

    /**
     * @return how much of the wave that has been destroyed, 0 = none, 1 = all.
     */
    public double getDestroyedPart() {
        int total = getTotalNumber();
        if (total <= 0) return 1.0;
        else return getNumberLeft() / total;
    }

    public boolean isReadyForNextWave() {
        return totalTime > nextWaveAfterSeconds || getDestroyedPart() > nextWaveAtDestroyedPart;
    }


    public void update(double seconds) {
        totalTime += seconds;
        for (EnemySpawner spawner : spawners) {
            spawner.update(seconds);
        }
    }
}
