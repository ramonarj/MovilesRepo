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

public class GameOverState implements GameState {


    //Objeto del juego
    private Game game;

    public GameOverState(Game game)
    {
        this.game = game;
        this.barsWidth = GameManager.getInstance().getBarsWidth();
    }

    /**
     * Inicializa todos los elementos del estado
     * @return
     */
    @Override
    public boolean init() {
        Graphics g = game.getGraphics();

        buttons = ResourceMan.getImage("Buttons");
        gameOver = ResourceMan.getImage("GameOver");
        playAgain = ResourceMan.getImage("PlayAgain");

        barsWidth = g.getWidth() / 5;

        int buttonWidth = buttons.getWidth() / 10;
        int buttonHeight = buttons.getHeight();

        //Botón de sonido
        Rect srcRect = new Rect(2* buttonWidth,0,  buttonWidth,buttonHeight);
        Sprite soundSprite=new Sprite(buttons,srcRect,g);
        srcRect = new Rect(3* buttonWidth,0,  buttonWidth,buttonHeight);
        Sprite soundSprite2=new Sprite(buttons,srcRect,g);

        soundButton = new Button(soundSprite, soundSprite2, barsWidth / 2,200, "Sonido");

        //Botón de instrucciones
        srcRect =new Rect(0,0, buttonWidth,buttonHeight);
        Sprite infoSprite=new Sprite(buttons,srcRect,g);
        instructionsButton = new Button(infoSprite, g.getWidth() - barsWidth / 2, 200, "Instrucciones");

        alphaTap=1f;
        velocidad =0.6f;
        return true;
    }

    /**
     * Por cada tick , actualiza todos los elementos del estado
     * y comprueba si hay algun evento por procesar
     * @param deltaTime time usado para actualizar los elementos del estado
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
            //Cambiamos al juego
            if(evt.type == Input.TouchEvent.EventType.PRESSED)
                if(instructionsButton.isPressed(evt.x, evt.y))
                    game.setGameState(new InstructionsState(game));
                else if (soundButton.isPressed(evt.x, evt.y))
                    soundButton.toggleSprite();
                else
                    game.setGameState(new PlayState(game));
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
     * Pinta todos los elementos del estado
     */
    @Override
    public void render()
    {
        Graphics g = game.getGraphics();

        //Fondo
        GameManager.getInstance().drawBackground();

        //Flechas
        GameManager.getInstance().drawArrows();

        //Game Over
        Rect srcRect = new Rect(0,0, gameOver.getWidth(), gameOver.getHeight());
        Sprite gameOverSprite=new Sprite(gameOver,srcRect,g);
        gameOverSprite.drawCentered(g.getWidth() / 2, 364);

        //Play again
        srcRect = new Rect(0,0, playAgain.getWidth(), playAgain.getHeight());
        Sprite playAgainSprite=new Sprite(playAgain,srcRect,g);
        playAgainSprite.drawCentered(g.getWidth() / 2, 1396, 1, alphaTap);

        //Botón de sonido
        soundButton.draw();

        //Botón de info
        instructionsButton.draw();


        GameManager.getInstance().drawNumber(GameManager.getInstance().getScore(), game.getGraphics().getWidth() / 2,
                800, 1.5f, GameManager.getInstance().getScoreDigits());;

        //Score text
        GameManager.getInstance().drawText("POINTS", barsWidth + 3 * barsWidth / 4, 1000, 0.75f);
    }

    private Image gameOver;
    private Image playAgain;
    private Image buttons;
    private float alphaTap;
    private float velocidad;
    private int barsWidth;

    private Button soundButton;
    private Button instructionsButton;
}
