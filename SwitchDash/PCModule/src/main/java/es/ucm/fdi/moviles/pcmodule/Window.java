package es.ucm.fdi.moviles.pcmodule;

import javax.swing.JFrame;

/**
 * Clase que hereda de JFrame y contiene lo necesario para pintar cosas en Java
 * Sacado en gran mayoría de los apuntes del profesor
 */
public class Window extends JFrame
{
    /**
     * Constructor.
     *
     * @param title Texto que se utilizará como título de la ventana
     * que se creará.
     */
    public Window(String title) {
        super(title);
    }


    /**
     * Realiza la inicialización del objeto (inicialización en dos pasos).
     * Se configura el tamaño de la ventana, se habilita el cierre de la
     * aplicación al cerrar la ventana, y se carga la imagen que se mostrará
     * en la ventana.
     *
     * Debe ser llamado antes de mostrar la ventana (con setVisible()).
     *
     * @return Cierto si tod0 fue bien y falso en otro caso (se escribe una
     * descripción del problema en la salida de error).
     */
    public boolean init(int width, int height, boolean fullscreen, int numBuffers)
    {
        setSize(width,height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Pantalla completa
        if(fullscreen)
        {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);
            setVisible(true);
        }


        //Añadido
        setIgnoreRepaint(true);
        setVisible(true);

        // Intentamos crear el buffer strategy con 2 buffers.
        int intentos = 100;
        while(intentos-- > 0) {
            try {
                createBufferStrategy(numBuffers);
                break;
            }
            catch(Exception e) {
                System.out.println(e.getMessage());

            }
        } // while pidiendo la creación de la buffeStrategy
        if (intentos == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return false;
        }
        return true;
    }
}
