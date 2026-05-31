package com.svalero.astra.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.svalero.astra.util.Constants;

public class ResourceManager {

    private static ResourceManager instance;
    private AssetManager assetManager;

    // --- Fuente ---
    public BitmapFont fontSmall;
    public BitmapFont fontMedium;
    public BitmapFont fontLarge;

    private ResourceManager() {
        assetManager = new AssetManager();
    }

    // Singleton
    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    // --- Carga de assets ---

    public void loadAll() {
        // Atlas de texturas
        assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);

        // Fondos
        assetManager.load("black.png", com.badlogic.gdx.graphics.Texture.class);
        assetManager.load("blue.png", com.badlogic.gdx.graphics.Texture.class);
        assetManager.load("darkPurple.png", com.badlogic.gdx.graphics.Texture.class);
        assetManager.load("purple.png", com.badlogic.gdx.graphics.Texture.class);

        // Sonidos
        assetManager.load(Constants.SOUNDS_PATH + "laser1.ogg", Sound.class);
        assetManager.load(Constants.SOUNDS_PATH + "laser2.ogg", Sound.class);
        assetManager.load(Constants.SOUNDS_PATH + "explosion.ogg", Sound.class);
        assetManager.load(Constants.SOUNDS_PATH + "shieldUp.ogg", Sound.class);
        assetManager.load(Constants.SOUNDS_PATH + "shieldDown.ogg", Sound.class);

        // Música
        assetManager.load(Constants.MUSIC_PATH + "level1.ogg", Music.class);
        assetManager.load(Constants.MUSIC_PATH + "level2.ogg", Music.class);
    }

    /**
     * Devuelve el progreso de carga entre 0 y 1
     * Se usa en SplashScreen para la barra de progreso
     */
    public float getProgress() {
        return assetManager.getProgress();
    }

    /**
     * Avanza la carga — llamar en el render() del SplashScreen
     * Devuelve true cuando ha terminado
     */
    public boolean update() {
        return assetManager.update();
    }

    // --- Getters de assets ---

    public TextureAtlas getAtlas() {
        return assetManager.get(Constants.TEXTURE_ATLAS, TextureAtlas.class);
    }

    public Sound getSound(String filename) {
        return assetManager.get(Constants.SOUNDS_PATH + filename, Sound.class);
    }

    public Music getMusic(String filename) {
        return assetManager.get(Constants.MUSIC_PATH + filename, Music.class);
    }

    // --- Fuentes con FreeType ---

    public void generateFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal(Constants.FONTS_PATH + "kenvector_future.ttf")
        );

        FreeTypeFontGenerator.FreeTypeFontParameter params =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 16;
        fontSmall = generator.generateFont(params);

        params.size = 24;
        fontMedium = generator.generateFont(params);

        params.size = 42;
        fontLarge = generator.generateFont(params);

        generator.dispose();
    }

    // --- Limpieza ---

    public void dispose() {
        assetManager.dispose();
        if (fontSmall  != null) fontSmall.dispose();
        if (fontMedium != null) fontMedium.dispose();
        if (fontLarge  != null) fontLarge.dispose();
    }
    public com.badlogic.gdx.graphics.Texture getBackground(String filename) {
        return assetManager.get(filename, com.badlogic.gdx.graphics.Texture.class);
    }
}

