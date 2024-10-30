package es.ucm.fdi.moviles.logic;

import java.util.ArrayList;

import es.ucm.fdi.moviles.engine.utils.Button;
import es.ucm.fdi.moviles.engine.system.Game;
import es.ucm.fdi.moviles.engine.system.GameState;
import es.ucm.fdi.moviles.engine.graphics.Graphics;
import es.ucm.fdi.moviles.engine.graphics.Image;
import es.ucm.fdi.moviles.engine.input.Input;
import es.ucm.fdi.moviles.engine.utils.Rect;
import es.ucm.fdi.moviles.engine.system.ResourceMan;
import es.ucm.fdi.moviles.engine.graphics.Sprite;

public class MenuState implements GameState {
    //Objeto del juego
    private Game game;

    public MenuState(Game game)
    {
        this.game = game;
    }

    @Override
    /**Inicializamos todas las varibales necesarias para la ejecucion del estado**/
    public boolean init()
    {
        Graphics g = game.getGraphics();

        //Pedimos imágenes al ResourceManager
        backgrounds = ResourceMan.getImage("Backgrounds");
        logo =        ResourceMan.getImage("Logo");
        tapToPlay =   ResourceMan.getImage("TapToPlay");
        buttons =     ResourceMan.getImage("Buttons");


        //Primera vez que pasamos por el menú
        if(GameManager.getInstance() == null)
        {
            GameManager.initInstance(game);

            coloresFlechas = new int[]{ 0xff41a85f, 0xff00a885, 0xff3d8eb9, 0xff2969b0,
                    0xff553982, 0xff28324e, 0xfff37934, 0xffd14b41, 0xff75706b };
            backgroundNo = (int)Math.floor(Math.random() * 9);
            barsWidth = g.getWidth() / 5;

            //Inicializar atributos
            GameManager.getInstance().setBarsWidth(barsWidth);
            GameManager.getInstance().setBackGroundNo(backgroundNo);
            GameManager.getInstance().setLateralColor(coloresFlechas[backgroundNo]);
            GameManager.getInstance().setArrowSprite(ResourceMan.getImage("Flechas"));
        }
        else
        {
            this.backgroundNo =GameManager.getInstance().getBackGroundNo();
            this.barsWidth = GameManager.getInstance().getBarsWidth();
        }


        int buttonWidth = buttons.getWidth() / 10;
        int buttonHeight = buttons.getHeight();

        //Botón de instrucciones
        Rect srcRect =new Rect(0,0, buttonWidth,buttonHeight);
        Sprite infoSprite=new Sprite(buttons,srcRect,g);
        instructionsButton = new Button(infoSprite, g.getWidth() - barsWidth / 2, 200, "Instrucciones");

        //Botón de sonido
        srcRect = new Rect(2* buttonWidth,0,  buttonWidth,buttonHeight);
        Sprite soundSprite=new Sprite(buttons,srcRect,g);
        srcRect = new Rect(3* buttonWidth,0,  buttonWidth,buttonHeight);
        Sprite soundSprite2=new Sprite(buttons,srcRect,g);

        soundButton = new Button(soundSprite, soundSprite2, barsWidth / 2,200, "Sonido");



        //Otros
        alphaTap=1f;
        velocidad =0.6f;
        return true;
    }


    /**
     * Unicamente comprobamos los eventos de Input para saber si actualizar los botones
     * o saltar al estado de juego
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime)
    {
        //Actualizar las flechas
        GameManager.getInstance().updateArrows(deltaTime);

        Input input = game.getInput();
        ArrayList<Input.TouchEvent> events = (ArrayList)input.getTouchEvents();
        for(Input.TouchEvent evt: events)
        {
            if(evt.type == Input.TouchEvent.EventType.PRESSED)
            {
                //Botón de instrucciones
                if(instructionsButton.isPressed(evt.x, evt.y))
                    game.setGameState(new InstructionsState(game));
                else if (soundButton.isPressed(evt.x, evt.y))
                    soundButton.toggleSprite();
                else
                    game.setGameState(new InstructionsState(game));

            }
        }

        alphaTap+=(deltaTime* velocidad);
        if(alphaTap>=1 || alphaTap<=0)
        {
            if(alphaTap<=0f)alphaTap=0;
            else if(alphaTap>=1f)alphaTap=1f;
            velocidad *=-1;
        }

    }

    /**
     * Pintamos todos los elementos del estado
     */
    @Override
    public void render()
    {
        //Color de fondo (para las barras laterales)
        Graphics g = game.getGraphics();

        //Fondo
        GameManager.getInstance().drawBackground();

        //Flechas
        GameManager.getInstance().drawArrows();

        //Logo
        Rect dstRect = new Rect(g.getWidth() / 3,356 ,g.getWidth() / 3,g.getHeight() / 6);
        g.drawImage(logo, dstRect,1f );

        //TapToPlay
        dstRect = new Rect(g.getWidth() / 3,g.getHeight()/2 , //950, 1464 (depende de la pantalla)
                g.getWidth() / 3,g.getHeight() / 30);
        g.drawImage(tapToPlay, dstRect, alphaTap);

        //Botón de sonido
        soundButton.draw();

        //Botón de info
        instructionsButton.draw();
    }


    private int backgroundNo;
    private Image backgrounds; //1 fila, 9 columnas
    private Image logo;
    private Image tapToPlay;
    private Image buttons; //1 fila, 10 columnas
    private int[] coloresFlechas;
    private float alphaTap;
    private float velocidad;

    private int barsWidth;

    private Button soundButton;
    private Button instructionsButton;
}
