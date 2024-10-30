using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Utils;

public class Tile : MonoBehaviour
{
    [Tooltip("La imagen que tendra cuando no forme parte del camino")]
    public SpriteRenderer spriteUntoggled;

    [Tooltip("La imagen que tendra cuando forme parte del camino")]
    public SpriteRenderer spriteToggled;

    [Tooltip("Líneas discontinuas en las 4 direcciones que forman un camino más visual")]
    public SpriteRenderer upPath;
    public SpriteRenderer downPath;
    public SpriteRenderer leftPath;
    public SpriteRenderer rightPath;
    public SpriteRenderer upHint;
    public SpriteRenderer downHint;
    public SpriteRenderer leftHint;
    public SpriteRenderer rightHint;

    // Activa el sprite del camino en una dirección
    public void setPath(Direction d)
    {
        //Sprites del camino (depende de la dirección de la que se venga)
        switch (d.dir)
        {
            case DirectionEnum.Up:
                upPath.enabled = true;
                break;
            case DirectionEnum.Down:
                downPath.enabled = true;
                break;
            case DirectionEnum.Left:
                leftPath.enabled = true;
                break;
            case DirectionEnum.Right:
                rightPath.enabled = true;
                break;
            default:
                break;
        }
    }

    // Activa el sprite de la pista en una dirección
    public void setHint(Direction d)
    {
        //Sprites del camino (depende de la dirección de la que se venga)
        switch (d.dir)
        {
            case DirectionEnum.Up:
                upHint.enabled = true;
                break;
            case DirectionEnum.Down:
                downHint.enabled = true;
                break;
            case DirectionEnum.Left:
                leftHint.enabled = true;
                break;
            case DirectionEnum.Right:
                rightHint.enabled = true;
                break;
            default:
                break;
        }
    }

    //Desactiva el sprite del camino de esa dirección
    public void unsetPath(Direction d)
    {
        //Sprites del camino (depende de la dirección de la que se venga)
        switch (d.dir)
        {
            case DirectionEnum.Up:
                upPath.enabled = false;
                break;
            case DirectionEnum.Down:
                downPath.enabled = false;
                break;
            case DirectionEnum.Left:
                leftPath.enabled = false;
                break;
            case DirectionEnum.Right:
                rightPath.enabled = false;
                break;
            default:
                break;
        }
    }

    //Desactiva todos los caminos
    public void unsetAllPaths()
    {
        //Sprites del camino
        upPath.enabled = false;
        downPath.enabled = false;
        leftPath.enabled = false;
        rightPath.enabled = false;
    }

    /* De momento unicamente solo querremos setTouch y unTouch*/
    public void setTouch() //d = dirección de la que se viene
    {
        //Swap
        spriteUntoggled.enabled = false;
        spriteToggled.enabled = true;
    }

    public void setUnTouch()
    {
        //Swap
        spriteUntoggled.enabled = true;
        spriteToggled.enabled = false;
    }
}
