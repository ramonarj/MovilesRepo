package es.ucm.fdi.moviles.engine.graphics;

import es.ucm.fdi.moviles.engine.utils.Rect;

/**
 * Motor gráfico; esta interfaz define métodos para crear y pintar imágenes,
 * así como para lidiar con las dimensiones de la ventana (tanato física como lógica)
 */
public interface Graphics
{
    /**
     * Crea una nueva imagen con la ruta dada
     * @param path ruta relativa a la imagen desde la carpeta raíz
     * @return
     */
    Image newImage(String path);

    /**
     * Limpia la pantalla con el color proporcionado
     * @param color Color usado para pintar la pantalla
     */
    void clear(int color);


    /**
     * Pinta una imagen en un rectángulo dado usando una transparencia
     * @param image imagen a pintar
     * @param destRect rectángulo destino
     * @param alpha transparencia a aplicarle
     */
    void drawImage(Image image, Rect destRect, float alpha);

    /**
     * Pinta una imagen en un rectángulo dado
     * @param image imagen a pintar
     * @param destRect rectángulo destino
     */
    void drawImage(Image image, Rect destRect);

    /**
     * Pinta una porción de la imagen en un rectángulo dado usando una transparencia
     * @param image imagen a pintar
     * @param srcRect rectángulo origen de la imagen
     * @param destRect rectángulo destino en la pantalla
     * @param alpha transparencia a aplicarle
     */
    void drawImage(Image image, Rect srcRect, Rect destRect, float alpha);

    /**
     * Pinta una porción de la imagen en un rectángulo dado
     * @param image imagen a pintar
     * @param srcRect rectángulo origen de la imagen
     * @param destRect rectángulo destino en la pantalla
     */
    void drawImage(Image image, Rect srcRect, Rect destRect);

    /**
     * Devuelve el ancho lógico
     * @return altura lógica del estado
     */
    int getWidth();

    /**
     * Devuelve el ancho lógico
     * @return ancho logico del estado
     */
    int getHeight();

    /**
     * Devuelve el ancho físico
     * @return ancho física de la ventana
     */
    int getWindowWidth();

    /**
     * Devuelve la altura física
     * @return altura física de la ventana
     */
    int getWindowHeight();


    /**
     * Establece las dimensiones lógicas
     * @param width anchura lógica
     * @param height altura lógica
     */
    void setLogicalView(int width, int height);

    /**
     * Establece el tamaño físico de la ventana
     * Nótese que este método deberá llamarse una vez al inicio,
     * y luego únicamente en casos de reescalado de la ventana.
     * @param width anchra física
     * @param height altura física
     */
    void setCanvasSize(int width,int height);
}
