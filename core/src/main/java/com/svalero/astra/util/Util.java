package com.svalero.astra.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Util {

    /**
     * Genera una posición Y aleatoria válida para que un enemigo
     * aparezca por la derecha de la pantalla sin salirse
     */
    public static float randomY(float spriteHeight) {
        return MathUtils.random(0, Constants.SCREEN_HEIGHT - spriteHeight);
    }

    /**
     * Genera una posición X fuera de pantalla por la derecha
     * para el spawn de enemigos
     */
    public static float spawnX(float spriteWidth) {
        return Constants.SCREEN_WIDTH + spriteWidth;
    }

    /**
     * Comprueba si un objeto ha salido completamente de la pantalla
     * por la izquierda (balas enemigas, enemigos que atraviesan)
     */
    public static boolean isOffScreenLeft(float x, float width) {
        return x + width < 0;
    }

    /**
     * Comprueba si un objeto ha salido completamente de la pantalla
     * por la derecha (balas del jugador)
     */
    public static boolean isOffScreenRight(float x) {
        return x > Constants.SCREEN_WIDTH;
    }

    /**
     * Comprueba si un objeto está completamente fuera de pantalla
     * en cualquier dirección
     */
    public static boolean isOffScreen(float x, float y, float width, float height) {
        return x + width < 0 ||
            x > Constants.SCREEN_WIDTH ||
            y + height < 0 ||
            y > Constants.SCREEN_HEIGHT;
    }

    /**
     * Mantiene al jugador dentro de los límites de la pantalla
     */
    public static Vector2 clampToScreen(Vector2 position, float width, float height) {
        position.x = MathUtils.clamp(position.x, 0, Constants.SCREEN_WIDTH - width);
        position.y = MathUtils.clamp(position.y, 0, Constants.SCREEN_HEIGHT - height);
        return position;
    }

    /**
     * Calcula el ángulo en grados entre dos puntos
     * Útil para disparos direccionales del Boss
     */
    public static float angleBetween(Vector2 origin, Vector2 target) {
        return MathUtils.atan2(
            target.y - origin.y,
            target.x - origin.x
        ) * MathUtils.radiansToDegrees;
    }
}
