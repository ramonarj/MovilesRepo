package es.ucm.fdi.moviles.engine.utils;

import es.ucm.fdi.moviles.engine.graphics.Image;
import es.ucm.fdi.moviles.engine.system.ResourceMan;

/**
 * Clase que representa una partícula
 */
public class Particle {

    /**
     * Establece todos los elementos necesarios de una particula
     * @param posX posicion X
     * @param posY posicion Y
     * @param velX velocidad X
     * @param velY velocidad Y
     * @param width anchura
     * @param height altura
     * @param alpha tranparencia
     * @param color color de la particula
     */
    public Particle(int posX,int posY, int velX,int velY,int width, int height,float alpha,int color)
    {
        this.im_= ResourceMan.getImage("Balls");
        this.posX=posX;
        this.posY=posY;
        this.velX=velX;
        this.velY=velY;
        this.width=im_.getWidth() / (10*width);
        this.height=im_.getHeight()/(2*height);
        this.height=height;
        this.alpha=alpha;
        this.color=color;
        this.randomScale=(float)(Math.floor(Math.random()*4)+1)/4;

    }

    /**
     * Actualizamos la posicion X e Y de la particula acorde a su velocidad y deltaTime
     * @param deltaTime deltaTime usado para actualizar posiciones
     */
    public void update(float deltaTime)
    {
        posX+=velX*deltaTime;
        posY+=velY*deltaTime;
        alpha-=(float)(Math.floor(Math.random() * 220)/255)*deltaTime;
    }

    public int getX(){return posX; }
    public int getY(){return posY; }
    public float getAlpha(){return  alpha;}
    public int getWidth(){return  width;}
    public int getheight(){return height;}
    public Image getImage(){return im_;}
    public int getColor(){return  color;}
    public float getRandomScale(){return randomScale;}


    private Image im_;
    private int posX;
    private int posY;
    private int velX;
    private int velY;
    private int width;
    private int height;
    private float alpha;
    private int color;
    private float randomScale;
}
