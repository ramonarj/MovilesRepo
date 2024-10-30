package es.ucm.fdi.moviles.engine.system;

/**
 * Estado de juego ejecutado el objeto Game, el cual llama consecutivamente
 * a sus métodos update y render
 */
public interface GameState {

    /**
     * Inicializa el estado de juego con los valores necesarios
     * @return
     */
    public boolean init();

    /**
     * Actualiza la lógica del estado actual y se encarga de manejar el input
     * @param deltaTime tiempo trascurrido desde el frame anterior
     */
    public void update(float deltaTime);

    /**
     * Pinta el estado actual del juego usando los gráficos de este
     */
    public void render();
}
