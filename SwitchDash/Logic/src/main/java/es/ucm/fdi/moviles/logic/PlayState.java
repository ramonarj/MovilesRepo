package es.ucm.fdi.moviles.logic;

import java.util.ArrayList;

import es.ucm.fdi.moviles.engine.system.Game;
import es.ucm.fdi.moviles.engine.system.GameState;
import es.ucm.fdi.moviles.engine.graphics.Graphics;
import es.ucm.fdi.moviles.engine.graphics.Image;
import es.ucm.fdi.moviles.engine.input.Input;
import es.ucm.fdi.moviles.engine.utils.ParticleGenerator;
import es.ucm.fdi.moviles.engine.utils.Rect;
import es.ucm.fdi.moviles.engine.system.ResourceMan;
import es.ucm.fdi.moviles.engine.graphics.Sprite;

public class PlayState implements GameState
{
    //Objeto del juego
    private Game game;

    public PlayState(Game game)
    {
        this.game = game;
        this.particleGenerator=new ParticleGenerator(this.game.getGraphics());
    }

    /**
     * Inicializamos todos los atributos del estado tanto las imaganes como las varibles
     * @return
     */
    @Override
    public boolean init()
    {
        //Carga del graphics
        g=game.getGraphics();

        //Carga de recursos
        balls = ResourceMan.getImage("Balls");
        player = ResourceMan.getImage("Players");
        buttons = ResourceMan.getImage("Buttons");
        white = ResourceMan.getImage("White");

        int[]coloresFlechas = new int[]{ 0xff41a85f, 0xff00a885, 0xff3d8eb9, 0xff2969b0,
                0xff553982, 0xff28324e, 0xfff37934, 0xffd14b41, 0xff75706b };

        backGroundNo =(int)Math.floor(Math.random() * 9);

        GameManager.getInstance().setBackGroundNo(backGroundNo);
        GameManager.getInstance().setLateralColor(coloresFlechas[backGroundNo]);
        GameManager.getInstance().restartGame();

        //Inicialización de las variables
        playerColor = 0;


        //Inicializamos las bolas
        contBolas=0;
        numBolas=4;
        ballColor=new int[numBolas];
        posBolas=new int[numBolas];
        posBolas[0]=-395;
        ballColor[0]=balls.getHeight()/2*(int)Math.floor(Math.random() * 2);
        for(int i=1;i<numBolas-1;i++) {
            posBolas[i] = -((i+1)*395);
            ballColor[i]=setballColor(ballColor[i-1]);
        }

        this.whiteAlpha=1.0f;

        return  true;
    }


    /**
     * Por cada tick , manejamos los eventos
     * y actualizamos todos los atributos del estado
     * @param deltaTime
     */

    @Override
    public void update(float deltaTime)
    {
        //Input
        checkInput();

        //Actualizar las flechas
        GameManager.getInstance().updateArrows(deltaTime);


        //Recorremos cada una de las pelotas y comprobamos si al estar en contacto con la barra
        //han podido ser atrapdas o en caso contrario , ha finalizado la partida
        for(int i=0;i<numBolas;i++) {
            posBolas[i] += (deltaTime * GameManager.getInstance().getBallVel());
            if(checkPosition(posBolas[i]))
            {
                if(match(ballColor[i])) {
                    particleGenerator.createSimulation(g.getWidth() / 2, posBolas[i], ballColor[i]);
                    posBolas[i] = getMenor() - 395;
                    ballColor[i] = setballColor(ballColor[takeBallIndex(i - 1, numBolas)]);
                    contBolas++;
                    GameManager.getInstance().addScore(1);
                    if (contBolas == 10)
                    {
                        GameManager.getInstance().increaseVelocity();
                        contBolas = 0;
                    }
                } else
                {
                    game.setGameState(new GameOverState(game));
                    break;
                }
            }
        }
        particleGenerator.update(deltaTime);

        //Necesario para hacer la animacion del flash al principio de cada estado
        whiteAlpha-=(float)10*(deltaTime);
        if(whiteAlpha<0.0f)whiteAlpha=0.0f;
    }

    /**
     * Por cada tick , pintamos todos los elementos del estado
     */
    @Override
    public void render()
    {

        GameManager.getInstance().drawBackground();

        //Flechas
        GameManager.getInstance().drawArrows();

        //Pelota
        drawBalls();

        //Particulas
        particleGenerator.Render();

        //Jugador
        drawPlayer();

        //Puntuación
        GameManager.getInstance().drawNumber(GameManager.getInstance().getScore(), 1000,
                200, 1f, GameManager.getInstance().getScoreDigits());

        if(whiteAlpha>0.0f)
              drawFlash();

    }


