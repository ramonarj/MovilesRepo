package es.ucm.fdi.moviles.logic;

import es.ucm.fdi.moviles.engine.graphics.Graphics;
import es.ucm.fdi.moviles.engine.system.Game;
import es.ucm.fdi.moviles.engine.graphics.Image;
import es.ucm.fdi.moviles.engine.utils.Rect;
import es.ucm.fdi.moviles.engine.system.ResourceMan;
import es.ucm.fdi.moviles.engine.graphics.Sprite;

/**
 * Proporciona utilidades para este juego en concreto,
 * sobre tod0 funcionalidades útiles en varias escenas distintas
 */
class GameManager {
    private static GameManager ourInstance = null;
    /**
     * crea la instancia del GameManageer que sera posteriormente usada
     * hasta que acabe el juego
     */
    public static void initInstance(Game game)
    {
        ourInstance = new GameManager(game);
    }

    /**
     * @return la instancia del GameManager
     */
    static GameManager getInstance() {
        return ourInstance;
    }

    /**
     * Carga la imagen de los puntos y se guarda la instancia de Game
     * @param game instancia que nos guardaremos
     */
    private GameManager(Game game)
    {
        this.scoreFont = ResourceMan.getImage("ScoreFont");
        this.game = game;
        this.backgrounds = ResourceMan.getImage("Backgrounds");

        restartGame();
    }

    /**
     * Establece el color de fondo
     * @param backGroundNo color de fondo
     */
    public void setBackGroundNo(int backGroundNo){this.backGroundNo = backGroundNo;}

    /**
     * Establece el color lateral
     * @param lateralColor color laterañ
     */
    public void setLateralColor(int lateralColor){this.lateralColor = lateralColor;}

    /**
     * Establece la anchura de la barra
     * @param barsWidth anchura de la barra
     */
    public void setBarsWidth(int barsWidth){this.barsWidth = barsWidth;}

    /**
     * @return el color de fondo
     */
    public int getBackGroundNo(){return backGroundNo;}

    /**
     * @return el color del lateral
     */
    public int getLateralColor(){return lateralColor;}

    /**
     * @return el ancho de la barra
     */
    public int getBarsWidth(){return barsWidth;}


    public void setArrowSprite(Image img)
    {
        this.flechas = img;
        this.posFlechas = flechas.getHeight() - game.getGraphics().getHeight();
    }

    /**
     * Pinta dos imagenes de flechas completas haciendo que una se ponga
     * encima de la otra hasta el final de la partida
     */
    public void drawArrows()
    {
        Graphics g  = game.getGraphics();

        Rect dstRect = new Rect(barsWidth,posFlechas,
                3 * barsWidth, flechas.getHeight());
        g.drawImage(flechas, dstRect, 0.25f);
    }

    /**
     * Actualiza las posiciones de las flechas con el uso del deltatime
     * @param deltaTime usado para actualizar las posiciones de las flechas
     */
    public void updateArrows(float deltaTime)
    {
        //Flechas
        int incr = (int)(deltaTime*velFlechas);
        posFlechas += incr;

        //Comprobar si hay que moverla al salirse de la pantalla
        if(posFlechas>0)
            posFlechas = -flechas.getHeight()/5;
    }

    //SOLO EN MAYUSCULAS
    public void drawText(String text, int x, int y, float scale)
    {
        int charWidth = scoreFont.getWidth() / 15 - 16;
        int charHeight = scoreFont.getHeight() / 7 - 24;
        int gap = (int)((charWidth / 1.2f) * scale);

        int rows = 7;
        int cols = 15;
        //Pintamos cada una de las letras
        for(int i = 0; i< text.length();i++)
        {
            //Pasamos a ascii y contamos la a como 0
            int pos = ((int)text.charAt(i)) - 65;

            int posX, posY;
            if(pos < cols)
                posX = pos;
            else
                posX = pos % cols;

            posY =  pos / cols;

            //Pintamos
            Rect srcRect = new Rect(posX * scoreFont.getWidth() / cols + 16, posY * scoreFont.getHeight() / rows + 24,
                    charWidth, charHeight);
            Sprite scoreSprites = new Sprite(scoreFont, srcRect, game.getGraphics());
            scoreSprites.drawCentered(x + i * gap, y, scale);
        }
    }

    /**
     * Metodo generico para pintar numeros que puede ser llamado desde varios estados
     * @param number numero a pintar
     * @param x posicion x donde pintar
     * @param y posicion y donde pintar
     * @param scale escala del numero a pintar
     */
    public void drawNumber(int number, int x, int y, float scale, int numDigits)
    {

        int charWidth = scoreFont.getWidth() / 15 - 16;
        int charHeight = scoreFont.getHeight() / 7 - 24;

        int auxNumber = number;
        int gap = (int)((float)80 * scale);

        //Pintamos una vez por cada dígito (de derecha a izquierda)
        for(int i=0;i<numDigits;i++)
        {
            //Número que queremos pintar
            int numeroApintar = auxNumber % 10;
            auxNumber /= 10;

            //Posición en la spritesheet
            int posicion=7+numeroApintar;
            int posicionY=3;
            if(posicion>14 && posicion<=16)
            {
                posicion-=15;
                posicionY=4;
            }
            else if(posicion>1 && posicionY==4)
            {
                posicion=7;
                posicionY=3;
            }


            //El 16 y el 24 son la mitad del espacio en blanco que hay en cada caracter (33px en X, 48px en Y)
            Rect srcRect = new Rect(posicion * scoreFont.getWidth() / 15 + 16, posicionY * scoreFont.getHeight() / 7 + 24,
                    charWidth, charHeight);
            Sprite scoreSprites = new Sprite(scoreFont, srcRect, game.getGraphics());

            scoreSprites.drawCentered(x + gap / 2 *(numDigits- 1) - gap * i, y, scale);
        }
    }

    /**
     * Pinta del color aleatorio escogido anteriormente el fondo del juego
     */
    public void drawBackground()
    {
        Graphics g = game.getGraphics();

        //Clear
        g.clear(lateralColor);

        //Color del centro
        Rect backRect = new Rect(barsWidth,0,3 * barsWidth, g.getHeight());
        Rect dstRect=new Rect(backgrounds.getWidth() / 9 * backGroundNo,0,backgrounds.getWidth() / 9,backgrounds.getHeight());
        Sprite backSprite=new Sprite(backgrounds,dstRect,g);
        backSprite.draw(backRect);
    }

    public void addScore(int score)
    {
        this.score += score;
        if(this.score%division==0)
        {
            scoreDigits++;
            division*=10;
        }
    }

    public int getScore(){return this.score;}
    public int getScoreDigits(){return this.scoreDigits;}

    public void restartGame()
    {
        //puntuación
        this.score =0;
        this.scoreDigits=1;
        this.division=10;

        //Velocidades
        this.velBolas = 430;
        this.velFlechas = 384;
    }

    /**
     * suma 90 de velocidad a todas las bolas del estado
     */
    public void increaseVelocity()
    {
        velBolas += 90;
        velFlechas+=90;
    }

    public int getBallVel(){return velBolas;}


    private Image flechas;
    private Image backgrounds;

    private int velBolas;
    private int posFlechas;
    private int velFlechas;

    private int division;
    private int score;
    private int scoreDigits;
    private int lateralColor;
    private int backGroundNo;
    private int barsWidth;
    private Image scoreFont;
    private Game game;
}
