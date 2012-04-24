package ludumdare23.enemy;

import ludumdare23.Game;

import java.util.Random;

/**
 *
 */
public interface EnemyFactory {

    EnemyShip createShip(Random random, EnemyShip leader);


}
