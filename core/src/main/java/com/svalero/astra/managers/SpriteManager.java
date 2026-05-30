package com.svalero.astra.managers;

import com.badlogic.gdx.utils.Array;
import com.svalero.astra.characters.Bullet;
import com.svalero.astra.characters.Enemy;
import com.svalero.astra.characters.Player;
import com.svalero.astra.characters.PowerUp;
import com.svalero.astra.util.Constants;

public class SpriteManager {

    private static SpriteManager instance;

    public Player player;
    private LevelManager levelManager;

    private SpriteManager() {
        levelManager = LevelManager.getInstance();
    }

    public static SpriteManager getInstance() {
        if (instance == null) {
            instance = new SpriteManager();
        }
        return instance;
    }

    public void init(Player player) {
        this.player = player;
    }

    public void update(float dt) {
        // Actualizar jugador
        player.update(dt);

        // Actualizar nivel (enemigos, powerups)
        levelManager.update(dt);

        // Comprobar colisiones
        checkCollisions();
    }

    private void checkCollisions() {
        Array<Enemy> enemies   = levelManager.enemies;
        Array<PowerUp> powerUps = levelManager.powerUps;

        // Balas del jugador vs enemigos
        for (int i = player.bullets.size - 1; i >= 0; i--) {
            Bullet bullet = player.bullets.get(i);
            for (int j = enemies.size - 1; j >= 0; j--) {
                Enemy enemy = enemies.get(j);
                if (bullet.rect.overlaps(enemy.rect)) {
                    enemy.takeDamage();
                    if (enemy.isDead()) {
                        player.addScore(enemy.points);
                    }
                    player.bullets.removeIndex(i);
                    break;
                }
            }
        }

        // Balas enemigas vs jugador
        for (Enemy enemy : enemies) {
            for (int i = enemy.bullets.size - 1; i >= 0; i--) {
                Bullet bullet = enemy.bullets.get(i);
                if (bullet.rect.overlaps(player.rect)) {
                    if (!player.shieldActive) {
                        player.takeDamage();
                    }
                    enemy.bullets.removeIndex(i);
                }
            }
        }

        // Enemigos vs jugador (colisión directa)
        for (Enemy enemy : enemies) {
            if (enemy.rect.overlaps(player.rect)) {
                if (!player.shieldActive) {
                    player.takeDamage();
                }
                enemy.takeDamage();
            }
        }

        // Powerups vs jugador
        for (PowerUp powerUp : powerUps) {
            if (!powerUp.isCollected() && powerUp.rect.overlaps(player.rect)) {
                powerUp.applyTo(player);
                powerUp.collect();
            }
        }
    }

    public void reset() {
        player.lives  = Constants.PLAYER_LIVES;
        player.score  = 0;
        player.bullets.clear();
        player.shieldActive = false;
        player.speedActive  = false;
        player.doubleShot   = false;
    }
}
