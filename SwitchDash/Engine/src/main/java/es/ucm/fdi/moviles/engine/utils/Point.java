package es.ucm.fdi.moviles.engine.utils;

/**
 * Clase auxiliar para representar puntos 2D, con una coordenada X y una coordenada Y
 */
public class Point
{
    //Coordenadas
    private int x;
    private int y;

    /**
     * Constructora que recibe dos coordenadas donde se generara el punto
     * @param x
     * @param y
     */
    public Point(int x, int y)
    {
        this.x=x;
        this.y=y;
    }

    /**
     * Devuelve la coordenada X
     * @return la coordenada X
     */
    public int getX(){return this.x;}

    /**
     * Devuelve la coordenada X
     * @return la coordenada Y
     */
    public int getY(){return this.y;}
}
