package es.ucm.fdi.moviles.engine.utils;

import es.ucm.fdi.moviles.engine.graphics.Sprite;

/**
 * Clase auxiliar que representa un botón. Contiene métodos para pintarse
 * y para saber si está pulsado, permitiendo tener 2 sprites entre los que cambia
 * en caso de que se quiera
 */
public class Button
{
    //Posición que ocupa en la pantalla
    private Rect rect;

    //Sprite utilizado
    private Sprite sprite;
    //Segundo sprite utilizado (no obligatorio)
    private Sprite spriteToggle;

    //Indica si se está usando el 2º sprite en este momento
    boolean toggled;

    //Nombre del botón
    private String name;

    /**
     * Constructora con rectángulo destino
     * @param sprite sprite que usará el botón
     * @param rect posición donde se pintará
     * @param name nombre del botón
     */
    public Button(Sprite sprite, Rect rect, String name)
    {
        this.sprite = sprite;
        this.rect = rect;
        this.name = name;
        this.spriteToggle = null;
        this.toggled = false;
    }

    /**
     * Constructora con posición (el botón se pintará centrado en esa posición)
     * @param sprite sprite que usará el botón
     * @param X posición X se pintará
     * @param name nombre del botón
     */
    public Button(Sprite sprite, int X, int Y, String name)
    {
        int width = sprite.getWidth();
        int height = sprite.getHeight();

        this.sprite = sprite;
        this.rect = new Rect(X - width / 2, Y - height / 2, width, height);
        this.name = name;
        this.spriteToggle = null;
        this.toggled = false;
    }

    /**
     * Constructora con posición (el botón se pintará centrado en esa posición)
     * @param sprite sprite que usará el botón
     * @param X posición X se pintará
     * @param name nombre del botón
     */
    public Button(Sprite sprite,Sprite toggle, int X, int Y, String name)
    {
        int width = sprite.getWidth();
        int height = sprite.getHeight();

        this.sprite = sprite;
        this.spriteToggle = toggle;
        this.rect = new Rect(X - width / 2, Y - height / 2, width, height);
        this.name = name;
        this.toggled = false;
    }


    /**
     * Dibuja el botón en su posición
     */
    public void draw()
    {
        if(toggled)
            spriteToggle.draw(rect);
        else
            sprite.draw(rect);
    }

    /**
     * Indica si el las coordenadas proporcionadas pertenecen al rectángulo del botón
     * @param x coordenada horizontal
     * @param y coordenada vertical
     * @return true si está siendo pulsado
     */
    public boolean isPressed(int x, int y)
    {
        return (x >= rect.x1() && x <= rect.x2() && y >= rect.y1() && y <= rect.y2());
    }

    /**
     * Devuelve el nombre del botón
     * @return el nombre del botón
     */
    public String getName(){return this.name;}

    /**
     * Cambia entre los 2 sprites del botón.
     * Si se llama en un botón que no ha sido creado con 2 sprites,
     * no tendrá ningún efecto
     */
    public void toggleSprite()
    {
        if(spriteToggle != null)
            toggled = !toggled;
    }
}
