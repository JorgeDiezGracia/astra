package com.svalero.astra.characters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.svalero.astra.util.Constants;

public class EnemyBasic extends Enemy {

    private static TextureRegion ownTexture;

    public static void setDefaultTexture(TextureRegion texture) {
        ownTexture = texture;
    }

    public EnemyBasic(float x, float y) {
        super(x, y, 1, 93, 84, Constants.POINTS_BASIC);
        this.velocity.x = -Constants.ENEMY_SPEED_BASIC;
        if (ownTexture != null) this.currentFrame = ownTexture;
    }

    @Override
    protected void updateMovement(float dt) {
        position.x += velocity.x * dt;
    }

    @Override
    public void shoot() {
        if (shootSound != null) shootSound.play(0.3f);

        bullets.add(new Bullet(
            position.x,
            position.y + height / 2f - 5f,
            -Constants.ENEMY_BULLET_SPEED, 0,
            false
        ));
    }
}
