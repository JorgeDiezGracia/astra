package com.svalero.astra.characters;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.svalero.astra.util.Constants;
import com.svalero.astra.util.Util;

public abstract class Enemy extends Character {

    public int points;
    protected float shootTimer;
    public Array<Bullet> bullets;
    protected Sound shootSound;
    protected Sound explosionSound;
    protected static TextureRegion defaultTexture;

    public Enemy(float x, float y, int lives, float width, float height, int points) {
        super(x, y, lives, width, height);
        this.points     = points;
        this.shootTimer = 0f;
        this.bullets    = new Array<>();
        if (defaultTexture != null) {
            this.currentFrame = defaultTexture;
        }
    }

    public static void setDefaultTexture(TextureRegion texture) {
        defaultTexture = texture;
    }

    public void setShootSound(Sound sound) {
        this.shootSound = sound;
    }

    public void setExplosionSound(Sound sound) {
        this.explosionSound = sound;
    }

    @Override
    public void update(float dt) {
        stateTime  += dt;
        shootTimer += dt;

        updateMovement(dt);

        if (shootTimer >= Constants.ENEMY_SHOOT_DELAY) {
            shoot();
            shootTimer = 0f;
        }

        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(dt);
            if (bullet.isOffScreen()) {
                bullets.removeIndex(i);
            }
        }

        if (currentAnimation != null) {
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        }

        updateRect();
    }

    @Override
    public void render(Batch batch) {
        if (currentFrame != null) {
            batch.draw(currentFrame,
                position.x, position.y,
                width / 2f, height / 2f,
                width, height,
                1f, 1f,
                -90f);
        }
        for (Bullet bullet : bullets) {
            bullet.render(batch);
        }
    }

    @Override
    public void die() {
        if (explosionSound != null) explosionSound.play(0.6f);
    }

    protected abstract void updateMovement(float dt);

    public boolean isOffScreenLeft() {
        return Util.isOffScreenLeft(position.x, width);
    }
}
