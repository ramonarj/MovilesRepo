package es.ucm.fdi.moviles.androidModule;

import es.ucm.fdi.moviles.engine.graphics.AbstractGraphics;
import es.ucm.fdi.moviles.engine.graphics.Image;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;

import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Implementa la interfaz Graphics para la plataforma de Android
 * ayudándose de la clase AbtractGraphics, de la que hereda.
 */
public class AndroidGraphics extends AbstractGraphics
{
    //Actividad de Android
    AppCompatActivity activity;
    Canvas canvas;

    /**
     * Constructora
     * @param activity la actividad en que usaremos los gráficos
     */
    public AndroidGraphics(AppCompatActivity activity)
    {
        this.activity = activity;
        canvas=null;
    }

    /**
     * Crea una imagen a partir de la ruta especificada y la devuelve
     * @param path ruta relativa hasta la imagen desde la carpeta raíz
     * @return imagen que acaba de ser creada
     */
    public Image newImage(String path)
    {
        InputStream inputStream = null;
        AndroidImage image = null;
        try {
            AssetManager assetManager = activity.getAssets();
            inputStream = assetManager.open(path);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            image = new AndroidImage(bitmap);
        }
        catch (IOException io) {
            Log.e("MainActivity", "Error lectura");
        }
        finally {
            try
            {
                inputStream.close();
            } catch (Exception io) {
            }
        }
        return image;
    }


    @Override
    public void clear(int color) {
        //Bloqueamos el canavas para limpiar la pantalla e inmediatamente
        //despues lo liberamos
        canvas.drawColor(color);
    }

    @Override
    public void drawRealImage(Image image, es.ucm.fdi.moviles.engine.utils.Rect destRect, float alpha) {

        Paint paint=new Paint();
        paint.setAlpha((int)(alpha*255));

        Rect src=new Rect(0,0,image.getWidth(),image.getHeight());
        Rect dest=new Rect(destRect.x1(),destRect.y1(),destRect.x2(),destRect.y2());

        canvas.drawBitmap(((AndroidImage) image).sprite,src,dest,paint);
    }

    @Override
    public void drawRealImage(Image image, es.ucm.fdi.moviles.engine.utils.Rect destRect) {

        Rect src=new Rect(0,0,image.getWidth(),image.getHeight());
        Rect dest=new Rect(destRect.x1(),destRect.y1(),destRect.x2(),destRect.y2());

        canvas.drawBitmap(((AndroidImage) image).sprite,src,dest,null);
    }

    @Override
    public void drawRealImage(Image image, es.ucm.fdi.moviles.engine.utils.Rect srcRect, es.ucm.fdi.moviles.engine.utils.Rect destRect, float alpha) {

        Paint paint=new Paint();
        paint.setAlpha((int)(alpha*255));

        Rect src=new Rect(srcRect.x1(),srcRect.y1(),srcRect.x2(),srcRect.y2());
        Rect dest=new Rect(destRect.x1(),destRect.y1(),destRect.x2(),destRect.y2());

        canvas.drawBitmap(((AndroidImage) image).sprite,src,dest,paint);

    }

    @Override
    public void drawRealImage(Image image, es.ucm.fdi.moviles.engine.utils.Rect srcRect, es.ucm.fdi.moviles.engine.utils.Rect destRect) {

        Rect src=new Rect(srcRect.x1(),srcRect.y1(),srcRect.x2(),srcRect.y2());
        Rect dest=new Rect(destRect.x1(),destRect.y1(),destRect.x2(),destRect.y2());

        canvas.drawBitmap(((AndroidImage) image).sprite,src,dest,null);
    }

    public void setCanvas(Canvas canvas_)
    {
        this.canvas=canvas_;
    }

}
