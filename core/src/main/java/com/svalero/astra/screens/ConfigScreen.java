package com.svalero.astra.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.svalero.astra.managers.ConfigManager;
import com.svalero.astra.managers.ResourceManager;
import com.svalero.astra.util.Constants;

public class ConfigScreen implements Screen {

    private Game game;
    private Stage stage;
    private Skin skin;
    private ConfigManager configManager;
    private ResourceManager resourceManager;

    public ConfigScreen(Game game) {
        this.game            = game;
        this.configManager   = ConfigManager.getInstance();
        this.resourceManager = ResourceManager.getInstance();
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
        Label title = new Label("OPTIONS", titleStyle);
        table.add(title).colspan(2).padBottom(40).row();

        // --- Opción 1: Sonido activado/desactivado ---
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font      = resourceManager.fontMedium;
        labelStyle.fontColor = Color.WHITE;

        Label lblSound = new Label("Sound:", labelStyle);
        CheckBox chkSound = new CheckBox("", skin);
        chkSound.setChecked(configManager.isSoundEnabled());
        table.add(lblSound).left().padBottom(20).padRight(20);
        table.add(chkSound).left().padBottom(20).row();

        // --- Opción 2: Volumen de música ---
        Label lblVolume = new Label("Music volume:", labelStyle);
        Slider sliderVolume = new Slider(0f, 1f, 0.1f, false, skin);
        sliderVolume.setValue(configManager.getMusicVolume());
        table.add(lblVolume).left().padBottom(20).padRight(20);
        table.add(sliderVolume).width(200).left().padBottom(20).row();

        // --- Opción 3: Dificultad ---
        Label lblDifficulty = new Label("Difficulty:", labelStyle);
        SelectBox<String> selectDifficulty = new SelectBox<>(skin);
        selectDifficulty.setItems("EASY", "NORMAL", "HARD");
        selectDifficulty.setSelected(configManager.getDifficulty());
        table.add(lblDifficulty).left().padBottom(40).padRight(20);
        table.add(selectDifficulty).left().padBottom(40).row();

        // --- Botón volver ---
        TextButton btnBack = new TextButton("BACK", skin);
        table.add(btnBack).colspan(2).width(250).height(55).row();

        // Listeners
        chkSound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                configManager.setSoundEnabled(chkSound.isChecked());
            }
        });

        sliderVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                configManager.setMusicVolume(sliderVolume.getValue());
            }
        });

        selectDifficulty.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                configManager.setDifficulty(selectDifficulty.getSelected());
            }
        });

        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0.1f, 1);
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
