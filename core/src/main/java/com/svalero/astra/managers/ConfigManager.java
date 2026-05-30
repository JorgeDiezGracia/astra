package com.svalero.astra.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.svalero.astra.util.Constants;

public class ConfigManager {

    private static ConfigManager instance;
    private Preferences prefs;

    private ConfigManager() {
        prefs = Gdx.app.getPreferences(Constants.PREFS_NAME);
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    // --- Sonido ---

    public boolean isSoundEnabled() {
        return prefs.getBoolean(Constants.PREF_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean enabled) {
        prefs.putBoolean(Constants.PREF_SOUND_ENABLED, enabled);
        prefs.flush();
    }

    // --- Volumen música ---

    public float getMusicVolume() {
        return prefs.getFloat(Constants.PREF_MUSIC_VOLUME, 0.7f);
    }

    public void setMusicVolume(float volume) {
        prefs.putFloat(Constants.PREF_MUSIC_VOLUME, volume);
        prefs.flush();
    }

    // --- Dificultad ---

    public String getDifficulty() {
        return prefs.getString(Constants.PREF_DIFFICULTY, "NORMAL");
    }

    public void setDifficulty(String difficulty) {
        prefs.putString(Constants.PREF_DIFFICULTY, difficulty);
        prefs.flush();
    }

    /**
     * Devuelve el multiplicador de velocidad enemiga según dificultad
     */
    public float getDifficultyMultiplier() {
        switch (getDifficulty()) {
            case "EASY":   return 0.75f;
            case "HARD":   return 1.5f;
            default:       return 1.0f;  // NORMAL
        }
    }
}
