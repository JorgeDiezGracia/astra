package com.svalero.astra.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.svalero.astra.util.Constants;
import com.svalero.astra.util.Util;

public class Player extends Character {

    public int score;
    private float shootTimer;
    public Array<Bullet> bullets;
    public boolean shieldActive;
    public float shieldTimer;
    public boolean speedActive;
    public float speedTimer;
    public boolean doubleShot;
    public float doubleShotTimer;
    private Sound shootSound;
    private Sound powerUpSound;

    // --- Parpadeo ---
    public boolean blinking;
    private float blinkTimer;
    private static final float BLINK_DURATION = 2f;
    private static final float BLINK_SPEED    = 0.1f;

    public Player(float x, float y) {
        super(x, y, Constants.PLAYER_LIVES, 99, 75);
        this.score        = 0;
        this.shootTimer   = 0f;
        this.bullets      = new Array<>();
        this.shieldActive = false;
        this.speedActive  = false;
        this.doubleShot   = false;
        this.blinking     = false;
        this.blinkTimer   = 0f;
    }

    public void setShootSound(Sound sound) {
        this.shootSound = sound;
    }

    public void setPowerUpSound(Sound sound) {
        this.powerUpSound = sound;
    }

    public Sound getPowerUpSound() {
        return powerUpSound;
    }

    public void setTexture(TextureRegion texture) {
        this.currentFrame = texture;
    }

    public void startBlink() {
        blinking   = true;
        blinkTimer = BLINK_DURATION;
    }

    @Override
    public void update(float dt) {
        stateTime  += dt;
        shootTimer += dt;

        float speed = speedActive ? Constants.PLAYER_SPEED * 1.6f : Constants.PLAYER_SPEED;
        velocity.set(0, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
            velocity.x = speed;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
            velocity.x = -speed;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
            velocity.y = speed;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
            velocity.y = -speed;

        position.x += velocity.x * dt;
        position.y += velocity.y * dt;
        Util.clampToScreen(position, width, height);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            shoot();
        }

        for (int i = bullets.size - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(dt);
            if (Util.isOffScreenRight(bullet.position.x)) {
                bullets.removeIndex(i);
            }
        }

        if (shieldActive) {
            shieldTimer -= dt;
            if (shieldTimer <= 0) shieldActive = false;
        }
        if (speedActive) {
            speedTimer -= dt;
            if (speedTimer <= 0) speedActive = false;
        }
        if (doubleShot) {
            doubleShotTimer -= dt;
            if (doubleShotTimer <= 0) doubleShot = false;
        }

        // Parpadeo
        if (blinking) {
            blinkTimer -= dt;
            if (blinkTimer <= 0) blinking = false;
        }

        if (currentAnimation != null) {
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        }

        updateRect();
    }

    @Override
    public void shoot() {
        if (shootTimer < Constants.PLAYER_SHOOT_DELAY) return;
        shootTimer = 0f;

        if (shootSound != null) shootSound.play(0.5f);

        bullets.add(new Bullet(
            position.x + width,
            position.y + height / 2f - 5f,
            Constants.PLAYER_BULLET_SPEED, 0,
            true
        ));

        if (doubleShot) {
            bullets.add(new Bullet(
                position.x + width,
                position.y + height / 2f + 10f,
                Constants.PLAYER_BULLET_SPEED, 0,
                true
            ));
        }
    }

    @Override
    public void render(Batch batch) {
        boolean visible = !blinking || (int)(blinkTimer / BLINK_SPEED) % 2 == 0;
        if (currentFrame != null && visible) {
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
    public void die() {}

    public void activateShield(float duration) {
        shieldActive = true;
        shieldTimer  = duration;
    }

    public void activateSpeed(float duration) {
        speedActive = true;
        speedTimer  = duration;
    }

    public void activateDoubleShot(float duration) {
        doubleShot      = true;
        doubleShotTimer = duration;
    }

    public void addScore(int points) {
        score += points;
    }
}
