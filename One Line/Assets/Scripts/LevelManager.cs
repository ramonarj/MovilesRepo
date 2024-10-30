using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class LevelManager : MonoBehaviour
{
    [Tooltip("El número de columnas a mostrar")]
    public int NUM_COLS;

    //Margenes
    float MARGIN;
    float GAP;

    [Tooltip("El prefab del Nivel")]
    public GameObject levelPrefab;
    [Tooltip("Sonido al pulsar el nivel")]
    public AudioClip clickSound;

    void Start()
    {
        //Dificultad en la que nos encontramos
        int difficulty = GameManager.Instance().getActualDifficulty();

        //Número de nivel hasta el que queremos bloquear
        int maxLevel = GameManager.Instance().getLevelProgress(difficulty);
        int numLevels = GameManager.Instance().getNumberOfLevels(difficulty);

        float horUnits = 10f * (float)Screen.width /(float) Screen.height;

        //Márgenes
        float levelPixels = (float)Screen.width / horUnits;
        GAP = (float)Screen.width / (float)(NUM_COLS + 1);
        MARGIN = (float)Screen.width / 6f;

        //Creamos los sprites de los niveles
        int rows = numLevels / NUM_COLS;
        int count = 0;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < NUM_COLS; j++)
            {
                //Creamos el objeto
                GameObject o = Instantiate(levelPrefab, transform);

                //Nombre y posición
                int c = count + 1; //NECESARIO PORQUE SI LE PASAS COUNT AL CALLBACK SE QUEDA CON EL VALOR DEL FINAL (100), 
                                    //Y SE INTENTA JUGAR AL NIVEL 101
                o.name = c.ToString();
                o.transform.position = new Vector3(MARGIN + j * GAP, (4f * Screen.height / 5f) - MARGIN - i * GAP); //Posicion

                //Depende de si está desbloqueado o no
                if (count <= maxLevel)
                {
                    //Número que le corresponde
                    o.transform.GetChild(1).GetComponent<UnityEngine.UI.Text>().text = c.ToString("000"); 
                    //Callbacks
                    o.GetComponent<Button>().onClick.AddListener(() => GameManager.Instance().GoToLevel(c)); 
                    o.GetComponent<Button>().onClick.AddListener(() => GameManager.Instance().playSound(clickSound));
                }
                else
                    o.transform.GetChild(2).gameObject.SetActive(true); //Activamos el candado

                count++;
            }
        }
    }
}
