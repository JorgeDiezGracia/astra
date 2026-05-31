package com.svalero.astra.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.svalero.astra.characters.Bullet;
import com.svalero.astra.characters.EnemyBasic;
import com.svalero.astra.characters.EnemyBoss;
import com.svalero.astra.characters.EnemyZigzag;
import com.svalero.astra.characters.Player;
import com.svalero.astra.characters.PowerUp;
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

    private ResourceManager resourceManager;
    private SpriteManager   spriteManager;
    private RenderManager   renderManager;
    private LevelManager    levelManager;
    private CameraManager   cameraManager;
    private ScrollManager   scrollManager;
    private ConfigManager   configManager;

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
        TextureAtlas atlas = resourceManager.getAtlas();

        // Jugador
        player = new Player(100, 300);
        player.setTexture(atlas.findRegion("playerShip1_blue"));

        // Texturas de balas
        Bullet.setDefaultTexture(atlas.findRegion("laserBlue01"));

        // Texturas de enemigos
        EnemyBasic.setDefaultTexture(atlas.findRegion("enemyRed1"));
        EnemyZigzag.setDefaultTexture(atlas.findRegion("enemyBlue2"));
        EnemyBoss.setDefaultTexture(atlas.findRegion("ufoRed"));

        // Texturas de powerups
        PowerUp.setTexture(PowerUp.Type.SHIELD,      atlas.findRegion("powerupBlue_shield"));
        PowerUp.setTexture(PowerUp.Type.SPEED,       atlas.findRegion("powerupBlue_bolt"));
        PowerUp.setTexture(PowerUp.Type.DOUBLE_SHOT, atlas.findRegion("powerupBlue_star"));

        // Animación de explosión con frames fire00-fire19
        Array<TextureRegion> explosionFrames = new Array<>();
        for (int i = 0; i <= 19; i++) {
            String name = "fire" + (i < 10 ? "0" + i : "" + i);
            TextureRegion frame = atlas.findRegion(name);
            if (frame != null) explosionFrames.add(frame);
        }
        if (explosionFrames.size > 0) {
            Animation<TextureRegion> explosionAnim = new Animation<>(0.05f, explosionFrames);
            spriteManager.setExplosionAnimation(explosionAnim);
        }

        // Inicializar managers
        spriteManager.init(player);
        levelManager.gameComplete  = false;
        levelManager.levelComplete = false;
        levelManager.loadLevel(levelManager.currentLevel == 0 ? 1 : levelManager.currentLevel);

        // Fondo parallax — 2 capas con velocidades distintas
        com.badlogic.gdx.graphics.Texture bgTexture = resourceManager.getBackground(
            levelManager.currentLevel == 1 ? "blue.png" : "darkPurple.png"
        );
        bgTexture.setWrap(
            com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat,
            com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat
        );
        TextureRegion[] layers = new TextureRegion[1];
        layers[0] = new TextureRegion(bgTexture);
        float[] speeds = { 60f };
        scrollManager.init(layers, speeds);
    }

    @Override
    public void render(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (music != null) music.pause();
            game.setScreen(new PauseScreen(game, this));
            return;
        }

        cameraManager.update();
        scrollManager.update(dt);
        spriteManager.update(dt);
        renderManager.render(player);

        if (player.isDead()) {
            stopMusic();
            game.setScreen(new GameOverScreen(game, player.score));
            dispose();
            return;
        }

        if (levelManager.levelComplete) {
            stopMusic();
            game.setScreen(new LevelCompleteScreen(game, player.score, levelManager.currentLevel));
            dispose();
            return;
        }

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
    }
}
