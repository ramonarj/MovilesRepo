package es.ucm.fdi.moviles.androidModule;

import android.graphics.Canvas;
import android.os.Build;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import es.ucm.fdi.moviles.engine.system.Game;
import es.ucm.fdi.moviles.engine.system.GameState;
import es.ucm.fdi.moviles.engine.graphics.Graphics;
import es.ucm.fdi.moviles.engine.input.Input;

/**
 * Clase que implementa la interfaz de juego para la plataforma Android.
 * Además, hereda de SurfaceView para actuar como ventana e implementa
 * Runnable para el control del Thread del juego
 */
public class AndroidGame extends SurfaceView implements Runnable , Game {

    /**
     * Constructora del AndroidGame que recibe la alctividad de Android.
     * Ademas,crea la instacia de graphics e input
     * @param Activity actividad de Android
     */
    public  AndroidGame(AppCompatActivity Activity)
    {
        super(Activity);
        this.activity_=Activity;
        this.graphic_=new AndroidGraphics(activity_);
        this.input_=new AndroidInput();

        setOnTouchListener(this.input_);
        this.input_.init(this);
    }

    @Override
    public Graphics getGraphics() {
        return graphic_;
    }

    @Override
    public Input getInput() {
        return input_;
    }

    @Override
    public void setGameState(GameState state) {
        state_=state;
        state_.init();
    }

    @Override
    public void GameOver(){
        running_=false;
    }

    @Override
    public void run() {
        long lastFrameTime = System.nanoTime();

        long informePrevio = lastFrameTime; // Informes de FPS
        int frames = 0;

        while (running_) {

            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            float elapsedTime = (float)(nanoElapsedTime / 1.0E9);
            state_.update(elapsedTime);

            //Bloqueamos el canvas, se lo pasamos al graphics y pintamos antes de desbloquearlo
            Canvas canvas = lockCanvas();
            graphic_.setCanvas(canvas);
            state_.render();
            unLockCanvas(canvas);

            //Contador de Frames
            // Informe de FPS
            if (currentTime - informePrevio > 1000000000l) {
                long fps = frames * 1000000000l / (currentTime - informePrevio);
                System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }
            ++frames;
        }
    }

    /**
     * Metodo que espera a que la hebra termine y pone la condicion del run a false
     */
    public void pause() {

        if (running_) {
            running_ = false;
            while (true) {
                try {
                    gameThread.join();
                    gameThread = null;
                    break;
                } catch (InterruptedException ie) {
                    // Esto no debería ocurrir nunca...
                }
            } // while(true)
        } // if (_running)

    } // pause

    /**
     * Se llamara una vez al iniciar la app y cada vez que giremos el movil para crear nuestra hebra de juego
     */
    public void onResume()
    {
        if (!running_) {
            running_ = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    /**
     * Bloquea el canvas para poder usarlo para renderizar
     */
    private Canvas lockCanvas()
    {
        while(!getHolder().getSurface().isValid());

        //Ejecución condicional (solo usamos un canvas acelerado si estamos en un API >=23,
        //que es el que lo soporta
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return getHolder().getSurface().lockHardwareCanvas();
        else
            return getHolder().lockCanvas();
    }

    /**
     * libera el canvas de una manera u otra dependiendo de la version del sdk
     * @param canvas
     */
    private void unLockCanvas(Canvas canvas)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            getHolder().getSurface().unlockCanvasAndPost(canvas);
        else
            getHolder().unlockCanvasAndPost(canvas);
    }


    private GameState state_;
    private AndroidGraphics graphic_;
    private AndroidInput input_;
    private AppCompatActivity activity_;
    boolean running_=false;
    Thread gameThread;
}
