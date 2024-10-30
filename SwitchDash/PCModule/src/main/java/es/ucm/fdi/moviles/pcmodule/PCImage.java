package es.ucm.fdi.moviles.pcmodule;

import es.ucm.fdi.moviles.engine.graphics.Image;

/**
 * Clase que implementa la interfaz Image específicamente para PC.
 * Contiene una imagen de Java
 */
public class PCImage implements Image
{
    //Imagen bruta de Java (para que no colisione el namespace)
    java.awt.Image img;

    /**
     * Constructora
     * @param img imagen de Java ya creada
     */
    public PCImage(java.awt.Image img)
    {
        this.img = img;
    }


    @Override
    public int getWidth()
    {
        return img.getWidth(null);
    }

    @Override
    public int getHeight()
    {
        return img.getHeight(null);
    }
}
