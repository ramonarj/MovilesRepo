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

public class InstructionsState implements GameState {


    //Objeto del juego
    private Game game;


    public InstructionsState(Game game)
    {
        this.game = game;
        this.barsWidth = GameManager.getInstance().getBarsWidth();
    }

    /**
     * Inicializa todas las variables del estado
     * @return
     */
    @Override
    public boolean init()
    {
        Graphics g = game.getGraphics();

        howToPlay = ResourceMan.getImage("HowToPlay");
        instructions = ResourceMan.getImage("instructions");
        tapToPlay = ResourceMan.getImage("TapToPlay");
        buttons = ResourceMan.getImage("Buttons");
        white = ResourceMan.getImage("White");

        barsWidth = g.getWidth() / 5;

        int buttonWidth = buttons.getWidth() / 10;
        int buttonHeight = buttons.getHeight();

        //Botón de instrucciones
        Rect srcRect =new Rect(buttonWidth,0, buttonWidth ,buttonHeight);
        Sprite infoSprite=new Sprite(buttons,srcRect,g);
        closeButton = new Button(infoSprite, g.getWidth() - barsWidth / 2, 200, "Cerrar");

        veloidad=0.6f;
        alphaTap=1f;
        this.whiteAlpha=1.0f;

        return true;
    }

    /**
     * Por cada tick actualiza los atrbitos necesarios y comprueba si ha habido
     * algun evento que tengamos que procesar
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime)
    {
        //Actualizar las flechas
        GameManager.getInstance().updateArrows(deltaTime);

        alphaTap+=(deltaTime*veloidad);
        if(alphaTap>=1f || alphaTap<=0)
        {
            if(alphaTap<=0)alphaTap=0;
            else if(alphaTap>=1f)alphaTap=1f;
            veloidad*=-1;
        }

        Input input = game.getInput();
        ArrayList<Input.TouchEvent> events = (ArrayList)input.getTouchEvents();
        for(Input.TouchEvent evt: events)
        {
            //Cambiamos al juego
            if(evt.type == Input.TouchEvent.EventType.PRESSED)
                if(closeButton.isPressed(evt.x, evt.y))
                    game.setGameState(new MenuState(game));
                else
                    game.setGameState(new PlayState(game));
        }
        //Necesario para hacer la animacion del flash al principio de cada estado
        whiteAlpha-=(float)10*(deltaTime);
        if(whiteAlpha<0.0f)whiteAlpha=0.0f;
    }

    /**
     * Pinta todos los elementos del estado
     */
    @Override
    public void render() {

        //Color de fondo (para las barras laterales)
        Graphics g = game.getGraphics();
        int width = g.getWidth();
        int height = g.getHeight();

        //Fondo
        GameManager.getInstance().drawBackground();;

        //Flechas
        GameManager.getInstance().drawArrows();

        //How to play
        Rect destRect = new Rect((int)(width / 3.25),height / 7 ,
                (int)(width / 2.5),height / 6);
        g.drawImage(howToPlay, destRect, 1f);

        //Instructions
        Rect srcRect = new Rect(0,0,instructions.getWidth(), instructions.getHeight());
        Sprite instructionsSprite=new Sprite(instructions,srcRect,g);
        instructionsSprite.drawCentered(width / 2, 950);

        //TapToPlay
        srcRect = new Rect(0,0,tapToPlay.getWidth(), tapToPlay.getHeight());
        Sprite tapSprite=new Sprite(tapToPlay,srcRect,g);
        tapSprite.drawCentered(width / 2, 1400, 1, alphaTap);

        //Botón de salir
        closeButton.draw();

        if(whiteAlpha>0.0f)
            drawFlash();
    }

    /**
     * Al principio pintaremos una pantalla blanca bajando de alpha hasta que sea 0 y empezamos el render
     */
    private void drawFlash()
    {
        Rect srcRect=new Rect(0,0,white.getWidth(),white.getHeight());
        Rect destRect=new Rect(0,0,game.getGraphics().getWidth(),game.getGraphics().getHeight());
        game.getGraphics().drawImage(white,srcRect,destRect,whiteAlpha);

    }

    //Fondo
    private Image white;

    private Image howToPlay;
    private Image instructions;
    private Image tapToPlay;

    private Image buttons; //1 fila, 10 columnas
    private float alphaTap;
    private float veloidad;
    private int barsWidth;
    private float whiteAlpha;

    Button closeButton;
}

