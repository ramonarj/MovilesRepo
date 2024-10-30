package es.ucm.fdi.moviles.engine.input;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.moviles.engine.system.Game;
import es.ucm.fdi.moviles.engine.utils.Point;
import es.ucm.fdi.moviles.engine.graphics.AbstractGraphics;

/**
 * Implementa las funcionalidades comunes de la clase Input para cualquier plataforma,
 * lidiando con los problemas que tiene el hecho de acceder a la lista de eventos registrados
 * desde distintas hebras, por lo que usa el semáforo de Java para sincronizar los accesos
 */
public abstract class AbstractInput implements Input
{
    //Referencia al juego
    protected Game game;

    //Lista de eventos registrados que no han sido solicitados todavía
    protected ArrayList<TouchEvent> events;

    /**
     * Constructora
     * Crea la lista de eventos
     */
    public AbstractInput()
    {
        events = new ArrayList<TouchEvent>();
    }

    /**
     * Inicializa el input recibiendo una referencia al juego
     * @param game juego al que corresponde
     * @return false si el juego no ha sido creado, true e.o.c
     */
    public boolean init(Game game)
    {
        this.game = game;
        return (this.game != null);
    }


    @Override
    synchronized public List<TouchEvent> getTouchEvents()
    {
        if(events.size() > 0)
        {
            //Creamos una nueva lista que sea copia de "events"
            ArrayList<TouchEvent> aux =  (ArrayList<TouchEvent>) events.clone();

            //Vaciamos "events" para la próxsima invocación
            events.clear();
            return aux;
        }
        return events;
    }

    /**
     * Añade un evento a la lista, usa el synchronized porque es posible que tratemos de acceder
     * a la vez que otro método (getTouchEvents), así que pedimos permiso al semáforo del objeto.
     * @param evt El evento a añadir a la cola
     */
    protected void addEvent(TouchEvent evt)
    {
        if(game.getGraphics() != null)
        {
            Point logicalPoint = ((AbstractGraphics) game.getGraphics()).physicalToLogical(new Point(evt.x, evt.y));
            evt.x = logicalPoint.getX();
            evt.y = logicalPoint.getY();
            //System.out.println("Evento de tipo " + evt.type.toString() + " en {" + evt.x + ", " + evt.y + "} con ID=" + evt.id);

            //Solo necesitamos que esta línea esté protegida
            synchronized (this){ events.add(evt);}
        }
    }
}
