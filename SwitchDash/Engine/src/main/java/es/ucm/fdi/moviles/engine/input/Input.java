package es.ucm.fdi.moviles.engine.input;

import java.util.List;

/**
 * Gestiona la entrada del juego, proporcionando una estructura "TouchEvent" para manejar los eventos
 * y definiendo un método para devolver los eventos actuales
 */
public interface Input
{
    /**
     * Representa la información de un toque sobre la pantalla
     */
    public static class TouchEvent
    {
        //Tipo enumerado para el tipo de evento
        public enum EventType {PRESSED, RELEASED, MOVED}

        //Tipo (pulsacion, liberacion, desplazamiento)
        public EventType type;

        //Posicion
        public int x;
        public int y;

        //Identificador del dedo/boton
        public int id;
    }

    /**
     * Devuelve la lista de eventos sucedidos desde la última vez que se llamó al
     * método
     * @return lista de eventos, que será de tamaño 0 en caso de que no haya habido ninguno
     */
    List<TouchEvent> getTouchEvents();
}
