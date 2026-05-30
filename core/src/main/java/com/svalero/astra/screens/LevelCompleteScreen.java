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
import com.svalero.astra.managers.LevelManager;
import com.svalero.astra.managers.ResourceManager;
import com.svalero.astra.util.Constants;

public class LevelCompleteScreen implements Screen {

    private Game game;
    private Stage stage;
    private Skin skin;
    private ResourceManager resourceManager;
    private LevelManager levelManager;
    private int score;
    private int level;

    public LevelCompleteScreen(Game game, int score, int level) {
        this.game            = game;
        this.score           = score;
        this.level           = level;
        this.resourceManager = ResourceManager.getInstance();
        this.levelManager    = LevelManager.getInstance();
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
        titleStyle.fontColor = Color.YELLOW;
        Label title = new Label("LEVEL " + level + " COMPLETE!", titleStyle);
        table.add(title).padBottom(30).row();

        // Puntuación
        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.font      = resourceManager.fontMedium;
        textStyle.fontColor = Color.WHITE;
        Label lblScore = new Label("SCORE: " + score, textStyle);
        table.add(lblScore).padBottom(50).row();

        // Botón siguiente nivel o fin del juego
        TextButton btnNext = new TextButton(
            levelManager.gameComplete ? "FINISH" : "NEXT LEVEL", skin);
        TextButton btnMainMenu = new TextButton("MAIN MENU", skin);

        table.add(btnNext).width(250).height(55).padBottom(15).row();
        table.add(btnMainMenu).width(250).height(55).row();

        btnNext.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (levelManager.gameComplete) {
                    game.setScreen(new GameOverScreen(game, score));
                } else {
                    levelManager.nextLevel();
                    game.setScreen(new GameScreen(game));
                }
                dispose();
            }
        });

        btnMainMenu.addListener(new ChangeListener() {
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
