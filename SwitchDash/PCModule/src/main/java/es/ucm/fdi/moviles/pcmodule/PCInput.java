package es.ucm.fdi.moviles.pcmodule;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import es.ucm.fdi.moviles.engine.input.AbstractInput;

/**
 * Implementa la clase Input para PC
 * Nótese que implementamos MouseListener (para la pulsación y liberación)
 * pero también MouseMotionListener (para el movimiento del ratón)
 * El resto de eventos está porque se obliga a sobrecargarlos pero no se usan
 */
public class PCInput extends AbstractInput implements MouseListener, MouseMotionListener, KeyListener
{

    /**
     * Constructora por defecto
     */
    public PCInput()
    {

    }

    /**
     * Dados un evento recibido de Mouse(motion)Listener y un tipo propio
     * de la clase input, crea el evento y lo añade a la lista
     * @param mouseEvent
     * @param type
     */
    private void registerEvent(MouseEvent mouseEvent, TouchEvent.EventType type)
    {
        //Rellenamos el evento
        TouchEvent evt = new TouchEvent();
        evt.x = mouseEvent.getX();
        evt.y = mouseEvent.getY();
        evt.id = mouseEvent.getID();
        evt.type = type;

        addEvent(evt);
    }

    /**
     * Dados un evento recibido de Mouse(motion)Listener y un tipo propio
     * de la clase input, crea el evento y lo añade a la lista
     * @param keyEvent
     * @param type
     */
    private void registerEvent(KeyEvent keyEvent, TouchEvent.EventType type)
    {
        //Rellenamos el evento
        TouchEvent evt = new TouchEvent();
        evt.x = 0;
        evt.y = 0;
        evt.id = keyEvent.getID();
        evt.type = type;

        addEvent(evt);
    }


    //MÉTODOS QUE IMPLEMENTAN LA INTERFAZ MOUSE LISTENER
    @Override
    public void mousePressed(MouseEvent mouseEvent)
    {
        registerEvent(mouseEvent, TouchEvent.EventType.PRESSED);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent)
    {
        registerEvent(mouseEvent, TouchEvent.EventType.RELEASED);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) { }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) { }

    @Override
    public void mouseExited(MouseEvent mouseEvent) { }

    //DE MOUSE MOTION LISTENER
    @Override
    public void mouseMoved(MouseEvent mouseEvent)
    {
        registerEvent(mouseEvent, TouchEvent.EventType.MOVED);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) { }

    //DE KEYLISTENER
    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
        //Pulsar el espacio equivale a hacer click
        if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE)
            registerEvent(keyEvent, TouchEvent.EventType.PRESSED);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) { }

    @Override
    public void keyTyped(KeyEvent keyEvent) { }
}
