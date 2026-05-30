package com.svalero.astra;

import com.badlogic.gdx.Game;
import com.svalero.astra.screens.SplashScreen;

public class Astra extends Game {

    @Override
    public void create() {
        setScreen(new SplashScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
