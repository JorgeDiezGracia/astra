package com.svalero.astra.characters;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.svalero.astra.util.Util;

public class Bullet {

    public Vector2 position;
    public Rectangle rect;
    private float velocityX;
    private float velocityY;
    private boolean fromPlayer;
    private TextureRegion texture;

    private static final float WIDTH  = 36f;
    private static final float HEIGHT = 9f;

    public Bullet(float x, float y, float velocityX, float velocityY, boolean fromPlayer) {
        this.position    = new Vector2(x, y);
        this.velocityX   = velocityX;
        this.velocityY   = velocityY;
        this.fromPlayer  = fromPlayer;
        this.rect        = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void update(float dt) {
        position.x += velocityX * dt;
        position.y += velocityY * dt;
        rect.setPosition(position.x, position.y);
    }

    public void render(Batch batch) {
        if (texture != null) {
            batch.draw(texture, position.x, position.y, WIDTH, HEIGHT);
        }
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public boolean isFromPlayer() {
        return fromPlayer;
    }

    public boolean isOffScreen() {
        return Util.isOffScreen(position.x, position.y, WIDTH, HEIGHT);
    }
}
