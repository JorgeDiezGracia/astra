package com.svalero.astra.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.svalero.astra.util.Constants;

public class EnemyZigzag extends Enemy {

    private static TextureRegion ownTexture;

    private float amplitude;
    private float frequency;
    private float originY;

    public static void setDefaultTexture(TextureRegion texture) {
        ownTexture = texture;
    }

    public EnemyZigzag(float x, float y) {
        super(x, y, 2, 93, 84, Constants.POINTS_ZIGZAG);
        this.velocity.x = -Constants.ENEMY_SPEED_ZIGZAG;
        this.amplitude  = MathUtils.random(80f, 160f);
        this.frequency  = MathUtils.random(1.5f, 3f);
        this.originY    = y;
        if (ownTexture != null) this.currentFrame = ownTexture;
    }

    @Override
    protected void updateMovement(float dt) {
        position.x += velocity.x * dt;
        position.y = originY + amplitude * MathUtils.sin(frequency * stateTime);
    }

    @Override
    public void shoot() {
        if (shootSound != null) shootSound.play(0.3f);

        bullets.add(new Bullet(
            position.x,
            position.y + height / 2f,
            -Constants.ENEMY_BULLET_SPEED,
            -80f,
            false
        ));
        bullets.add(new Bullet(
            position.x,
            position.y + height / 2f,
            -Constants.ENEMY_BULLET_SPEED,
            80f,
            false
        ));
    }
}
