package es.ucm.fdi.moviles.engine.system;

import es.ucm.fdi.moviles.engine.graphics.Graphics;
import es.ucm.fdi.moviles.engine.input.Input;

/**
 * Interfaz del objeto de juego, que define métodos para obtener tanto los gráficos como el input
 * del juego actual.
 */
public interface Game
{
    /**
     * Devuelve el motor de renderizado
     * @return los gráficos usados para el juego
     */
    Graphics getGraphics();

    /**
     * Devuelve el motor de input
     * @return el motor de inpur usado por el juego
     */
    Input getInput();

    /**
     * Cambia de estado de juego
     * @param state estado que pasará a ejecutarse desde el final del update del estado anterior
     */
    void setGameState(GameState state);

    /**
     * Bucle principal del juego; se ejecuta hasta que se llame a GameOver()
     */
    void run();

    /**
     * Finaliza el bucle principal del juego, haciendo que acabe
     */
    public void GameOver();
}
