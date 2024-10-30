using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.IO;
using System.Text;

public class GameSaving
    {
    /*Atributos que serializaremos*/
    [SerializeField] public List<int> levels;
    [SerializeField] public int coins;
    [SerializeField] public int waiting; //Conocer si estas o no esperando el challenge
    [SerializeField] public int challenge;
    [SerializeField] public string hash;
    [SerializeField] public string dateTime;

}

[System.Serializable]
public class SaveDataManager : MonoBehaviour
{
    private static SaveDataManager instance;
    private string jsonSavePath;
    private const string abc= "abcdefghijklmñnopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ1234567890_ - +,#$%&/()=¿?¡!|,.;:{}[]";

    GameSaving game;

    public GameSaving getGame() { return game; }


    public static SaveDataManager Instance()
    {
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

    /*Configuramos el path segun donde se encuentre la app*/
    void Start()
    {
        jsonSavePath = Application.persistentDataPath + "/save.json";  
    }

    /*Hacemos una instancia de Game donde guardaremos los datos cuando salgamos del juego*/
    private void OnEnable()
    {
        game = new GameSaving();
    }

    /*Serializamos la clase*/
    public void save(List<int> levels_,int coins_,int waiting_,int challenge_, string dateTime)
    {
        game.levels = levels_;
        game.coins = coins_;
        game.waiting = waiting_;
        game.challenge = challenge_;
        game.dateTime = dateTime;
        
        //Cremos el hash concantenando el contenido de la clase y anadiedo una sal al final
        game.hash = createHash((concatenateLevels(levels_) + coins_ + waiting_ +getString(GameManager.Instance().getString(),GameManager.Instance().getNumber())+challenge_+dateTime).ToString());
        //Rellenamos el json y lo guardamos
        string jsonData = JsonUtility.ToJson(game);
        File.WriteAllText(Application.persistentDataPath + "/save.json", jsonData);
    }

    //Devuelve el objeto creado leyendo el Json especificado
    public bool load()
    {
        game = JsonUtility.FromJson<GameSaving>(File.ReadAllText(Application.persistentDataPath + "/save.json"));
        return true;
    }

    public string createHash(string str)
    {
        System.Security.Cryptography.SHA256Managed crypt = new System.Security.Cryptography.SHA256Managed();
       
        System.Text.StringBuilder hash = new System.Text.StringBuilder();
        byte[] crypto = crypt.ComputeHash(Encoding.UTF8.GetBytes(str), 0, Encoding.UTF8.GetByteCount(str));
        foreach (byte bit in crypto)
        {
            hash.Append(bit.ToString("x2"));
        }
        return hash.ToString().ToLower();
    }

    public string getString(string var1, int var2)
    {

        string String = "";
        if (var2 > 0 && var2 < abc.Length)
        {
            for (int i = 0; i < var1.Length; i++)
            { 
                int posCaracter = getPosABC(var1[i]);
                if (posCaracter != -1) 
                {
                    int pos = posCaracter + var2;
                    while (pos >= abc.Length)
                    {
                        pos = pos - abc.Length;
                    }
                    String += abc[pos];
                }
                else String += var1[i];
            }
        }
        return String;
    }

    string getAnotherString(string var1 , int var2)
    {
    
        string String = "";
        if (var2 > 0 && var2 < abc.Length)
        {
            for (int i = 0; i < var1.Length; i++)
            {
                int posCaracter = getPosABC(var1[i]);
                if (posCaracter != -1) 
                {
                    int pos = posCaracter - var2;
                    while (pos < 0)
                    {
                        pos = pos + abc.Length;
                    }
                    String += abc[pos];
                }
                else
                {
                    String += var1[i];
                }
            }

        }
        return String;
    }

    public string concatenateLevels(List<int>levelss)
    {
        string s = "";
        for (int i=0;i<levelss.Count;i++)
        {
            s += levelss[i];
        }
        return s;
    }

    private int getPosABC(char caracter)
    {
        for (int i = 0; i < abc.Length; i++)
        {
            if (caracter == abc[i]) return i;
        }
       
        return -1;
    
    }
}
