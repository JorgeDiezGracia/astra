package com.svalero.astra.characters;

import com.svalero.astra.util.Constants;

public class EnemyBasic extends Enemy {

    public EnemyBasic(float x, float y) {
        super(x, y, 1, 93, 84, Constants.POINTS_BASIC);
        this.velocity.x = -Constants.ENEMY_SPEED_BASIC;
    }

    @Override
    protected void updateMovement(float dt) {
        // Movimiento recto hacia la izquierda
        position.x += velocity.x * dt;
    }

    @Override
    public void shoot() {
        if (shootSound != null) shootSound.play(0.3f);

        // Dispara una bala hacia la izquierda
        bullets.add(new Bullet(
            position.x,
            position.y + height / 2f - 5f,
            -Constants.ENEMY_BULLET_SPEED, 0,
            false
        ));
    }
}
