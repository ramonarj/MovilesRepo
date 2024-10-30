package es.ucm.fdi.moviles.engine.graphics;

import es.ucm.fdi.moviles.engine.utils.Point;
import es.ucm.fdi.moviles.engine.utils.Rect;

/**
 * Clase intermedia entre la interfaz y las implementaciones de cada plataforma
 * Esta clase proporciona reescalado para pasar de coordenadas
 * lógicas a coordenadas reales, dejando a las clases que hereden de ella
 * únicamente de implementar los métodos "drawRealImage", ya en coordenadas reales del canvas
 */
public abstract class AbstractGraphics implements  Graphics {

    @Override
    public void drawImage(Image image, Rect destRect, float alpha)
    {
        Rect srcRect=new Rect(0,0,image.getWidth(),image.getHeight());
        Rect newRect = realDestRect(destRect);
        drawRealImage(image,srcRect,newRect,alpha);
    }


    @Override
    public void drawImage(Image image, Rect destRect)
    {
        Rect srcRect=new Rect(0,0,image.getWidth(),image.getHeight());
        Rect newRect = realDestRect(destRect);
        drawRealImage(image,srcRect,newRect);
    }

    @Override
    public void drawImage(Image image, Rect srcRect, Rect destRect, float alpha) {

        Rect newRect = realDestRect(destRect);
        drawRealImage(image,srcRect,newRect,alpha);
    }


    @Override
    public void drawImage(Image image, Rect srcRect, Rect destRect)
    {
        Rect newRect = realDestRect(destRect);
        drawRealImage(image,srcRect,newRect);
    }


    /**
     * Calcula el rectángulo de destino real, i.e, recibe el rectángulo en posiciones lógicas
     * y calcula las posiciones reales del canvas en las que se tendrá que dibujar
     * @param destRect rectángulo en coordenadas lógicas
     * @return rectángulo en coordenadas reales
     */
    private Rect realDestRect(Rect destRect)
    {
        //Sigfinica que estamos con la pantalla apaisada
        float factor=takeScaleFactor();
        int newx1=(int)(windowWidth/2-((logicalWidth*factor)/2));
        int newy1=(int)(windowHeight/2-((logicalHeight*factor)/2));

        //Calculamos la esquina superior izquierda y dimensiones del rectángulo reescalado
        int x1=(int)(newx1+(destRect.x1()*factor));
        int y1=(int)(newy1+destRect.y1()*factor);
        int width=(int)(windowWidth/2-((logicalWidth*factor)/2)+destRect.x2()*factor)- x1;
        int height=(int)(windowHeight/2-((logicalHeight*factor)/2)+destRect.y2()*factor) - y1;

        //Lo devolvemos
        return new Rect(x1, y1, width, height);
    }

    /**
     * Reescala un punto en coordenadas fisicas a logicas,
     * para asbtraer a la logica del tamaño del dispositivo
     * @param physical
     * @return
     */
    public Point physicalToLogical(Point physical)
    {
        float factor = takeScaleFactor();
        int newx1=(int)(logicalWidth/2-((windowWidth/factor)/2));
        int newy1=(int)(logicalHeight/2-((windowHeight/factor)/2));
        return new Point((int)(newx1 + (float)physical.getX() / factor), (int)( newy1 + (float)physical.getY() / factor));
    }


    @Override
    public void setLogicalView(int width, int height)
    {
        logicalWidth=width;
        logicalHeight=height;
    }

    @Override
    public void setCanvasSize(int width,int height)
    {
        windowWidth=width;
        windowHeight=height;
    }


    /**
     * Calcula el factor de escala por el que multiplicar/dividir
     * al hacer las conversiones físicas y lógicas, usando las dimensiones
     * reales y las de la lógica de juego especificadas
     * @return
     */
    private float takeScaleFactor()
    {
        float widthFactor = windowWidth / logicalWidth;
        float heigthFactor = windowHeight / logicalHeight;

        if (widthFactor < heigthFactor) return widthFactor;
        else return  heigthFactor;
    }

    //Dimensiones lógicas
    protected int logicalWidth;
    protected int logicalHeight;

    //Dimensiones físicas
    protected float windowWidth;
    protected float windowHeight;

    @Override
    public int getWidth() {
        return this.logicalWidth;
    }

    @Override
    public int getHeight() {
        return this.logicalHeight;
    }

    @Override
    public int getWindowWidth() {
        return (int)this.windowWidth;
    }

    @Override
    public int getWindowHeight() {
        return (int)this.windowHeight;
    }


    //Métodos que sobreescribiran las clases hijas haciendo que la imagen se pinta de una forma concreta
    //depediendo la plataforma

    /**
     * Mirar documentación de drawRealImage (Image image, Rect srcRect, Rect destRect, float alpha)
     */
    public abstract void drawRealImage(Image image, Rect destRect);

    /**
     * Mirar documentación de drawRealImage (Image image, Rect srcRect, Rect destRect, float alpha)
     */
    public abstract void drawRealImage(Image image, Rect destRect,float alpha);

    /**
     * Mirar documentación de drawRealImage (Image image, Rect srcRect, Rect destRect, float alpha)
     */
    public abstract void drawRealImage(Image image, Rect srcRect, Rect destRect);

    /**
     * Pinta una porción de la imagen dada en un rectángulo destino usando transparencia.
     * Este método y sus parientes son los que deben implementar las clases específicas de gráficos
     * para cada plataforma, teniendo en cuenta que las coordenadas del rectángulo destino
     * ya están reescaladas a coordenadas lógicas.
     * @param image imagen a pintar
     * @param srcRect rectángulo de la imagen a pintar
     * @param destRect rectángulo de la ventana lógica en el que pintamos
     * @param alpha
     */
    public abstract void drawRealImage(Image image, Rect srcRect, Rect destRect, float alpha);
}