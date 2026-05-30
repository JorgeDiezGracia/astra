package com.svalero.astra.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.svalero.astra.characters.Enemy;
import com.svalero.astra.characters.Player;
import com.svalero.astra.characters.PowerUp;
import com.svalero.astra.util.Constants;

public class RenderManager {

    private static RenderManager instance;

    public SpriteBatch batch;
    private CameraManager cameraManager;
    private ScrollManager scrollManager;
    private LevelManager levelManager;
    private ResourceManager resourceManager;

    private RenderManager() {
        batch           = new SpriteBatch();
        cameraManager   = CameraManager.getInstance();
        scrollManager   = ScrollManager.getInstance();
        levelManager    = LevelManager.getInstance();
        resourceManager = ResourceManager.getInstance();
    }

    public static RenderManager getInstance() {
        if (instance == null) {
            instance = new RenderManager();
        }
        return instance;
    }

    public void render(Player player) {
        // Limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cameraManager.camera.combined);
        batch.begin();

        // 1. Fondo parallax
        scrollManager.render(batch);

        // 2. Powerups
        for (PowerUp powerUp : levelManager.powerUps) {
            powerUp.render(batch);
        }

        // 3. Enemigos y sus balas
        for (Enemy enemy : levelManager.enemies) {
            enemy.render(batch);
        }

        // 4. Jugador y sus balas
        player.render(batch);

        // 5. HUD
        renderHUD(player);

        batch.end();
    }

    private void renderHUD(Player player) {
        BitmapFont font = resourceManager.fontMedium;
        if (font == null) return;

        // Puntuación
        font.setColor(Color.WHITE);
        font.draw(batch, "SCORE: " + player.score,
            20, Constants.SCREEN_HEIGHT - 20);

        // Vidas
        font.draw(batch, "LIVES: " + player.lives,
            Constants.SCREEN_WIDTH / 2f - 60,
            Constants.SCREEN_HEIGHT - 20);

        // Nivel actual
        font.draw(batch, "LEVEL: " + levelManager.currentLevel,
            Constants.SCREEN_WIDTH - 160,
            Constants.SCREEN_HEIGHT - 20);

        // Indicador de escudo activo
        if (player.shieldActive) {
            font.setColor(Color.CYAN);
            font.draw(batch, "SHIELD",
                20, Constants.SCREEN_HEIGHT - 50);
        }

        // Indicador de doble disparo
        if (player.doubleShot) {
            font.setColor(Color.YELLOW);
            font.draw(batch, "DOUBLE",
                20, Constants.SCREEN_HEIGHT - 75);
        }

        // Indicador de velocidad
        if (player.speedActive) {
            font.setColor(Color.GREEN);
            font.draw(batch, "SPEED",
                20, Constants.SCREEN_HEIGHT - 100);
        }

        font.setColor(Color.WHITE);
    }

    public void dispose() {
        batch.dispose();
    }
}
