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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.svalero.astra.managers.ResourceManager;
import com.svalero.astra.util.Constants;

public class GameOverScreen implements Screen {

    private Game game;
    private Stage stage;
    private Skin skin;
    private ResourceManager resourceManager;
    private int finalScore;
    private TextField tfName;

    public GameOverScreen(Game game, int finalScore) {
        this.game            = game;
        this.finalScore      = finalScore;
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
        titleStyle.fontColor = Color.RED;
        Label title = new Label("GAME OVER", titleStyle);
        table.add(title).padBottom(30).row();

        // Puntuación final
        Label.LabelStyle scoreStyle = new Label.LabelStyle();
        scoreStyle.font      = resourceManager.fontMedium;
        scoreStyle.fontColor = Color.WHITE;
        Label lblScore = new Label("SCORE: " + finalScore, scoreStyle);
        table.add(lblScore).padBottom(30).row();

        // Campo nombre para el ranking
        Label lblName = new Label("Enter your name:", scoreStyle);
        table.add(lblName).padBottom(10).row();

        tfName = new TextField("", skin);
        tfName.setMaxLength(15);
        tfName.setMessageText("Your name...");
        table.add(tfName).width(300).height(50).padBottom(30).row();

        // Botones
        TextButton btnSave      = new TextButton("SAVE SCORE", skin);
        TextButton btnPlayAgain = new TextButton("PLAY AGAIN", skin);
        TextButton btnMainMenu  = new TextButton("MAIN MENU", skin);

        table.add(btnSave).width(250).height(55).padBottom(15).row();
        table.add(btnPlayAgain).width(250).height(55).padBottom(15).row();
        table.add(btnMainMenu).width(250).height(55).row();

        // Listeners
        btnSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String name = tfName.getText().trim();
                if (!name.isEmpty()) {
                    // TODO: guardar en SQLite cuando implementemos ScoreManager
                    System.out.println("Saving score: " + name + " - " + finalScore);
                }
            }
        });

        btnPlayAgain.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
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
