package com.svalero.astra.util;

public class Constants {

    // --- Pantalla ---
    public static final int SCREEN_WIDTH  = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final String TITLE      = "Astra";

    // --- Juego ---
    public static final int FPS = 60;

    // --- Jugador ---
    public static final float PLAYER_SPEED        = 300f;
    public static final float PLAYER_BULLET_SPEED = 600f;
    public static final int   PLAYER_LIVES        = 3;
    public static final float PLAYER_SHOOT_DELAY  = 0.25f; // segundos entre disparos

    // --- Enemigos ---
    public static final float ENEMY_SPEED_BASIC  = 180f;
    public static final float ENEMY_SPEED_ZIGZAG = 200f;
    public static final float ENEMY_SPEED_BOSS   = 100f;
    public static final float ENEMY_BULLET_SPEED = 350f;
    public static final float ENEMY_SHOOT_DELAY  = 1.5f;

    // --- Puntuación ---
    public static final int POINTS_BASIC  = 100;
    public static final int POINTS_ZIGZAG = 200;
    public static final int POINTS_BOSS   = 1000;

    // --- Niveles ---
    public static final int TOTAL_LEVELS = 2;

    // --- Assets ---
    public static final String TEXTURE_ATLAS = "atlas/astra.atlas";
    public static final String SOUNDS_PATH   = "sounds/";
    public static final String MUSIC_PATH    = "music/";
    public static final String LEVELS_PATH   = "levels/";
    public static final String FONTS_PATH    = "fonts/";

    // --- Base de datos ---
    public static final String DB_NAME      = "astra.db";
    public static final int    TOP_SCORES   = 10;

    // --- Preferencias ---
    public static final String PREFS_NAME         = "astra-prefs";
    public static final String PREF_SOUND_ENABLED = "soundEnabled";
    public static final String PREF_MUSIC_VOLUME  = "musicVolume";
    public static final String PREF_DIFFICULTY    = "difficulty";
}
