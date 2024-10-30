package es.ucm.fdi.moviles.pcmodule;

import java.awt.image.BufferStrategy;

import es.ucm.fdi.moviles.engine.system.Game;
import es.ucm.fdi.moviles.engine.system.GameState;
import es.ucm.fdi.moviles.engine.graphics.Graphics;
import es.ucm.fdi.moviles.engine.input.Input;

/**
 * Clase que implementa la interfaz Game para la plataforma de PC.
 */
public class PCGame implements Game {

    //Sistema de input
    PCInput input;
    //Sistema de gráficos
    PCGraphics graphics;
    //Ventana
    es.ucm.fdi.moviles.pcmodule.Window window;

    //Estado actual
    GameState state;

    boolean running_;


    /**
     * Constructora por defecto
     */
    public PCGame()
    {

    }

    @Override
    public void run()
    {
        BufferStrategy strategy = window.getBufferStrategy();
        long lastFrameTime = System.nanoTime();
        while(running_)
        {
            //Calcular el deltaTime
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;

            //Update
            state.update((float)elapsedTime);

            // Pintamos el frame con el BufferStrategy
            do {
                do {
                    java.awt.Graphics g = window.getBufferStrategy().getDrawGraphics();
                    try {
                        state.render();
                    }
                    finally {
                        g.dispose();
                    }
                } while(strategy.contentsRestored());
                strategy.show();
            } while(strategy.contentsLost());
        }
    }

    @Override
    public void GameOver(){
        running_=false;
    }


    /**
     * Inicializa el juego para PC, recibiendo la ventana ya creada y unas dimensiones
     * lógicas para que usen los Gráficos
     * @param window ventana de la aplicación ya inicializada
     * @param logicalWidth anchura lógica
     * @param logicalHeight altura lógica
     * @return true si tod0 ha salido correcto, false e.0.c
     */
    public boolean init(es.ucm.fdi.moviles.pcmodule.Window window, int logicalWidth, int logicalHeight)
    {
        this.window = window;

        //Subsistema de input (lo registramos como listener de la ventana)
        this.input = new PCInput();
        window.addMouseListener(input);
        window.addMouseMotionListener(input);
        window.addKeyListener(input);
        this.input.init(this);

        //El graphics lo creamos referenciando la ventana
        this.graphics = new PCGraphics(window, logicalWidth, logicalHeight);
        window.addComponentListener(graphics);

        //Establecemos vision vertical como predeterminado
        this.graphics.setCanvasSize(window.getWidth() ,window.getHeight());
        running_ = true;

        return true;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public void setGameState(GameState state)
    {
        this.state = state;
        state.init();
    }
}
