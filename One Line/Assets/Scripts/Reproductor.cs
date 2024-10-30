using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Reproductor : MonoBehaviour
{
    //Nombre del Script
    private string name;
    void Awake()
    {
        Object.DontDestroyOnLoad(gameObject);
        name = this.gameObject.name;
    }
    public string getReproductorName() { return name; }
}
