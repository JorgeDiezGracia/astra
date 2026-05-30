package com.svalero.astra.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.svalero.astra.util.Constants;

public class CameraManager {

    private static CameraManager instance;
    public OrthographicCamera camera;
    public Viewport viewport;

    private CameraManager() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, camera);
        camera.position.set(Constants.SCREEN_WIDTH / 2f, Constants.SCREEN_HEIGHT / 2f, 0);
        camera.update();
    }

    public static CameraManager getInstance() {
        if (instance == null) {
            instance = new CameraManager();
        }
        return instance;
    }

    public void update() {
        camera.update();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
