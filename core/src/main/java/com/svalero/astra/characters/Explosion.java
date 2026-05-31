package com.svalero.astra.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Explosion {

    private Animation<TextureRegion> animation;
    private float stateTime;
    private Vector2 position;
    private float width;
    private float height;
    private boolean finished;

    private static final float FRAME_DURATION = 0.05f;
    private static final float SIZE = 100f;

    public Explosion(float x, float y) {
        this.position  = new Vector2(x - SIZE / 2f, y - SIZE / 2f);
        this.stateTime = 0f;
        this.finished  = false;
        this.width     = SIZE;
        this.height    = SIZE;
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
    }

    public void update(float dt) {
        stateTime += dt;
        if (animation != null && animation.isAnimationFinished(stateTime)) {
            finished = true;
        }
    }

    public void render(Batch batch) {
        if (animation == null || finished) return;
        TextureRegion frame = animation.getKeyFrame(stateTime, false);
        batch.draw(frame,
            position.x, position.y,
            width / 2f, height / 2f,
            width, height,
            1f, 1f,
            90f);
    }

    public boolean isFinished() {
        return finished;
    }
}
