
package es.ucm.fdi.moviles.engine.utils;

/**
 * Clase usada para representar un rectángulo
 */
public class Rect {

    //Coordenadas de la esquina superior izquierda del rectángulo
    private int x1;
    private int x2;
    private int y1;
    private int y2;

    //Anchura y altura del rectángulo
    private int w;
    private int h;

    /**
     * Constructora que recibe una esquina y unas dimensiones
     * @param x Coordenada x de la esquina superior izquierda
     * @param y Coordenada y de la esquina superior izquierda
     * @param width Anchura del rectángulo
     * @param height Altura del rectángulo
     */
    public Rect(int x, int y, int width, int height)
    {
        this.x1 = x;
        this.y1 = y;
        this.x2 = x + width;
        this.y2 = y + height;

        //Width and height
        this.w = width;
        this.h = height;
    }

    /**
     * Getter
     * @return anchura del rectángulo
     */
    public int getWidth() {return w;};


    /**
     * Getter
     * @return altura del rectángulo
     */
    public int getHeight() {return h;};

    /**
     * Getter
     * @return coordenada horizontal de la esquina superior izquierda
     */
    public int x1() {return x1;};

    /**
     * Getter
     * @return coordenada horizontal de la esquina inferior derecha
     */
    public int x2() {return x2;};

    /**
     * Getter
     * @return coordenada vertical de la esquina superior izquierda
     */
    public int y1() {return y1;};

    /**
     * Getter
     * @return coordenada vertical de la esquina inferior derecha
     */
    public int y2() {return y2;};


    /**
     * Mueve el rectángulo al sitio solicitado, manteniendo las dimensiones
     * @param newX1 coordenanda horizontal de la nueva esquina superior izquierda
     * @param newY1 coordenanda vertical de la nueva esquina superior izquierda
     */
    public void moveTo(int newX1, int newY1)
    {
        this.x1 = newX1;
        this.x2 = x1 + w;
        this.y1 = newY1;
        this.y2 = y1 + h;
    }

    /**
     * Modifica el tamaño del rectángulo, dejando inmutable su esquina inferior izquierda
     * @param newWidth nueva anchura del rectángulo
     * @param newHeight nueva altura del rectángulo
     */
    public void resize(int newWidth, int newHeight)
    {
        this.x2 = x1 + newWidth;
        this.y2 = y1+ newHeight;
        this.w = newWidth;
        this.h = newHeight;
    }
}
