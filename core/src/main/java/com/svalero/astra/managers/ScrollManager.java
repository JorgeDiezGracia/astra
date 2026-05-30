package com.svalero.astra.managers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.svalero.astra.util.Constants;

public class ScrollManager {

    private static ScrollManager instance;

    // Capas del parallax (de más lejos a más cerca)
    private TextureRegion[] layers;
    private float[] speeds;
    private float[] offsets;
    private int numLayers;

    private ScrollManager() {
    }

    public static ScrollManager getInstance() {
        if (instance == null) {
            instance = new ScrollManager();
        }
        return instance;
    }

    /**
     * Inicializar con las texturas de las capas del fondo
     * Llamar desde GameScreen cuando los assets estén cargados
     */
    public void init(TextureRegion[] layers, float[] speeds) {
        this.layers    = layers;
        this.speeds    = speeds;
        this.numLayers = layers.length;
        this.offsets   = new float[numLayers];
        for (int i = 0; i < numLayers; i++) {
            offsets[i] = 0f;
        }
    }

    public void update(float dt) {
        for (int i = 0; i < numLayers; i++) {
            offsets[i] += speeds[i] * dt;
            // Reiniciar offset cuando la textura ha dado una vuelta completa
            if (offsets[i] >= Constants.SCREEN_WIDTH) {
                offsets[i] = 0f;
            }
        }
    }

    /**
     * Pinta cada capa dos veces seguidas para el efecto de scroll infinito
     */
    public void render(Batch batch) {
        for (int i = 0; i < numLayers; i++) {
            if (layers[i] == null) continue;
            // Primera copia
            batch.draw(layers[i],
                -offsets[i], 0,
                Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
            // Segunda copia — justo a la derecha de la primera
            batch.draw(layers[i],
                Constants.SCREEN_WIDTH - offsets[i], 0,
                Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        }
    }

    public void dispose() {
        // Las texturas las gestiona ResourceManager
    }
}