    /** METODOS AUXILIRES **/

    /**
     * por cada tick, comprueba si ha habido algun evento que tengamos que procesar
     */
    private void checkInput()
    {
        Input input = game.getInput();
        ArrayList<Input.TouchEvent> events = (ArrayList)input.getTouchEvents();
        for(Input.TouchEvent evt: events)
        {
            //Cambiamos de color
            if(evt.type == Input.TouchEvent.EventType.PRESSED)
                playerColor = 1 - playerColor;
        }
    }



    /**
     * Dado que % en java puede devolver negativo, usaremos este metodo
     * @param index Indice anterior a la bola que queremos pintar para saber que colo tiene
     * @param numBolas Numero de bolas que usamos en el bucle de juego
     * @return el indice en el array de colores de bolas que usaremos para pintar la siguiente bola
     */
    private int takeBallIndex(int index,int numBolas) {
        return (((index % numBolas) + numBolas) % numBolas);
    }


    /**
     * Al principio pintaremos una pantalla blanca bajando de alpha hasta que sea 0 y empezamos el render
     */
    private void drawFlash()
    {
        Rect srcRect=new Rect(0,0,white.getWidth(),white.getHeight());
        Rect destRect=new Rect(0,0,g.getWidth(),g.getHeight());
        g.drawImage(white,srcRect,destRect,whiteAlpha);

    }

    /**
     * @param lastColor color de la siguiente pelota
     * @returnel color de la siguiente pelota, haciendo que el 70% sea el de la anterior
     * y el 30% el otro.
     */
    private int setballColor(int lastColor)
    {
        int value=(int)Math.floor(Math.random()*9);
        if(value<=6)return lastColor;
        else if(lastColor==balls.getHeight()/2)return 0;
        else return balls.getHeight()/2;
    }


    /**
     * Pinta la barra de blanco o negro dependiendo del input del usuario
     */
    private void drawPlayer() {
        Rect srcRect=new Rect(0,playerColor * player.getHeight() / 2,player.getWidth(),player.getHeight() / 2);
        Sprite playerSprite=new Sprite(player,srcRect,g);
        playerSprite.drawCentered(g.getWidth() / 2,1200);
    }

    /**
     * Pinta un numero maximo de bolas escogidas con la intencion de
     * que sean reutilizadas hasta el final de la misma
     */
    private void drawBalls() {
        for(int i=0;i<numBolas;i++)
        {
            Rect srcRect=new Rect(0,ballColor[i],balls.getWidth() / 10,balls.getHeight() / 2);
            Sprite ballSprite=new Sprite(balls,srcRect,g);
            ballSprite.drawCentered(g.getWidth() / 2,posBolas[i]);
        }
    }


    /**
     * Escoge la bola que este mas alta dentro de la pantalla
     * @return
     */
    private int getMenor()
    {
        int value=game.getGraphics().getHeight();
        for(int i=0;i<numBolas;i++)
            if(posBolas[i]<value)value=posBolas[i];
        return value;
    }

    /**
     *
     * @param posBola posicion de la bola i-esima
     * @return true si la bola toca con la barra del juego, false en caso contrario
     */
    private boolean checkPosition(int posBola) {
        return (posBola+balls.getHeight()/4>1200-player.getHeight()/4 && posBola<1200+player.getHeight()/4);
    }

    /**
     * Comprueba si el color del jugadory el de la bola comprueban
     * @param ballcolor color de la bola i-esima
     * @return true si el color de la bola es igual al de la barra, false en caso contraio
     */
    private boolean match(int ballcolor) {
        return (ballcolor==0 && playerColor==0) || (ballcolor==balls.getHeight()/2 && playerColor==1);
    }

    //Fondo
    private Image white;

    //Objetos
    private Image balls; //2 filas, 10 columnas
    private Image player; //2 filas, 1 columna

    //Textos
    private Image buttons; //1 fila, 10 columnas


    //VARIABLES DE JUEGO:
    private int playerColor; //0 = blanco, 1 = negro
    private int contBolas;
    private int posBolas[];
    private int numBolas;
    private int ballColor[];
    private int backGroundNo;
    private float whiteAlpha;

    private Graphics g;
    private ParticleGenerator particleGenerator;
}
