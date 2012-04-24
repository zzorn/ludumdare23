package ludumdare23.enemy;

import ludumdare23.Game;
import ludumdare23.MathUtils;
import net.zzorn.utils.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class EnemySpawner {
    private final Game game;
    private final Random random;
    private final EnemyFactory factory;
    private final Vec3 spawnPos;
    private final Vec3 spawnVelocity;
    private final double spawnInterval;
    private final double spawnIntervalVariation;
    private final double spawnVelocityVariation;
    private final double spawnPosVariation;
    private final int totalNumber;
    private int numberLeft;
    private EnemyShip leader;
    private double timeToNextSpawn = 0;

    private List<EnemyShip> spawnedShips = new ArrayList<EnemyShip>();

    public EnemySpawner(Game game,
                        Random random,
                        EnemyFactory factory,
                        Vec3 spawnPos,
                        double spawnPosVariation,
                        Vec3 spawnVelocity,
                        double spawnVelocityVariation,
                        int averageNumber,
                        double numberVariation,
                        double spawnInterval,
                        double spawnIntervalVariation) {
        this.game = game;
        this.random = random;
        this.factory = factory;
        this.spawnPos = spawnPos;
        this.spawnVelocity = spawnVelocity;
        this.spawnInterval = spawnInterval;
        this.spawnIntervalVariation = spawnIntervalVariation;
        this.spawnVelocityVariation = spawnVelocityVariation;
        this.spawnPosVariation = spawnPosVariation;
        this.totalNumber = (int) MathUtils.gaussianValue(random, (double) averageNumber, numberVariation, 1, 1000);
        numberLeft = totalNumber;
    }

    public int getNumberLeft() {
        return numberLeft;
    }

    public int getShipsAlive() {
        if (totalNumber <= 0) return 0;
        else {
            int alive = numberLeft; // The unborn children
            for (EnemyShip spawnedShip : spawnedShips) {
                if (!spawnedShip.isDestroyed()) alive++;
            }
            return alive;
        }
    }

    public double getDestroyedPart() {
        if (totalNumber <= 0) return 1.0;
        else {
            double alive = numberLeft; // The unborn children
            double dead = 0;
            for (EnemyShip spawnedShip : spawnedShips) {
                if (spawnedShip.isDestroyed()) dead++;
                else alive++;
            }
            return dead / (alive + dead);
        }
    }

    public void update(double seconds) {
        if (numberLeft > 0) {
            timeToNextSpawn -= seconds;
            if (timeToNextSpawn <= 0) {
                timeToNextSpawn = MathUtils.gaussianValue(random, spawnInterval, spawnIntervalVariation, 0, 600);
                numberLeft -= 1;
                spawn();
            }
        }
    }

    private void spawn() {
        System.out.println("EnemySpawner.spawn");

        // Create ship
        EnemyShip ship = factory.createShip(random, leader);

        // Position
        Vec3 p = MathUtils.createRandomVec(random, spawnPosVariation);
        p.setPlus(spawnPos);
        ship.pos().set(p);

        // Velocity
        Vec3 v = MathUtils.createRandomVec(random, spawnVelocityVariation);
        v.setPlus(spawnVelocity);
        ship.velocity().set(v);

        System.out.println("p = " + p);
        System.out.println("v = " + v);

        // Add to game
        game.addEnemy(ship);

        // Keep track of them
        spawnedShips.add(ship);
        leader = ship;
    }

    public int getTotalNumber() {
        return totalNumber;
    }
}
