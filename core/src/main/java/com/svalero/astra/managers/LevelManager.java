package com.svalero.astra.managers;

import com.badlogic.gdx.utils.Array;
import com.svalero.astra.characters.Enemy;
import com.svalero.astra.characters.EnemyBasic;
import com.svalero.astra.characters.EnemyBoss;
import com.svalero.astra.characters.EnemyZigzag;
import com.svalero.astra.characters.PowerUp;
import com.svalero.astra.util.Constants;
import com.svalero.astra.util.Util;

public class LevelManager {

    private static LevelManager instance;

    public int currentLevel;
    public boolean levelComplete;
    public boolean gameComplete;

    // Oleadas
    private float spawnTimer;
    private float spawnDelay;
    private int enemiesSpawned;
    private int enemiesPerWave;
    private int currentWave;
    private int totalWaves;
    private boolean bossSpawned;
    private boolean bossDefeated;

    // Listas de entidades activas
    public Array<Enemy> enemies;
    public Array<PowerUp> powerUps;

    private LevelManager() {
        enemies   = new Array<>();
        powerUps  = new Array<>();
        currentLevel = 1;
    }

    public static LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    public void loadLevel(int level) {
        currentLevel  = level;
        levelComplete = false;
        bossSpawned   = false;
        bossDefeated  = false;
        currentWave   = 0;
        enemiesSpawned = 0;
        enemies.clear();
        powerUps.clear();

        switch (level) {
            case 1:
                totalWaves     = 3;
                enemiesPerWave = 5;
                spawnDelay     = 2f;
                break;
            case 2:
                totalWaves     = 4;
                enemiesPerWave = 7;
                spawnDelay     = 1.5f;
                break;
            default:
                totalWaves     = 3;
                enemiesPerWave = 5;
                spawnDelay     = 2f;
                break;
        }
    }

    public void update(float dt) {
        spawnTimer += dt;

        // Spawnear enemigos
        if (!bossSpawned && spawnTimer >= spawnDelay) {
            spawnTimer = 0f;
            spawnEnemy();
        }

        // Actualizar enemigos
        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            enemy.update(dt);

            // Eliminar si ha salido por la izquierda o está muerto
            if (enemy.isDead()) {
                // Probabilidad de soltar powerup al morir
                maybeSpawnPowerUp(enemy);
                enemies.removeIndex(i);
            } else if (enemy.isOffScreenLeft()) {
                enemies.removeIndex(i);
            }
        }

        // Actualizar powerups
        for (int i = powerUps.size - 1; i >= 0; i--) {
            PowerUp p = powerUps.get(i);
            p.update(dt);
            if (p.isCollected() || p.isOffScreen()) {
                powerUps.removeIndex(i);
            }
        }

        // Comprobar si el nivel ha terminado
        checkLevelComplete();
    }

    private void spawnEnemy() {
        if (currentWave >= totalWaves) {
            // Todas las oleadas completadas — spawnear boss
            if (!bossSpawned && enemies.isEmpty()) {
                spawnBoss();
            }
            return;
        }

        if (enemiesSpawned < enemiesPerWave) {
            float x = Util.spawnX(100f);
            float y = Util.randomY(84f);

            Enemy enemy;
            // Nivel 1: básicos y zigzag. Nivel 2: más zigzag
            if (currentLevel == 1) {
                enemy = (enemiesSpawned % 3 == 0)
                    ? new EnemyZigzag(x, y)
                    : new EnemyBasic(x, y);
            } else {
                enemy = (enemiesSpawned % 2 == 0)
                    ? new EnemyZigzag(x, y)
                    : new EnemyBasic(x, y);
            }

            enemies.add(enemy);
            enemiesSpawned++;
        } else {
            // Oleada completada
            currentWave++;
            enemiesSpawned = 0;
        }
    }

    private void spawnBoss() {
        bossSpawned = true;
        float x = Constants.SCREEN_WIDTH + 50f;
        float y = Constants.SCREEN_HEIGHT / 2f - 97f;
        enemies.add(new EnemyBoss(x, y));
    }

    private void maybeSpawnPowerUp(Enemy enemy) {
        // 20% de probabilidad de soltar powerup
        if (Math.random() < 0.2f) {
            PowerUp.Type[] types = PowerUp.Type.values();
            PowerUp.Type type = types[(int)(Math.random() * types.length)];
            powerUps.add(new PowerUp(
                enemy.position.x,
                enemy.position.y,
                type
            ));
        }
    }

    private void checkLevelComplete() {
        if (bossSpawned && enemies.isEmpty()) {
            if (currentLevel >= Constants.TOTAL_LEVELS) {
                gameComplete = true;
            } else {
                levelComplete = true;
            }
        }
    }

    public void nextLevel() {
        loadLevel(currentLevel + 1);
    }

    public void reset() {
        currentLevel = 1;
        loadLevel(1);
    }
}
