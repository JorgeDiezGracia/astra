package com.svalero.astra.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.svalero.astra.managers.ConfigManager;
import com.svalero.astra.managers.ResourceManager;
import com.svalero.astra.util.Constants;

public class PauseScreen implements Screen {

    private Game game;
    private GameScreen gameScreen;  // referencia para poder continuar
    private Stage stage;
    private Skin skin;
    private ResourceManager resourceManager;
    private ConfigManager configManager;

    public PauseScreen(Game game, GameScreen gameScreen) {
        this.game            = game;
        this.gameScreen      = gameScreen;
        this.resourceManager = ResourceManager.getInstance();
        this.configManager   = ConfigManager.getInstance();
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        skin  = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // Título
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font      = resourceManager.fontLarge;
        titleStyle.fontColor = Color.CYAN;
        Label title = new Label("PAUSED", titleStyle);
        table.add(title).padBottom(50).row();

        // Botones
        TextButton btnResume    = new TextButton("RESUME", skin);
        TextButton btnSound     = new TextButton(
            configManager.isSoundEnabled() ? "SOUND: ON" : "SOUND: OFF", skin);
        TextButton btnMainMenu  = new TextButton("MAIN MENU", skin);
        TextButton btnExit      = new TextButton("EXIT GAME", skin);

        table.add(btnResume).width(250).height(55).padBottom(15).row();
        table.add(btnSound).width(250).height(55).padBottom(15).row();
        table.add(btnMainMenu).width(250).height(55).padBottom(15).row();
        table.add(btnExit).width(250).height(55).row();

        // Listeners
        btnResume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(gameScreen);
                dispose();
            }
        });

        btnSound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = !configManager.isSoundEnabled();
                configManager.setSoundEnabled(enabled);
                btnSound.setText(enabled ? "SOUND: ON" : "SOUND: OFF");
            }
        });

        btnMainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                gameScreen.dispose();
                dispose();
            }
        });

        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0.5f, 0.8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
