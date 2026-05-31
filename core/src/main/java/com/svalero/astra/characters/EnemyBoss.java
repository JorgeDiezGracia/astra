package com.svalero.astra.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.svalero.astra.util.Constants;

public class EnemyBoss extends Enemy {

    private static TextureRegion ownTexture;

    public enum Phase {
        PHASE_1,
        PHASE_2,
        PHASE_3
    }

    public Phase currentPhase;
    private int maxLives;
    private float verticalSpeed;
    private int verticalDirection;
    private float directionTimer;

    public static void setDefaultTexture(TextureRegion texture) {
        ownTexture = texture;
    }

    public EnemyBoss(float x, float y) {
        super(x, y, 20, 222, 195, Constants.POINTS_BOSS);
        this.velocity.x        = -Constants.ENEMY_SPEED_BOSS;
        this.maxLives          = 20;
        this.currentPhase      = Phase.PHASE_1;
        this.verticalSpeed     = 120f;
        this.verticalDirection = 1;
        this.directionTimer    = 0f;
        if (ownTexture != null) this.currentFrame = ownTexture;
    }

    @Override
    protected void updateMovement(float dt) {
        updatePhase();

        if (position.x > Constants.SCREEN_WIDTH * 0.65f) {
            position.x += velocity.x * dt;
        }

        directionTimer += dt;
        float threshold = currentPhase == Phase.PHASE_3 ? 0.75f : 1.5f;
        if (directionTimer > threshold) {
            verticalDirection *= -1;
            directionTimer = 0f;
        }
        position.y += verticalSpeed * verticalDirection * dt;

        if (position.y < 0) {
            position.y = 0;
            verticalDirection = 1;
        }
        if (position.y + height > Constants.SCREEN_HEIGHT) {
            position.y = Constants.SCREEN_HEIGHT - height;
            verticalDirection = -1;
        }

        switch (currentPhase) {
            case PHASE_2: verticalSpeed = 180f; break;
            case PHASE_3: verticalSpeed = 240f; break;
            default: break;
        }
    }

    private void updatePhase() {
        float lifePercent = (float) lives / maxLives;
        if (lifePercent > 0.66f)      currentPhase = Phase.PHASE_1;
        else if (lifePercent > 0.33f) currentPhase = Phase.PHASE_2;
        else                          currentPhase = Phase.PHASE_3;
    }

    @Override
    public void shoot() {
        if (shootSound != null) shootSound.play(0.4f);

        switch (currentPhase) {
            case PHASE_1:
                bullets.add(new Bullet(
                    position.x,
                    position.y + height / 2f,
                    -Constants.ENEMY_BULLET_SPEED, 0,
                    false
                ));
                break;
            case PHASE_2:
                shootFan(3);
                break;
            case PHASE_3:
                shootFan(5);
                break;
        }
    }

    private void shootFan(int numBullets) {
        float angleStep  = 30f / (numBullets - 1);
        float startAngle = 180f - (numBullets - 1) * angleStep / 2f;

        for (int i = 0; i < numBullets; i++) {
            float angle = startAngle + i * angleStep;
            float rad   = angle * MathUtils.degreesToRadians;
            bullets.add(new Bullet(
                position.x,
                position.y + height / 2f,
                MathUtils.cos(rad) * Constants.ENEMY_BULLET_SPEED,
                MathUtils.sin(rad) * Constants.ENEMY_BULLET_SPEED,
                false
            ));
        }
    }

    public Phase getPhase() {
        return currentPhase;
    }
}
