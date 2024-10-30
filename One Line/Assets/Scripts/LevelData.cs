using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class LevelDataList
{
    public LevelDataList()
    {
        levels = new List<LevelData>();
    }

    //Devuelve el objeto creado leyendo el Json especificado
    public static LevelDataList CreateFromJSON(string jsonString)
    {
        return JsonUtility.FromJson<LevelDataList>(jsonString);
    }

    //Sobrecargamos el operador de acceso 
    public LevelData this[int i]
    {
        get { return levels[i]; }
        set { levels[i] = value; }
    }

    public List<LevelData> levels;
}

[System.Serializable]
public class LevelData
{
    //Número del nivel (1-500)
    public int index;
    //Disposición del nivel (cada string es una fila del tablero)
    //0 = vacío
    //1 = tile normal
    //2 = tile inicial
    public List<string> layout;
    //Camino a seguir para completar el nivel
    public List<Utils.tilePosition> path;
}