package com.svalero.astra.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.svalero.astra.managers.ScoreManager;
import com.svalero.astra.util.Constants;

import java.util.List;

public class GameOverScreen implements Screen {

    private Game game;
    private Stage stage;
    private Skin skin;
    private ResourceManager resourceManager;
    private ScoreManager scoreManager;
    private int finalScore;
    private boolean victory;
    private TextField tfName;

    public GameOverScreen(Game game, int finalScore, boolean victory) {
        this.game            = game;
        this.finalScore      = finalScore;
        this.victory         = victory;
        this.resourceManager = ResourceManager.getInstance();
        this.scoreManager    = ScoreManager.getInstance();
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        skin  = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        // Tabla principal dividida en dos columnas
        Table root = new Table();
        root.setFillParent(true);
        root.center();
        stage.addActor(root);

        // --- Columna izquierda: resultado ---
        Table leftTable = new Table();
        leftTable.center();

        // Título
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        BitmapFont font = resourceManager.fontLarge;
        if (font == null) font = skin.getFont("default-font");
        titleStyle.font      = font;
        titleStyle.fontColor = victory ? Color.YELLOW : Color.RED;
        Label title = new Label(victory ? "YOU WIN!" : "GAME OVER", titleStyle);
        leftTable.add(title).padBottom(20).row();

        // Puntuación
        Label.LabelStyle textStyle = new Label.LabelStyle();
        BitmapFont fontMedium = resourceManager.fontMedium;
        if (fontMedium == null) fontMedium = skin.getFont("default-font");
        textStyle.font      = fontMedium;
        textStyle.fontColor = Color.WHITE;
        Label lblScore = new Label("SCORE: " + finalScore, textStyle);
        leftTable.add(lblScore).padBottom(20).row();

        // Campo nombre
        Label lblName = new Label("Enter your name:", textStyle);
        leftTable.add(lblName).padBottom(8).row();

        tfName = new TextField("", skin);
        tfName.setMaxLength(15);
        tfName.setMessageText("Your name...");
        leftTable.add(tfName).width(250).height(45).padBottom(20).row();

        // Botones
        TextButton btnSave      = new TextButton("SAVE SCORE", skin);
        TextButton btnPlayAgain = new TextButton("PLAY AGAIN", skin);
        TextButton btnMainMenu  = new TextButton("MAIN MENU", skin);

        leftTable.add(btnSave).width(220).height(50).padBottom(10).row();
        leftTable.add(btnPlayAgain).width(220).height(50).padBottom(10).row();
        leftTable.add(btnMainMenu).width(220).height(50).row();

        // --- Columna derecha: ranking ---
        Table rightTable = new Table();
        rightTable.center();

        Label rankTitle = new Label("TOP 10", titleStyle);
        rightTable.add(rankTitle).padBottom(15).row();

        List<ScoreManager.ScoreEntry> topScores = scoreManager.getTopScores(10);
        if (topScores.isEmpty()) {
            Label noScores = new Label("No scores yet!", textStyle);
            rightTable.add(noScores).row();
        } else {
            for (int i = 0; i < topScores.size(); i++) {
                ScoreManager.ScoreEntry entry = topScores.get(i);
                String line = (i + 1) + ".  " + entry.name + "  -  " + entry.score;
                Label lbl = new Label(line, textStyle);
                rightTable.add(lbl).left().padBottom(5).row();
            }
        }

        // Añadir columnas al root
        root.add(leftTable).padRight(80).top();
        root.add(rightTable).top();

        // Listeners
        btnSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String name = tfName.getText().trim();
                if (!name.isEmpty()) {
                    scoreManager.saveScore(name, finalScore);
                    // Refrescar ranking
                    stage.clear();
                    show();
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
