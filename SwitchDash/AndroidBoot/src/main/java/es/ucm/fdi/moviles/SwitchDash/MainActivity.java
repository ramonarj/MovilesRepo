package es.ucm.fdi.moviles.SwitchDash;

import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import es.ucm.fdi.moviles.logic.LoadState;
import es.ucm.fdi.moviles.androidModule.AndroidGame;
import es.ucm.fdi.moviles.engine.system.ResourceMan;


/**
 * Clase que arranca el juego en Android y que usa todas las funcionalidades
 * del ciclo de vida de las aplicaciones en Android heredando de
 * AppCompatActivity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Callback llamado cuando se crea la actividad
     * @param savedInstanceState datos sobre la instancia de la actividad que murió
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Para la pantalla completa y que no se vea el logo ni los botones de abajo
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
       // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //Constructora padre
        super.onCreate(savedInstanceState);

        //Creamos el juego y estado actual
        game=new AndroidGame(this);
        LoadState loadState=new LoadState(game);
        init();

        //Inicializar Resouce Manager
        ResourceMan.initInstance(game);

        //Cargar el estado inicial
        game.setGameState(loadState);

        setContentView(game);
    }

    /**
     * Callback para el cambio de resolución (girar el móvil)
     * @param newConfig nueva configuración de pantalla adoptada
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Point size=new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        game.getGraphics().setCanvasSize(size.x ,size.y);
    }

    /**
     * Llama tanto al resume de la superclase como al
     * resume del game
     */
    @Override
    protected void onResume() {

        super.onResume();
        game.onResume();
    }

    /**
     * LLama al Pause de la superclase y al pause del Game
     */
    @Override
    protected void onPause() {

        // Avisamos a la vista (que es la encargada del active render)
        // de lo que está pasando.
        super.onPause();
        game.pause();
    }

    /**
     * Establece tanto el tamaño logico de la pantalla como el
     * tamaño fisico del dispositivo
     */
    protected void init()
    {
        //Establecemos vision vertical como predeterminado
        game.getGraphics().setLogicalView(1080, 1920);
        Point size=new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        game.getGraphics().setCanvasSize(size.x ,size.y);
    }

    //Referencia al juego
    private AndroidGame game;
}
