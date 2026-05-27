package com.svalero.astra.characters;

import com.badlogic.gdx.math.MathUtils;
import com.svalero.astra.util.Constants;

public class EnemyZigzag extends Enemy {

    private float amplitude;  // altura del zigzag
    private float frequency;  // velocidad del zigzag
    private float originY;    // posición Y inicial

    public EnemyZigzag(float x, float y) {
        super(x, y, 2, 93, 84, Constants.POINTS_ZIGZAG);
        this.velocity.x = -Constants.ENEMY_SPEED_ZIGZAG;
        this.amplitude  = MathUtils.random(80f, 160f);
        this.frequency  = MathUtils.random(1.5f, 3f);
        this.originY    = y;
    }

    @Override
    protected void updateMovement(float dt) {
        // Movimiento horizontal constante
        position.x += velocity.x * dt;

        // Movimiento vertical sinusoidal
        position.y = originY + amplitude * MathUtils.sin(frequency * stateTime);
    }

    @Override
    public void shoot() {
        if (shootSound != null) shootSound.play(0.3f);

        // Dispara en diagonal hacia abajo-izquierda y arriba-izquierda
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
