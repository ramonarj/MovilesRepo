using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/*
 * Este script existe para poder llamar a métodos del GameManager desde botones de la escena
 * sin depender de que esté añadido a un objeto/no.
 */
public class Callbacks : MonoBehaviour
{
    //Pulsación en el cuadradito del nivel
    public void OnClickedLevel(int level)
    {
        GameManager.Instance().GoToLevel(level);
    }

    //Pulsación en el play (en el pop-up al completar un nivel)
    public void OnClickedNextLevel()
    {
        GameManager.Instance().NextLevel();
        AdsManager.Instance().ShowAd();
    }

    //Pulsación en home (en el pop-up al completar un nivel)
    public void OnClickedHome()
    {
        GameManager.Instance().GoToMenu();
        if (GameManager.Instance().getChallenge())
        {
            GameManager.Instance().setChallenge(false);
            GameManager.Instance().wasChallenge = true;
        }
    }

    //Pulsación de un modo de juego (1-5)
    public void OnClickedGamemode(int gamemode)
    {
        GameManager.Instance().GoToSeleccion(gamemode);
    }

    //Pulsación en home (para salir del juego en el menú)
    public void OnClickedExit()
    {
        GameManager.Instance().QuitApp();
    }

    //Pulsación en el botón de los anuncios
    public void OnClickedViewAd(int coins)
    {
        AdsManager.Instance().ShowRewardedAd(coins);
    }

    //Pulsacion en el boton de challenge
    public void OnClickChallenge()
    {
        GameManager.Instance().ShowChallengePanel();
    }

    //Pulsacion sobre el regalo
    public void onClickGift()
    {
        GameManager.Instance().ShowGiftPanel();
    }

    //Pulsacion sobre el regalo
    public void onClickSmallGift()
    {
        GameManager.Instance().ShowGift();
    }

    //Cierra popups
    public void OnClosePopup()
    {
        GameManager.Instance().ClosePopUps();
    }

    //Pulsacion de comprar el modo challenge 
    public void OnClickpayChallenge()
    {
        if (GameManager.Instance().getCoins() >= 25)
        {
            GameManager.Instance().playChallenge();
        }
    }

    //Pulsacion de ir al modo challenge con anuncio
    public void OnClicknoPayChallenge()
    {
        GameManager.Instance().playChallenge();
    }

    //Anade una cantidad determinada de monedas
    public void addCoinsChallenge(int coins)
    {
        if (GameManager.Instance().getChallenge())
        {
            GameManager.Instance().addCoins(coins);
        }
    }

    //Anade una cantidad determinada de monedas
    public void addCoins(int coins)
    {
        GameManager.Instance().addCoins(coins);
    }


    //Reproducción de un clip al pulsar
    public void OnSoundPlayed(AudioClip clip)
    {
        GameManager.Instance().playSound(clip);
    }

    //Pulsación en el botón de volver (dentro del nivel)
    public void OnClickedBack()
    {
        if (!GameManager.Instance().getChallenge())
            GameManager.Instance().GoToSeleccion();
        else
        {
            GameManager.Instance().GoToMenu();
            GameManager.Instance().wasChallenge = true;
            GameManager.Instance().setChallenge(false);
        }
    }
}
