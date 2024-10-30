using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.IO;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class GameManager : MonoBehaviour
{
    private static GameManager instance;

    //Prefab del GO con el componente con AudioSource
    public GameObject reproductor;

    //Nombres de las dificultades
    public List<string> difficulties;

    public bool wasChallenge;

    //Número de monedas
    private int coinNo=0;

    //Número de niveles que nos hemos pasado en cada nivel de dificultad (5 enteros de 1-100)
    private List<int> levelprogress;

    //Datos de todos los niveles
    private List<LevelDataList> levelDataLists;

    //Nivel seleccionado para jugar
    private int actualLevel; // 1-100 
    private int actualDifficulty; //1-5
    private int challengeCount;

    //Audio source que se crea solo para reproducir sonidos
    private GameObject source;
    //Cogemos el nombre del Script reproductor
    public string getString() { return this.gameObject.name; }
    public int getNumber() { return levelprogress.Count; }

    //Conocer si nos encontramos en modo challenge
    private bool challenge;
    //Conocer si tenemos que esperar 30 mins para volver a jugar al challenge
    private bool waitingChallenge;
    //Tiempo entre challenges
    private float timeChallengeLeft;
    //Minutos y segundos
    private int minutos, segundos;
    private string ultimoRetoJugado;
    private int waiting; //Saber si tenemos que esperar
        
    public static GameManager Instance() { 
        if(instance==null)
            instance = new GameObject("GameManager").AddComponent<GameManager>();
        return instance;
    }

    void Awake()
    {
        if (instance != null && instance != this)
            Destroy(gameObject);
        else
        {
            instance = this;

            //Leemos los niveles (usammos WWw)
            levelDataLists = new List<LevelDataList>();

            //Hay un archivo por cada dificultad
            for(int i= 0; i < difficulties.Count; i++)
            {
                string filePath = Application.streamingAssetsPath + "/" + difficulties[i] + ".json";

                //Funciona en PC y en Android (no hace falta poner macros)
                UnityEngine.Networking.UnityWebRequest www = UnityEngine.Networking.UnityWebRequest.Get(filePath);
                www.SendWebRequest();
                while (!www.isDone) { }
                string inputJson = www.downloadHandler.text;

                //Una vez tenemos la cadena con el json, leemos los niveles
                levelDataLists.Add(LevelDataList.CreateFromJSON(inputJson));
            }

            

            //Para cubrirnos las espaldas
            actualLevel = 1;
            actualDifficulty = 1;

            //Start
            coinNo = 0;
            levelprogress = new List<int>();
            for (int i = 0; i < difficulties.Count; i++)
                levelprogress.Add(0);

            SceneManager.sceneLoaded += OnSceneLoaded;
            Object.DontDestroyOnLoad(gameObject);
            wasChallenge = false;
        }
    }

    void Start()
    {
        //Intentamos cargar el archivo de progreso, y si lo hemos conseguido, comprobamos el hash
        if (SaveDataManager.Instance().load())
            if (compareHashes(SaveDataManager.Instance().getGame()))
            {
                levelprogress = SaveDataManager.Instance().getGame().levels;
                coinNo = SaveDataManager.Instance().getGame().coins;
                challengeCount = SaveDataManager.Instance().getGame().challenge;
                ultimoRetoJugado = SaveDataManager.Instance().getGame().dateTime;
                waiting = SaveDataManager.Instance().getGame().waiting;
                Debug.Log("Juego cargado correctamente");
            }
            else Debug.Log("Juego reiniciado debido a una modificacion del archivo de carga");

        //Empezamos con challenge a false
        challenge = false;
        timeChallengeLeft = 30 * 60; //Segundos
    }

    //Controlar el cronometro del challenge
    private void Update()
    {
        if (waitingChallenge)
        {
            float cosa = Time.deltaTime;
            int coco = 0;
            timeChallengeLeft -= cosa;
        
            /*Nos aseguramos unicamnte de acutualizar cuando estamos en el menu*/
            if (SceneManager.GetActiveScene().name == "Menu")
            {
                Text waitingText = GameObject.Find("timeWaiting").GetComponent<Text>();
                minutos = (int)(timeChallengeLeft / 60);
                segundos = (int)(timeChallengeLeft % 60);
                waitingText.text = string.Format("{0:0}:{1:00}", minutos, segundos);
            }

        }
        if (timeChallengeLeft <= 0 && waitingChallenge)
        {
            waitingChallenge = false;
            timeChallengeLeft = 30 * 60;
            Text waitingText = GameObject.Find("timeWaiting").GetComponent<Text>();
            waitingText.text = "";
            DesactivateChallengeWait();
        }
    }
    private bool compareHashes(GameSaving game)
    {
        string levelsConcatenate = SaveDataManager.Instance().concatenateLevels(game.levels);
        string String = SaveDataManager.Instance().getString(getString(), getNumber());
        string hash = SaveDataManager.Instance().createHash(levelsConcatenate+ game.coins+game.waiting+String+game.challenge+game.dateTime);
        return (hash == game.hash);

    }
    //Devuelve los datos del nivel especificado
    public LevelData getLevelData(int index)
    {
        //Van del 0-500 repartidos en 5 dificultades
        int trueIndex = index + (actualDifficulty - 1) * 100;
        //Hay que cubrirse las espaldas por si no están todos los niveles del 0 al "index"
        return levelDataLists[actualDifficulty - 1].levels.Find(x => x.index == trueIndex);
    }

    /*GETTERS*/
    public int getActualLevel()
    {
        return actualLevel;
    }

    public int getActualDifficulty()
    {
        return actualDifficulty;
    }

    public string getActualDifficultyName()
    {
        return difficulties[actualDifficulty - 1];
    }

    public int getCoins()
    {
        return coinNo;
    }

    public bool getChallenge()
    {
        return challenge;
    }

    public int getLevelProgress(int difficulty)
    {
        return levelprogress[difficulty - 1];
    }

    //Número de niveles que tiene esa dificultad (para la pantalla de selección)
    public int getNumberOfLevels(int difficulty)
    {
        return levelDataLists[difficulty - 1].levels.Count;
    }

    public void setChallenge(bool challenge_) { challenge = challenge_; }
    //Hemos completado el nivel que estábamos jugado
    public void levelCompleted()
    {
        //Si nos tocaba pasarnos ese nivel, actualizamos la lista
        if (levelprogress[actualDifficulty - 1] == actualLevel - 1)
            levelprogress[actualDifficulty - 1]++;
    }

    //Nos lleva a la pantalla de selección (seleccionando dificultad
    public void GoToSeleccion(int difficulty)
    {
        actualDifficulty = difficulty;
        SceneManager.LoadScene("Seleccion", LoadSceneMode.Single);
    }

    //Nos lleva a la pantalla de selección (sin cambiar la dificultad)
    public void GoToSeleccion()
    {
        SceneManager.LoadScene("Seleccion", LoadSceneMode.Single);
        challenge = false;
    }

    //Nos lleva a la pantalla de nivel
    public void GoToLevel(int levelNo)
    {
        //Nivel máximo al que podemos jugar
        int maxLevel = levelprogress[actualDifficulty - 1];
        //Comprobamos que está desbloqueado
        if (maxLevel >= levelNo - 1)
        {
            actualLevel = levelNo;
            GoToScene("Nivel");
        }
    }

    //Jugamos el siguiente nivel
    public void NextLevel()
    {
        actualLevel++;

        //Comprobamos que no nos hemos pasado el último nivel de la dificultad
        if(actualLevel <= levelDataLists[actualDifficulty - 1].levels.Count)
        {
            GoToScene("Nivel");
            int number = (wasChallenge || waiting == 1) ? 1 : 0;
            if (!wasChallenge && ultimoRetoJugado != null)
                SaveDataManager.Instance().save(levelprogress, coinNo, number, challengeCount, ultimoRetoJugado);
            else SaveDataManager.Instance().save(levelprogress, coinNo, number, challengeCount, System.DateTime.Now.ToString("MM/dd/yyyy H:mm:ss"));
        }

        //Si lo hemo hecho, volvemos a la selección
        else
            GoToScene("Seleccion");
    }

    public void playChallenge()
    {
        //Escogemos una dificultad aleatoria
        actualDifficulty = Random.Range(3, difficulties.Count);
        //Escogemos un nivel aleatorio
        actualLevel = Random.Range(1, levelDataLists[actualDifficulty].levels.Count);
        //Ponemos el modo challenge a true
        challenge = !challenge;
        //Vamos a la escena del nivel , cuyo canvas cambiaremos 
        //depenediendo del booleano challenge
        GoToScene("Nivel");
    }

    //Nos lleva a la pantalla de  menú
    public void GoToMenu()
    {
        GoToScene("Menu");
    }

    //Sale de la aplicación
    public void QuitApp()
    {
        int number;
        if (wasChallenge || waiting==1) number = 1;
        else number = 0;
        if (!wasChallenge && ultimoRetoJugado!=null)
            SaveDataManager.Instance().save(levelprogress, coinNo, number, challengeCount, ultimoRetoJugado);
        else SaveDataManager.Instance().save(levelprogress, coinNo, number, challengeCount, System.DateTime.Now.ToString("MM/dd/yyyy H:mm:ss"));

        Application.Quit();
    }

    /*Aumenta el contador de retos superados*/
    public void addChallengeCount()
    {
        challengeCount++;
    }

    /*Aumenta un numero concreto de monedas y actualiza el texto si existe en la escena actual*/
    public void addCoins(int n)
    {
        //Cogemos el texto de las monedas
        coinNo += n;
        GameObject numero = GameObject.Find("Numero");
        if (numero != null)
        {
            Text conisTetx = GameObject.Find("Numero").GetComponent<Text>();
            conisTetx.text = System.Convert.ToString(coinNo);
        }

        int number;
        if (wasChallenge || waiting == 1) number = 1;
        else number = 0;
        if (!wasChallenge && ultimoRetoJugado != null)
            SaveDataManager.Instance().save(levelprogress, coinNo, number, challengeCount, ultimoRetoJugado);
        else SaveDataManager.Instance().save(levelprogress, coinNo, number, challengeCount, System.DateTime.Now.ToString("MM/dd/yyyy H:mm:ss"));
    }

    /*Activamos un flag para saber que no podemos volver a jugar un reto hasta dentro
     de otros 30 mins , y ademas lo mostramos en el canvas*/
    public void ActivateChallengeWait()
    {
        waitingChallenge = true;
        /*Cambiamos el canvas*/
        GameObject.Find("TimeWaitingBar").GetComponent<Image>().enabled = true;
        /*Desactivamos el boton*/
        GameObject.Find("Challenge").GetComponent<Button>().enabled = false;
    }

    /*Desactivamos un flag para saber que ya podemos volver a jugar un reto
    y ademas, lo dejamos de mostrar en el canvas*/
    public void DesactivateChallengeWait()
    {
        waitingChallenge = false;
        /*Cambiamos el canvas*/
        GameObject.Find("TimeWaitingBar").GetComponent<Image>().enabled = false;
        /*Desactivamos el boton*/
        GameObject.Find("Challenge").GetComponent<Button>().enabled = true;
    }

    /*Instancia un clon del reproductor para que reproduzca el clip en cuestión*/
    public void playSound(AudioClip clip)
    {
        source = Instantiate(reproductor);
        source.GetComponent<AudioSource>().PlayOneShot(clip);
        Destroy(source, clip.length);
    }

    /*Mostrarmos unicamente el panel del challenge, desactivando todos los demas*/
    public void ShowChallengePanel()
    {
        GameObject.Find("Canvas").transform.Find("PopUpPanel").gameObject.SetActive(true);
        GameObject.Find("Canvas").transform.Find("PopUpPanel").transform.Find("Present").gameObject.SetActive(false);
        GameObject.Find("Canvas").transform.Find("PopUpPanel").transform.Find("Challenge").gameObject.SetActive(true);
        GameObject.Find("Canvas").transform.Find("PopUpPanel").transform.Find("BigGift").gameObject.SetActive(false);
    }

    /*Mostrarmos unicamente el panel de recompensa del regalo, desactivando todos los demas*/
    public void ShowGiftPanel()
    {
        GameObject.Find("Canvas").transform.Find("PopUpPanel").gameObject.SetActive(true);
        GameObject.Find("Canvas").transform.Find("PopUpPanel").transform.Find("Present").gameObject.SetActive(true);
        GameObject.Find("Canvas").transform.Find("PopUpPanel").transform.Find("Challenge").gameObject.SetActive(false);
        GameObject.Find("Canvas").transform.Find("PopUpPanel").transform.Find("BigGift").gameObject.SetActive(false);
    }

    /*Mostrarmos unicamente el panel del regalo, desactivando todos los demas*/
    public void ShowGift()
    {
        GameObject.Find("Canvas").transform.Find("PopUpPanel").gameObject.SetActive(true);
        GameObject.Find("Canvas").transform.Find("PopUpPanel").transform.Find("Present").gameObject.SetActive(false);
        GameObject.Find("Canvas").transform.Find("PopUpPanel").transform.Find("Challenge").gameObject.SetActive(false);
        GameObject.Find("Canvas").transform.Find("PopUpPanel").transform.Find("BigGift").gameObject.SetActive(true);
    }

    /*Mostrarmos unicamente el panel del regalo, desactivando todos los demas*/
    public void ClosePopUps()
    {
        GameObject.Find("Canvas").transform.Find("PopUpPanel").gameObject.SetActive(false);
    }

    /*Escogemos un nivel aleatorio de una dificultad aleatoria y nos vamos a la escena nivel para jugarlo*/
    public void challengeMode()
    {
        //Escogemos una dificultad aleatoria
        actualDifficulty = Random.Range(0, difficulties.Count);
        //Escogemos un nivel aleatorio
        actualLevel = Random.Range(1, 100);
        //Y volvemos a recargar la escena para cargar el nivel seleccionado
        GoToScene("Nivel");
    }

    /*En caso de que cuando volvamos a entrar a la app, no hayan pasado aun 30 mins desde
    que jugamos al ultimo reto, entonces debemos actualizar el panel del tiempo*/
    private void ActualizarTiempo(string oldHour, string oldMinute,string newHour,string newMinute,string newSecond, string oldSecond)
    {
        /*Miramos cuantos minutos han pasado*/
        if (int.Parse(oldHour) < int.Parse(newHour))
        {
            timeChallengeLeft = (60 * 30)-(60* (60-int.Parse(oldMinute) + int.Parse(newMinute))) + System.Math.Abs(int.Parse(newSecond) - int.Parse(oldSecond));
        }
        else
        {
            int minDifference = (int.Parse(newMinute) - int.Parse(oldMinute));
            int secsDifference = (int.Parse(newSecond) - int.Parse(oldSecond));
            timeChallengeLeft = (60 * 30) - (Mathf.Abs(minDifference) * 60) - System.Math.Abs(secsDifference);
        }
        /*Actualizamos el temporizador*/
        ActivateChallengeWait();
    }

    /*Comprbamos si han pasado 30 mins desde que hemos hecho el ultimo challenge*/
    public bool morethan30Mins(string oldDate,string thisDate)
    {
        if (oldDate != null)
        {
            string date1 = thisDate.Split(" "[0])[0];
            string date2 = oldDate.Split(" "[0])[0];
            if (date1 != date2) return true;
            else
            {
                /*En caso de que las fechas sean iguales , nos fijamos en la hora*/
                string hour1 = thisDate.Split(char.Parse(" "))[1].Split(char.Parse(":"))[0];
                string hour2 = oldDate.Split(char.Parse(" "))[1].Split(char.Parse(":"))[0];

                string min1 = thisDate.Split(char.Parse(" "))[1].Split(char.Parse(":"))[1];
                string min2 = oldDate.Split(char.Parse(" "))[1].Split(char.Parse(":"))[1];

                //Comprobamos que hayan pasado 30 minutos
                if (int.Parse(min2) + 30 <= int.Parse(min1))
                {
                    return true;
                }
                else if (int.Parse(hour1) > int.Parse(hour2) && ((60 - int.Parse(min2) + int.Parse(min1)) > 30)) return true;
                else return false;
            }
        }
        else return true;
    }

    /*Vamos a la escena "scene"*/
    private void GoToScene(string scene)
    {
        SceneManager.LoadScene(scene);
    }

    /*Vamos a la escena "scene" despues de un tiempo de delay*/
    IEnumerator GoToSceneDelay(string scene, float delay)
    {
        yield return new WaitForSeconds(delay);
        SceneManager.LoadScene(scene);
    }

    //Este método se llama siempre que se carga una escena nueva y sirve sobre todo para inicializar GO de la escena (como los textos)
    //en función de variables del código
    private void OnSceneLoaded(Scene scene, LoadSceneMode mode)
    {
        GameObject monedas = GameObject.Find("Numero");
        GameObject dificultad = GameObject.Find("Dificultad");

        switch (scene.name)
        {
                //Pantalla de carga
            case "Loading":
                StartCoroutine(GoToSceneDelay("Menu", 1f));
                break;
                //Menu
            case "Menu":
                GameObject gamemodes = GameObject.Find("Gamemodes");

                if (gamemodes != null)
                {
                    for (int i = 0; i < gamemodes.transform.childCount - 1; i++)
                        gamemodes.transform.GetChild(i).GetChild(1).GetComponent<Text>().text = levelprogress[i].ToString() + "/100";
                    gamemodes.transform.GetChild(gamemodes.transform.childCount - 1).GetChild(2).GetComponent<Text>().text = challengeCount.ToString();
                }
                if(monedas != null)
                    monedas.GetComponent<Text>().text = coinNo.ToString();

                if(wasChallenge) ActivateChallengeWait();

                /*En caso de que no se haya completando los 30 mins desde
                la ultima vez que jugamos el reto*/
                string newtime = System.DateTime.Now.ToString("MM/dd/yyyy H:mm:ss");
                if ((!morethan30Mins(ultimoRetoJugado, newtime) && wasChallenge) || waiting == 1)
                {
                    /*En caso de que las fechas sean iguales , nos fijamos en la hora*/
                    string hour1 = newtime.Split(char.Parse(" "))[1].Split(char.Parse(":"))[0];
                    string hour2 = ultimoRetoJugado.Split(char.Parse(" "))[1].Split(char.Parse(":"))[0];

                    string min1 = newtime.Split(char.Parse(" "))[1].Split(char.Parse(":"))[1];
                    string min2 = ultimoRetoJugado.Split(char.Parse(" "))[1].Split(char.Parse(":"))[1];

                    string sec1 = newtime.Split(char.Parse(" "))[1].Split(char.Parse(":"))[2];
                    string sec2 = ultimoRetoJugado.Split(char.Parse(" "))[1].Split(char.Parse(":"))[2];

                    ActualizarTiempo(hour2, min2, hour1, min1, sec1, sec2);
                }
                else ultimoRetoJugado = newtime;
                break;
                //Selección de nivel
            case "Seleccion": 
                if (dificultad != null)
                    dificultad.GetComponent<Text>().text = difficulties[actualDifficulty - 1];
                break;
                //Nivel
            case "Nivel":
                if (dificultad != null && !challenge)
                    dificultad.GetComponent<Text>().text = difficulties[actualDifficulty - 1] + " " + actualLevel.ToString();
                if (monedas != null && !challenge)
                    monedas.GetComponent<Text>().text = coinNo.ToString();
                else
                {
                    dificultad.GetComponent<Text>().text = "CHALLENGE";

                    //Elementos que desactivamos del canvas
                    GameObject monedasCanvas = GameObject.Find("Monedas");
                    monedasCanvas.SetActive(false);

                    GameObject ReiniciarCanvas = GameObject.Find("Reiniciar");
                    ReiniciarCanvas.SetActive(false);

                    GameObject anunciosCanvas = GameObject.Find("Anuncio");
                    anunciosCanvas.SetActive(false);

                    GameObject pistaCanvas = GameObject.Find("Pista");
                    pistaCanvas.SetActive(false);

                    //Hacemos visible el contador
                    GameObject.Find("Canvas").transform.Find("Contador").gameObject.SetActive(true);


                }
                break;
            default:
                break;
        }
    }
}
