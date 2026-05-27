package com.svalero.astra.characters;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.svalero.astra.util.Constants;
import com.svalero.astra.util.Util;

public abstract class Enemy extends Character {

    // --- Puntuación que da al morir ---
    public int points;

    // --- Disparo ---
    protected float shootTimer;
    public Array<Bullet> bullets;

    // --- Sonido ---
    protected Sound shootSound;
    protected Sound explosionSound;

    public Enemy(float x, float y, int lives, float width, float height, int points) {
        super(x, y, lives, width, height);
        this.points      = points;
        this.shootTimer  = 0f;
        this.bullets     = new Array<>();
    }

    public void setShootSound(Sound sound) {
        this.shootSound = sound;
    }

    public void setExplosionSound(Sound sound) {
        this.explosionSound = sound;
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        shootTimer += dt;

        updateMovement(dt);

        // Disparo
        if (shootTimer >= Constants.ENEMY_SHOOT_DELAY) {
            shoot();
            shootTimer = 0f;
        }

        // Actualizar balas
        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(dt);
            if (bullet.isOffScreen()) {
                bullets.removeIndex(i);
            }
        }

        // Actualizar frame de animación
        if (currentAnimation != null) {
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        }

        updateRect();
    }

    @Override
    public void render(Batch batch) {
        super.render(batch);
        for (Bullet bullet : bullets) {
            bullet.render(batch);
        }
    }

    @Override
    public void die() {
        if (explosionSound != null) explosionSound.play(0.6f);
    }

    /**
     * Cada subclase define su propio patrón de movimiento
     */
    protected abstract void updateMovement(float dt);

    /**
     * Comprueba si el enemigo ha salido por la izquierda
     */
    public boolean isOffScreenLeft() {
        return Util.isOffScreenLeft(position.x, width);
    }
}
