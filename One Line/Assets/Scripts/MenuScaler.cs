using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class MenuScaler : MonoBehaviour
{
    float DEFAULT_ASPECT = 9f / 16f;

    // Start is called before the first frame update
    void Start()
    {
        float aspectRatio = (float)Screen.width / (float)Screen.height;
        GameObject middlePanel = GameObject.Find("MiddlePanel");
        float scaleFactor = DEFAULT_ASPECT / aspectRatio;
        middlePanel.transform.localScale = new Vector3(scaleFactor, scaleFactor);
       // middlePanel.GetComponent<RectTransform>().rect.position.y += (1f - scaleFactor) * (float)Screen.height / 2;
        middlePanel.transform.Translate(new Vector3(0,(1-scaleFactor) * (Screen.height / 5)));
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
