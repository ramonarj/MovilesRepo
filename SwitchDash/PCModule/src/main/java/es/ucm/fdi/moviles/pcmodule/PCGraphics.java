package es.ucm.fdi.moviles.pcmodule;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import es.ucm.fdi.moviles.engine.graphics.AbstractGraphics;
import es.ucm.fdi.moviles.engine.graphics.Image;
import es.ucm.fdi.moviles.engine.utils.Rect;

/**
 * Implementa la interfaz Graphics para la plataforma de PC
 * ayudándose de la clase AbtractGraphics, de la que hereda.
 * Implementa ComponentListener para enterarse de los cambios en
 * la ventana de la aplicación
 */
public class PCGraphics extends AbstractGraphics implements ComponentListener
{
    //Ventana de la aplicación
    es.ucm.fdi.moviles.pcmodule.Window window;
    //Gráficos de Java
    java.awt.image.BufferStrategy strategy;

    /**
     * Constructora
     * @param window ventana de la aplicación (debe heredar de JFrame)
     * @param logicalWidth anchura lógica que tendrá el juego
     * @param logicalHeight altura lógica que tendrá el juego
     */
    public PCGraphics(es.ucm.fdi.moviles.pcmodule.Window window, int logicalWidth, int logicalHeight)
    {
        this.window = window;
        this.strategy = window.getBufferStrategy();
        setLogicalView(logicalWidth, logicalHeight);
    }

    @Override
    public Image newImage(String name)
    {
        name= "assets/" + name;
        PCImage image = null;

        try
        {
            java.awt.Image img = javax.imageio.ImageIO.read(new java.io.File(name));
            image = new PCImage(img);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }

        return image;
    }

    @Override
    public void clear(int color)
    {
        java.awt.Graphics g = strategy.getDrawGraphics();
        g.setColor(new Color(color));
        g.fillRect(0,0, getWindowWidth(), getWindowHeight());
    }

    @Override
    public void setCanvasSize(int width, int height)
    {
        windowWidth=width;
        windowHeight=height;
    }


    /**
     * Método responsable de dibujar una imagen para PC
     * Todos los métodos públicos "drawImage" terminan llamando a este
     * @param image imagen que se dibuja(de tipo PCImage)
     * @param srcRect rectángulo de la imagen que se pintará
     * @param destRect rectángulo de la pantalla en coordenadas lógicas donde se dibujará
     * @param alpha valor de la transparencia (0 = totalmente transparene, 1 = totalmente opaco)
     */
    protected void drawImagePrivate(PCImage image, Rect srcRect, Rect destRect, float alpha)
    {
        java.awt.Graphics g = strategy.getDrawGraphics();

        //No es necesario usar el compositor
        if(alpha == 1)
        {
            //Pintamos
            g.drawImage(image.img,
                    destRect.x1(), destRect.y1(), destRect.x2(), destRect.y2(),       //Rectángulo destino
                    srcRect.x1(), srcRect.y1(), srcRect.x2(), srcRect.y2(),           //Rectángulo fuente
                    null);
        }

        //Usamos el composite
        else
        {
            //Casting
            Graphics2D g2d = (Graphics2D)g;

            //Creamos el composite de alpha y lo aplicamos
            Composite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2d.setComposite(alphaComp);

            //Pintamos
            g.drawImage(image.img,
                    destRect.x1(), destRect.y1(), destRect.x2(), destRect.y2(),       //Rectángulo destino
                    srcRect.x1(), srcRect.y1(), srcRect.x2(), srcRect.y2(),           //Rectángulo fuente
                    null);

            //Dejamos como estaba
            Composite opaqueComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
            g2d.setComposite(alphaComp);
        }
    }

    @Override
    public void drawImage(Image image, Rect destRect, float alpha) {
        super.drawImage(image, destRect, alpha);
    }

    @Override
    public void drawRealImage(Image image, Rect destRect)
    {
        Rect srcRect = new Rect(0,0, image.getWidth(), image.getHeight());
        drawImagePrivate((PCImage)image, srcRect, destRect,1f);
    }

    @Override
    public void drawRealImage(Image image, Rect destRect, float alpha)
    {
        Rect srcRect = new Rect(0,0, image.getWidth(), image.getHeight());
        drawImagePrivate((PCImage)image, srcRect, destRect, alpha);
    }

    @Override
    public void drawRealImage(Image image, Rect srcRect, Rect destRect)
    {
        drawImagePrivate((PCImage)image, srcRect, destRect, 1f);
    }

    @Override
    public void drawRealImage(Image image, Rect srcRect, Rect destRect, float alpha)
    {
        drawImagePrivate((PCImage)image, srcRect, destRect, alpha);
    }

    @Override
    public void componentResized(ComponentEvent componentEvent)
    {
        setCanvasSize(window.getWidth(), window.getHeight());
    }

    //Callbacks de
    @Override
    public void componentMoved(ComponentEvent componentEvent) { }

    @Override
    public void componentShown(ComponentEvent componentEvent) { }

    @Override
    public void componentHidden(ComponentEvent componentEvent) { }
}
