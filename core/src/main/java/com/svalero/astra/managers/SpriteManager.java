package com.svalero.astra.managers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.svalero.astra.characters.Bullet;
import com.svalero.astra.characters.Enemy;
import com.svalero.astra.characters.Explosion;
import com.svalero.astra.characters.Player;
import com.svalero.astra.characters.PowerUp;
import com.svalero.astra.util.Constants;

public class SpriteManager {

    private static SpriteManager instance;

    public Player player;
    public Array<Explosion> explosions;
    private LevelManager levelManager;
    private Animation<TextureRegion> explosionAnimation;
    private Sound powerUpSound;

    private SpriteManager() {
        levelManager = LevelManager.getInstance();
        explosions   = new Array<>();
    }

    public static SpriteManager getInstance() {
        if (instance == null) {
            instance = new SpriteManager();
        }
        return instance;
    }

    public void setExplosionAnimation(Animation<TextureRegion> animation) {
        this.explosionAnimation = animation;
    }

    public void setPowerUpSound(Sound sound) {
        this.powerUpSound = sound;
    }

    public void init(Player player) {
        this.player         = player;
        this.explosions.clear();
        player.lives        = Constants.PLAYER_LIVES;
        player.score        = 0;
        player.dead         = false;
        player.bullets.clear();
        player.shieldActive = false;
        player.speedActive  = false;
        player.doubleShot   = false;
        player.blinking     = false;
    }

    public void update(float dt) {
        player.update(dt);
        levelManager.update(dt);
        checkCollisions();

        // Actualizar explosiones
        for (int i = explosions.size - 1; i >= 0; i--) {
            Explosion exp = explosions.get(i);
            exp.update(dt);
            if (exp.isFinished()) {
                explosions.removeIndex(i);
            }
        }
    }

    private void spawnExplosion(float x, float y) {
        Explosion exp = new Explosion(x, y);
        if (explosionAnimation != null) {
            exp.setAnimation(explosionAnimation);
        }
        explosions.add(exp);
    }

    private void checkCollisions() {
        Array<Enemy>   enemies  = levelManager.enemies;
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
                        spawnExplosion(
                            enemy.position.x + enemy.rect.width / 2f,
                            enemy.position.y + enemy.rect.height / 2f
                        );
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
                    if (!player.shieldActive && !player.blinking) {
                        player.takeDamage();
                        player.startBlink();
                    }
                    enemy.bullets.removeIndex(i);
                }
            }
        }

        // Enemigos vs jugador colisión directa
        for (Enemy enemy : enemies) {
            if (enemy.rect.overlaps(player.rect)) {
                if (!player.shieldActive && !player.blinking) {
                    player.takeDamage();
                    player.startBlink();
                }
                enemy.takeDamage();
                if (enemy.isDead()) {
                    spawnExplosion(
                        enemy.position.x + enemy.rect.width / 2f,
                        enemy.position.y + enemy.rect.height / 2f
                    );
                }
            }
        }

        // Powerups vs jugador
        for (PowerUp powerUp : powerUps) {
            if (!powerUp.isCollected() && powerUp.rect.overlaps(player.rect)) {
                powerUp.applyTo(player);
                powerUp.collect();
                if (powerUpSound != null) powerUpSound.play(0.7f);
            }
        }
    }

    public void reset() {
        player.lives        = Constants.PLAYER_LIVES;
        player.score        = 0;
        player.bullets.clear();
        player.shieldActive = false;
        player.speedActive  = false;
        player.doubleShot   = false;
        player.blinking     = false;
        explosions.clear();
    }
}
