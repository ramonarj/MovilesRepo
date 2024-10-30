package es.ucm.fdi.moviles.pcboot;

import es.ucm.fdi.moviles.logic.LoadState;

import es.ucm.fdi.moviles.engine.system.ResourceMan;
import es.ucm.fdi.moviles.pcmodule.PCGame;
import es.ucm.fdi.moviles.pcmodule.Window;

/**
 * Clase que arranca el juego para PC. Usa una Window (que en Android no es necesaria)
 * y usa las clases del motor específicas para PC
 */
public class SwitchDash
{
    public static void main(String[]args)
    {
        //1. CREAMOS LA VENTANA
        Window ventana = new Window("Switch Dash");
        if (!ventana.init(400, 600, false, 4))
            return;

        //2. CREAMOS EL JUEGO CON LA VENTANA Y UNAS DIMENSIONES LÓGICAS
        PCGame game = new PCGame();
        game.init(ventana, 1080, 1920);

        //3. INICIALIZAR EL RESOURCEMANAGER
        ResourceMan.initInstance(game);

        //4. EL JUEGO Y LA LÓGICA TIENEN REFERENCIAS MUTUAS
        LoadState loadState = new LoadState(game);
        game.setGameState(loadState);

        //5. EJECUTAR EL JUEGO
        game.run();
    }
}