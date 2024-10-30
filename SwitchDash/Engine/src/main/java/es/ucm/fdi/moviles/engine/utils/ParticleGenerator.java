package es.ucm.fdi.moviles.engine.utils;

import java.util.Random;
import java.util.Vector;

import es.ucm.fdi.moviles.engine.graphics.Graphics;
import es.ucm.fdi.moviles.engine.graphics.Sprite;

public class ParticleGenerator {
    public ParticleGenerator(Graphics graphics)
    {
        this.particles_=new Vector();
        this.graphics=graphics;
        this.rand=new Random();
    }

    /**
     * Acutaliza los vatributos de cada una de las particulas del
     * vector usando el deltaTime
     * @param f deltaTime proporcionado por el game
     */
    public void update(float f)
    {
        for(int i=0;i<particles_.size();i++)
        {
            //Actualizamos los valores de cada particula
            Particle par=(Particle)particles_.elementAt(i);
            par.update(f);

            //En caso de que sean invisibles , las quitamos del vector
            if(par.getAlpha()<=0)
                particles_.remove(i);
        }
    }

    /**
     * Pinta todas las particulas que se encuentren en el vector
     */
    public void Render()
    {
        for(int i=0;i<particles_.size();i++) {
            //Actualizamos los valores de cada particula
            Particle par = (Particle) particles_.elementAt(i);
            //Pintamos cada una de las particulas
            Rect srcRect = new Rect(0, par.getColor(),
                    par.getImage().getWidth() / 10, par.getImage().getHeight() / 2);

           ;
            Sprite particleSprite = new Sprite(par.getImage(), srcRect, graphics);
            particleSprite.drawCentered(par.getX(),par.getY(),par.getRandomScale(),par.getAlpha());
        }
    }

    /**
     * Dada una posicion y un color crea una particula con unos parametros
     * iniciales randomizados y la añade al vector de particulas
     * @param initialPositionX posicionX inicial de la particula
     * @param initialPositionY posicionY inicial de la particula
     * @param color color de la particula
     */
    private void createParticle(int initialPositionX,int initialPositionY,int color)
    {
        float alpha=(float)(Math.floor(Math.random() * 255)/255);
        int velX=rand.nextInt(250+250)-250;
        int velY=(int)(Math.floor(Math.random() * 200));
        int randomNumber=(int)Math.floor(Math.random() * 2);
        int widthDivison=1+randomNumber;
        int heigthDivison=1+randomNumber;
        Particle particle=new Particle(initialPositionX,initialPositionY,velX,velY,widthDivison,heigthDivison,alpha,color);
        particles_.add(particle);
    }

    /**
     * Dada una posicion y un color llama al createParticle tantas
     * veces como particulas queramos crear
     * @param initialPositionX posicionX inicial de la particula
     * @param initialPositionY posicionY inicial de la particula
     * @param color color de la particula
     */
    public void createSimulation(int initialPositionX,int initialPositionY,int color)
    {
        for(int i=0;i<4;i++)
        {
            createParticle(initialPositionX,initialPositionY,color);
        }
    }
    private Vector particles_;
    private Graphics graphics;
    private Random rand;
}