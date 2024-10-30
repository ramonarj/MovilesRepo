package es.ucm.fdi.moviles.androidModule;

import android.view.MotionEvent;
import android.view.View;

import es.ucm.fdi.moviles.engine.input.AbstractInput;
import es.ucm.fdi.moviles.engine.input.Input;

/**
 * Implementa la clase Input para Android valiéndose de la clase AbstractInput, de la
 * que hereda. Implementa onTouchListener para recibir eventos de input sobre la View
 */
public class AndroidInput extends AbstractInput implements View.OnTouchListener{

    /**
     * Constructora por defecto
     */
    public AndroidInput()
    {

    }


    /**
     * Recibe un evento que ha sido registrado en la vista
     * @param v Vista en la que está ocurriendo el evento
     * @param event Evento a registrar
     * @return true si el evento ha sido consumido y noo queremos que nadie más lo use
     */
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        //Multitouch ( puede haber varias pulsaciones a la vez)
        final int pointerCount = event.getPointerCount();
        for (int p = 0; p < pointerCount; p++)
        {
            //Un evento nuestro por cada uno de los punteros registrados
            Input.TouchEvent evt = new TouchEvent();
            switch (event.getAction()) //Tipo del evento
            {
                case MotionEvent.ACTION_DOWN:
                    evt.type = TouchEvent.EventType.PRESSED;
                    break;
                case MotionEvent.ACTION_MOVE:
                    evt.type = TouchEvent.EventType.MOVED;
                    break;
                case MotionEvent.ACTION_UP:
                    evt.type = TouchEvent.EventType.RELEASED;
                    break;
            }

            //Rellenamos el resto de datos del evento
            evt.x = (int)event.getX();
            evt.y = (int)event.getY();
            evt.id = event.getPointerId(p);

            //Lo añadimos a la lista
            addEvent(evt);
        }
        return true; //El evento se consume siempre
    }
}
