package es.ucm.fdi.moviles.logic;

import es.ucm.fdi.moviles.engine.system.Game;
import es.ucm.fdi.moviles.engine.system.GameState;
import es.ucm.fdi.moviles.engine.system.ResourceMan;

public class LoadState implements GameState {
    //Objeto del juego
    private Game game;

    public LoadState(Game game)
    {
        this.game = game;
    }

    /**
     * Cargamos todas las imagenes en el resourceManager una solo vez para
     * no tener que cargarlas en cada uno de los estados
     * @return
     */
    @Override
    public boolean init()
    {
        //Carga de recursos
        ResourceMan.loadImage("backgrounds.png", "Backgrounds");
        ResourceMan.loadImage("balls.png", "Balls");
        ResourceMan.loadImage("arrowsBackground.png", "Flechas");
        ResourceMan.loadImage("players.png", "Players");
        ResourceMan.loadImage("switchDashLogo.png", "Logo");
        ResourceMan.loadImage("tapToPlay.png", "TapToPlay");
        ResourceMan.loadImage("buttons.png", "Buttons");
        ResourceMan.loadImage("howToPlay.png", "HowToPlay");
        ResourceMan.loadImage("instructions.png", "instructions");
        ResourceMan.loadImage("gameOver.png", "GameOver");
        ResourceMan.loadImage("playAgain.png", "PlayAgain");
        ResourceMan.loadImage("white.png", "White");
        ResourceMan.loadImage("scoreFont.png", "ScoreFont");

        //Cambiamos al menú
        game.setGameState(new MenuState(game));
        return true;
    }


    //No hacen falta
    @Override
    public void update(float deltaTime){ }

    @Override
    public void render() { }
}
