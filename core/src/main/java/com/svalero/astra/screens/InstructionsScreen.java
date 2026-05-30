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
import com.svalero.astra.managers.ResourceManager;
import com.svalero.astra.util.Constants;

public class InstructionsScreen implements Screen {

    private Game game;
    private Stage stage;
    private Skin skin;
    private ResourceManager resourceManager;

    public InstructionsScreen(Game game) {
        this.game            = game;
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
        Label title = new Label("HOW TO PLAY", titleStyle);
        table.add(title).padBottom(40).row();

        // Instrucciones
        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.font      = resourceManager.fontMedium;
        textStyle.fontColor = Color.WHITE;

        String[] lines = {
            "MOVE:     W A S D  or  Arrow Keys",
            "SHOOT:    Space Bar  (hold to auto-fire)",
            "PAUSE:    Escape",
            "",
            "ENEMIES:",
            "  Red ship    ->  100 pts  (flies straight)",
            "  Blue ship   ->  200 pts  (zigzag movement)",
            "  BOSS        ->  1000 pts (3 phases!)",
            "",
            "POWERUPS:",
            "  CYAN    ->  Shield (8 seconds)",
            "  GREEN   ->  Speed boost (8 seconds)",
            "  YELLOW  ->  Double shot (8 seconds)",
            "",
            "Survive all waves and defeat the Boss",
            "to complete each level. Good luck!"
        };

        for (String line : lines) {
            Label lbl = new Label(line, textStyle);
            table.add(lbl).left().padBottom(6).row();
        }

        // Botón volver
        TextButton btnBack = new TextButton("BACK", skin);
        table.add(btnBack).width(250).height(55).padTop(30).row();

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
