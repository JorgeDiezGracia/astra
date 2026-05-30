package com.svalero.astra.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.svalero.astra.managers.ResourceManager;
import com.svalero.astra.util.Constants;

public class SplashScreen implements Screen {

    private Game game;
    private ResourceManager resourceManager;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private float progress;

    public SplashScreen(Game game) {
        this.game            = game;
        this.resourceManager = ResourceManager.getInstance();
        this.batch           = new SpriteBatch();
        this.shapeRenderer   = new ShapeRenderer();
    }

    @Override
    public void show() {
        resourceManager.loadAll();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Avanzar carga
        progress = resourceManager.getProgress();
        boolean done = resourceManager.update();

        // Dibujar barra de progreso
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Fondo de la barra
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1);
        shapeRenderer.rect(
            Constants.SCREEN_WIDTH / 2f - 200,
            Constants.SCREEN_HEIGHT / 2f - 15,
            400, 30
        );

        // Barra de progreso
        shapeRenderer.setColor(0.2f, 0.6f, 1f, 1);
        shapeRenderer.rect(
            Constants.SCREEN_WIDTH / 2f - 200,
            Constants.SCREEN_HEIGHT / 2f - 15,
            400 * progress, 30
        );

        shapeRenderer.end();

        // Cuando termine la carga ir al menú principal
        if (done) {
            resourceManager.generateFonts();
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }
}
