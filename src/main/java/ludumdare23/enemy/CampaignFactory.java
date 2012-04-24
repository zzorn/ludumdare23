package ludumdare23.enemy;

import ludumdare23.Game;
import ludumdare23.MathUtils;
import ludumdare23.Particle;
import ludumdare23.Weapon;
import ludumdare23.enemy.behavior.AttackBehavior;
import ludumdare23.enemy.behavior.AvoidBehavior;
import ludumdare23.enemy.behavior.FollowBehavior;
import ludumdare23.enemy.behavior.FollowRelativelyBehavior;
import net.zzorn.gameflow.EntityGroup;
import net.zzorn.utils.Vec3;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class CampaignFactory {

    private final Random random;
    private final Game game;

    private final List<String> shipImagesInIncreasingRarity = Arrays.asList(
            "images/enemyship3.png",
            "images/enemyship2.png",
            "images/enemyship1.png",
            "images/enemyship4.png",
            "images/carriership.png",
            "images/mothership1.png"
    );

    public CampaignFactory(Random random, Game game) {
        this.random = random;
        this.game = game;
    }

    public EnemyCampaign createCampaign(int level) {
        // Calculate some properties for the campaign
        double difficulty = (double)level / ((double)level + 10); // Near zero at level 1, 0.5 at level 10, increases towards 1.
        int numWaves = (int) MathUtils.mixWithVariation(random, difficulty, 0.2, 2, 10, 2, 20);
        double numShipsInWave = MathUtils.mixWithVariation(random, difficulty, 0.3, 10, 50,  1, 100);
        double boosterProbabilityInWave = 1.0 - Math.abs(difficulty - 0.5); // Maximum number of boosters around level 5, then less.

        return doCreateCampaign(difficulty, numWaves, numShipsInWave, boosterProbabilityInWave);
    }


    private EnemyCampaign doCreateCampaign(double difficulty, int numWaves, double numShipsInWave, double boosterProbabilityInWave) {
        EnemyCampaign campaign = new EnemyCampaign();
        for (int i = 0; i < numWaves; i++) {
            System.out.println("Making wave " + i);
            double wavePos = (double) i / (double) (numWaves-1);
            double waveDifficulty =  MathUtils.mixWithVariation(random, wavePos, 0.2, difficulty * 0.6, difficulty * 1.4, 0.05, 1);

            double nextWaveDestroyedPart = MathUtils.mixWithVariation(random, waveDifficulty, 0.2,  0.9, 0.7, 0.1, 0.99);
            double nextWaveAfterSeconds  = MathUtils.mixWithVariation(random, waveDifficulty, 0.2,  10*60, 60, 10, 15*60);
            EnemyWave wave = new EnemyWave(nextWaveDestroyedPart, nextWaveAfterSeconds);

            int numShipTypes = (int) MathUtils.mixWithVariation(random, waveDifficulty, 0.3, 2, 6, 1, 10);
            int numShips     = (int) MathUtils.mixWithVariation(random, waveDifficulty, 0.3, numShipsInWave * 0.5, numShipsInWave * 2, 4, 100);

            for (int shipType = 0; shipType < numShipTypes; shipType++) {
                System.out.println("Making ship type " + shipType);
                double shipTypePos = (double) shipType / ((double) numShipTypes - 1);

                // Take some ships out of total number for wave
                int numShipsOfType;
                if (shipType == numShipTypes - 1) {
                    numShipsOfType = numShips;
                }
                else {
                    numShipsOfType = (int) (numShips * MathUtils.mixWithVariation(random, waveDifficulty, 0.1, 0.8, 0.2, 0.1, 0.9));
                    numShips -= numShipsOfType;
                }

                System.out.println("numShipsOfType = " + numShipsOfType);

                wave.addSpawner(createShipSpawner(numShipsOfType, waveDifficulty, shipTypePos, boosterProbabilityInWave));
            }

            campaign.addWave(wave);
        }
        return campaign;
    }

    private EnemySpawner createShipSpawner(int numShipsOfType, double waveDifficulty, double shipTypePos, double boosterProbabilityInWave) {

        EnemyFactory factory = createShipFactory(shipTypePos, waveDifficulty, boosterProbabilityInWave);

        double spawnDistance = MathUtils.mixWithVariation(random, waveDifficulty, 0.2, 1000, 5000, 1500, 5000);
        double spawnPosVariation = MathUtils.mixWithVariation(random, waveDifficulty, 0.2, 0, 2000, 0, 1000);
        Vec3 pos = MathUtils.createRandomVec(random, spawnDistance);
        double spawnSpeed = MathUtils.mixWithVariation(random, waveDifficulty, 0.2, 10, 2000, 0.1, 500);
        double spawnSpeedVariation = MathUtils.mixWithVariation(random, waveDifficulty, 0.2, 0, 1000, 0, 100);
        Vec3 vel = MathUtils.createRandomVec(random, spawnSpeed);

        double interval = MathUtils.mixWithVariation(random, waveDifficulty, 0.2, 10, 0, 0, 60);
        double intervalVariation = MathUtils.mixWithVariation(random, waveDifficulty, 0.2, 0, 1, 0, 1);

        EnemySpawner enemySpawner = new EnemySpawner(
                game,
                random,
                factory,
                pos, spawnPosVariation,
                vel, spawnSpeedVariation,
                numShipsOfType, 0.1,
                interval, intervalVariation);

        return enemySpawner;
    }

    private EnemyFactory createShipFactory(final double shipRarity, final double difficulty, final double boosterProbability) {
        return new EnemyFactory() {
            @Override
            public EnemyShip createShip(Random random, final EnemyShip leader) {
                // Detemine picture
                int numPics = shipImagesInIncreasingRarity.size();
                int shipPic = (int) (numPics * MathUtils.mixWithVariation(random, shipRarity, 0.2, 0, difficulty, 0, 0.99));
                if (shipPic >= numPics) shipPic = numPics - 1;
                String picName = shipImagesInIncreasingRarity.get(shipPic);

                double maxThrust = 1000 * MathUtils.mixWithVariation(random, shipRarity, 0.4, difficulty*5, difficulty*0.8, 0.5, 10);
                double maxSpeed  = 100 * MathUtils.mixWithVariation(random, shipRarity, 0.4, difficulty*5, difficulty*0.2, 0.5, 10);

                int numWeapons = (int) MathUtils.mixWithVariation(random, shipRarity, 0.2, difficulty, difficulty*10, 1, 4);

                EnemyShip ship = new EnemyShip(game, game.getPlanet(), game.pictureStore().get(picName, 2), maxThrust, maxSpeed, numWeapons, 500, 100, game.getPlayer());

                // Weapons
                for (int weaponNum = 0; weaponNum < numWeapons; weaponNum++) {
                    double weaponPos = MathUtils.map(weaponNum, 0, numWeapons - 1, 0, 1);
                    ship.addWeapon(createWeapon(game, game.getEnemyBulletGroup(), random, weaponPos, shipRarity + difficulty, boosterProbability));
                }

                // Behaviors
                createBehaviors(ship, leader, shipRarity, difficulty);

                return ship;
            }
        };
    }

    private void createBehaviors(EnemyShip ship, EnemyShip leader, double shipRarity, double difficulty) {
        // Make sure the ships find the battlefield
        ship.addBehavior(new FollowBehavior(game.getPlanet(), 0, 1000000000, 0.1));

        double avoidPlayerDistance  = MathUtils.mixWithVariation(random, shipRarity - difficulty, 0.3, 10, 100, 10, 200);
        double attackPlayerDistance = MathUtils.mixWithVariation(random, shipRarity + difficulty, 0.3, 200, 1000, 1, 1000);
        double followPlayerDistance = MathUtils.mixWithVariation(random, shipRarity + difficulty, 0.3, 200, 5000, 1, 10000);

        double avoidPlayer = MathUtils.mixWithVariation(random, difficulty, 0.2, 1-shipRarity, shipRarity*difficulty, 0, 1);
        if (avoidPlayer > 0.5) ship.addBehavior(new AvoidBehavior(game.getPlayer(), 0, avoidPlayerDistance, avoidPlayer));

        double followPlayer = MathUtils.mixWithVariation(random, shipRarity*difficulty, 0.2, 1, 0, 0, 1);
        if (followPlayer > 0.5) ship.addBehavior(new FollowBehavior(game.getPlayer(), avoidPlayerDistance, followPlayerDistance, followPlayer));

        double dodgePlayer = MathUtils.mixWithVariation(random, difficulty+ (1-shipRarity), 0.2, 0.2, 1, 0, 1);
        Vec3 relPos = MathUtils.createRandomVec(random, 0.5*(avoidPlayerDistance + followPlayerDistance));
        if (dodgePlayer > 0.5) ship.addBehavior(new FollowRelativelyBehavior(game.getPlayer(), avoidPlayerDistance, followPlayerDistance, followPlayer, relPos));

        double followLeader = MathUtils.mixWithVariation(random, shipRarity, 0.2, 0.7, 0.5 - difficulty, 0, 1);
        if (leader != null && followLeader > 0.5) ship.addBehavior(new FollowBehavior(leader, 100, 1000, followLeader));

        double attackPlayer = MathUtils.mixWithVariation(random, shipRarity*difficulty, 0.2, 1, 0, 0, 1);
        if (attackPlayer > 0.2) ship.addBehavior(new AttackBehavior(game.getPlayer(), avoidPlayerDistance, attackPlayerDistance));


        double avoidPlanetDistance = MathUtils.mixWithVariation(random, difficulty - shipRarity, 0.3, 1000, 400, 10, 500);
        double attackPlanetDistance = MathUtils.mixWithVariation(random, difficulty*shipRarity, 0.3, 1000, 10000, 10, 1000);
        double followPlanetDistance = MathUtils.mixWithVariation(random, difficulty*shipRarity, 0.3, 100000, 10000, 10, 1000000);

        double followPlanet = MathUtils.mixWithVariation(random, shipRarity, 0.2, 0, 1, 0, 1);
        if (followPlanet > 0.2) ship.addBehavior(new FollowBehavior(game.getPlanet(), avoidPlanetDistance, followPlanetDistance, followPlanet));

        double avoidPlanet = MathUtils.mixWithVariation(random, shipRarity*difficulty, 0.2, 1, 0.2, 0, 1);
        if (avoidPlanet> 0.5) ship.addBehavior(new AvoidBehavior(game.getPlanet(), 0, avoidPlanetDistance, avoidPlanet));

        double attackPlanet = MathUtils.mixWithVariation(random, shipRarity+difficulty, 0.2, 0, 1, 0, 1);
        if (attackPlanet> 0.2) ship.addBehavior(new AttackBehavior(game.getPlanet(), avoidPlanetDistance, attackPlanetDistance));

    }


    public static Weapon createWeapon(Game game, EntityGroup<Particle> bulletGroup, Random random, double weaponPos, double power, double boosterProbability) {
        double heaviness = MathUtils.mixWithVariation(random, weaponPos, 0.2, power * 0.1, power,        0.01, 1);
        double rapidness = MathUtils.mixWithVariation(random, weaponPos, 0.2, power * 2,   power * 0.01, 0.01, 1);

        double cooldownTime = MathUtils.mixWithVariation(random, rapidness, 0.2, 1, 0.01, 0.1, 1);
        double shotSize     = MathUtils.mixWithVariation(random, heaviness, 0.2, 1, 50,    10, 100);
        double shotSpeed    = MathUtils.mixWithVariation(random, rapidness, 0.2, 200, 1000,   100, 1000);
        Color shotColor     = MathUtils.createRandomColor(random,
                power * 0.5, 0.2,
                0.5 + power * 0.5, 0.2,
                0.5 + power * 0.5, 0.2);

        int clipSize = (int) MathUtils.mixWithVariation(random, power, 0.3, 10, 100, 1, 100);
        double clipReloadTime = MathUtils.mixWithVariation(random, power, 0.2, 4, 1*rapidness, 0, 5);
        double airResistance = MathUtils.mixWithVariation(random, rapidness+power, 0.2, 1000, 100, 1000, 10000);

        double damage = MathUtils.mixWithVariation(random, heaviness, 0.2, 10, 100, 5, 200);

        return new Weapon(game.getPlanet(), bulletGroup, cooldownTime, shotSize, shotSpeed, shotColor, clipSize, clipReloadTime, airResistance, damage);
    }


}
