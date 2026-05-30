package com.svalero.astra.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.svalero.astra.characters.Player;
import com.svalero.astra.managers.CameraManager;
import com.svalero.astra.managers.ConfigManager;
import com.svalero.astra.managers.LevelManager;
import com.svalero.astra.managers.RenderManager;
import com.svalero.astra.managers.ResourceManager;
import com.svalero.astra.managers.ScrollManager;
import com.svalero.astra.managers.SpriteManager;

public class GameScreen implements Screen {

    private Game game;
    private Player player;

    // Managers
    private ResourceManager resourceManager;
    private SpriteManager spriteManager;
    private RenderManager renderManager;
    private LevelManager levelManager;
    private CameraManager cameraManager;
    private ScrollManager scrollManager;
    private ConfigManager configManager;

    // Música
    private com.badlogic.gdx.audio.Music music;

    public GameScreen(Game game) {
        this.game            = game;
        this.resourceManager = ResourceManager.getInstance();
        this.spriteManager   = SpriteManager.getInstance();
        this.renderManager   = RenderManager.getInstance();
        this.levelManager    = LevelManager.getInstance();
        this.cameraManager   = CameraManager.getInstance();
        this.scrollManager   = ScrollManager.getInstance();
        this.configManager   = ConfigManager.getInstance();
    }

    @Override
    public void show() {
        // Crear jugador
        player = new Player(100, 300);

        // Inicializar SpriteManager con el jugador
        spriteManager.init(player);

        // Cargar nivel
        levelManager.loadLevel(levelManager.currentLevel);

        // Inicializar scroll con fondos del atlas
        // TODO: sustituir null por las TextureRegions reales cuando tengamos el atlas
        com.badlogic.gdx.graphics.g2d.TextureRegion[] layers = new
            com.badlogic.gdx.graphics.g2d.TextureRegion[2];
        float[] speeds = { 30f, 80f };
        scrollManager.init(layers, speeds);

        // Música
        String musicFile = levelManager.currentLevel == 1 ? "level1.ogg" : "level2.ogg";
        music = resourceManager.getMusic(musicFile);
        if (music != null && configManager.isSoundEnabled()) {
            music.setLooping(true);
            music.setVolume(configManager.getMusicVolume());
            music.play();
        }
    }

    @Override
    public void render(float dt) {
        // Pausa con Escape
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (music != null) music.pause();
            game.setScreen(new PauseScreen(game, this));
            return;
        }

        // Actualizar cámara y scroll
        cameraManager.update();
        scrollManager.update(dt);

        // Actualizar lógica
        spriteManager.update(dt);

        // Pintar
        renderManager.render(player);

        // Comprobar game over
        if (player.isDead()) {
            stopMusic();
            game.setScreen(new GameOverScreen(game, player.score));
            dispose();
            return;
        }

        // Comprobar nivel completo
        if (levelManager.levelComplete) {
            stopMusic();
            game.setScreen(new LevelCompleteScreen(game, player.score,
                levelManager.currentLevel));
            dispose();
            return;
        }

        // Comprobar juego completo
        if (levelManager.gameComplete) {
            stopMusic();
            game.setScreen(new GameOverScreen(game, player.score));
            dispose();
        }
    }

    private void stopMusic() {
        if (music != null) {
            music.stop();
            music.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        cameraManager.resize(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stopMusic();
        renderManager.dispose();
    }
}
