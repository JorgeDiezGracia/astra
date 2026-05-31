package com.svalero.astra.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Character {

    public Vector2 position;
    public Vector2 velocity;
    public Rectangle rect;
    public int lives;
    protected boolean dead;
    public float stateTime;
    protected Animation<TextureRegion> currentAnimation;
    protected TextureRegion currentFrame;
    protected float width;
    protected float height;

    public Character(float x, float y, int lives, float width, float height) {
        this.position  = new Vector2(x, y);
        this.velocity  = new Vector2(0, 0);
        this.lives     = lives;
        this.dead      = false;
        this.stateTime = 0f;
        this.width     = width;
        this.height    = height;
        this.rect      = new Rectangle(x, y, width, height);
    }

    public void render(Batch batch) {
        if (currentFrame != null) {
            batch.draw(currentFrame, position.x, position.y, width, height);
        }
    }

    protected void updateRect() {
        rect.setPosition(position.x, position.y);
    }

    public boolean isDead() {
        return dead;
    }

    public void takeDamage() {
        lives--;
        if (lives <= 0) {
            dead = true;
            die();
        }
    }

    public abstract void update(float dt);
    public abstract void shoot();
    public abstract void die();
}
