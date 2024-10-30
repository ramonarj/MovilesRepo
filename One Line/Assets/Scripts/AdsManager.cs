using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Monetization;


public class AdsManager : MonoBehaviour
{
    private static AdsManager instance;
    //Id del juego
    private string gameId = "3417809";
    //Modo test por defecto
    private bool testMode = true;
    //Numero de veces que ha querido ver un anuncio
    private int rewardCout = 0;

    [Tooltip("Numero de recompensa que daremos cada vez que veamos un anuncio de tipo recompensa")]
    public int coinsRewarded = 20;

    //Ids de los tipos de anuncios
    string placementIdVideo = "video";
    string placementIdRewardedVideo = "rewardedVideo";

    public static AdsManager Instance()
    {
        if (instance == null)
            instance = new GameObject("AdsManager").AddComponent<AdsManager>();
        return instance;
    }

    void Awake()
    {
        // Singleton
        if (instance == null)
            instance = this;
        else
            Destroy(this.gameObject);
        Object.DontDestroyOnLoad(gameObject);
    }

    void Start()
    {
        /*Inicializamos Unity ads con el gameId correcto y en modo test*/
        Monetization.Initialize(gameId, testMode);
    }

    /*Mostramos anuncios normales*/
    public void ShowAd()
    {
        StartCoroutine(WaitForAd());
    }

    /*Mostramos anuncios con recompensa*/
    public void ShowRewardedAd(int coins)
    {
        coinsRewarded = coins;
        StartCoroutine(WaitForAd(true));
    }

    /*Esperamos a que podamos lanzar un anuncio,de cualquier tipo
    y en caso de que se trate de un anuncio de recompensa,llamamos al adfinished
    para comprobar si lo ha visto entero y por tanto recibe la recompensa o lo ha saltado*/
    IEnumerator WaitForAd(bool rewarded=false)
    {
        string placementId = rewarded ? placementIdRewardedVideo : placementIdVideo;

        while (!Monetization.IsReady(placementId))
        {
            yield return null;
        }

        ShowAdPlacementContent ad = null;
        ad = Monetization.GetPlacementContent(placementId) as ShowAdPlacementContent;

        /*En caso de que el anuncio no sea nulo*/
        if (ad != null)
        {
            /*Si es un anuncio es de recompensa, comprobaremos si lo ha terminado de ver*/
            if (rewarded)
                ad.Show(AdFinished);
            else
                ad.Show();
        }
    }

    void AdFinished(ShowResult result)
    {
        /*Si ha terminado de ver el anuncio , le recompensamos dandole monedas*/
        if (result == ShowResult.Finished)
        {
            GameManager.Instance().addCoins(coinsRewarded);
        }
    }
}
