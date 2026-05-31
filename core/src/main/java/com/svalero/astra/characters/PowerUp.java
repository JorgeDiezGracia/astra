package com.svalero.astra.characters;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.svalero.astra.util.Util;

import java.util.HashMap;

public class PowerUp {

    public enum Type {
        SHIELD,
        SPEED,
        DOUBLE_SHOT
    }

    public static final float DURATION = 8f;
    public static final float SPEED    = 120f;
    public static final float WIDTH    = 34f;
    public static final float HEIGHT   = 34f;

    private static HashMap<Type, TextureRegion> textures = new HashMap<>();

    public Vector2 position;
    public Rectangle rect;
    public Type type;
    private boolean collected;
    private TextureRegion texture;

    public static void setTexture(Type type, TextureRegion texture) {
        textures.put(type, texture);
    }

    public PowerUp(float x, float y, Type type) {
        this.position  = new Vector2(x, y);
        this.rect      = new Rectangle(x, y, WIDTH, HEIGHT);
        this.type      = type;
        this.collected = false;
        this.texture   = textures.get(type);
    }

    public void update(float dt) {
        position.x -= SPEED * dt;
        rect.setPosition(position.x, position.y);
    }

    public void render(Batch batch) {
        if (texture != null && !collected) {
            batch.draw(texture, position.x, position.y, WIDTH, HEIGHT);
        }
    }

    public void collect() {
        this.collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    public boolean isOffScreen() {
        return Util.isOffScreenLeft(position.x, WIDTH);
    }

    public void applyTo(Player player) {
        switch (type) {
            case SHIELD:      player.activateShield(DURATION);      break;
            case SPEED:       player.activateSpeed(DURATION);       break;
            case DOUBLE_SHOT: player.activateDoubleShot(DURATION);  break;
        }
    }
}
