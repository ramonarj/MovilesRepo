using System.Collections;
using System.Collections.Generic;
using System;

namespace Utils
{
    //[System.Serializable]
    //public class IntList
    //{
    //    public IntList()
    //    {
    //        list = new List<int>();
    //    }
    //    public List<int> list;
    //}

    /*Estructura que necesitaremos para tener la correlacion entre el array de booleanos y tiles*/
    [System.Serializable]
    public struct tilePosition
    {
        public int x;
        public int y;

        //Constructora
        public tilePosition(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        //Operadores de igualdad y desigualdad
        public static bool operator ==(tilePosition lhs, tilePosition rhs)
        {
            return (lhs.x == rhs.x && lhs.y == rhs.y);
        }
        public static bool operator !=(tilePosition lhs, tilePosition rhs)
        {
            return !(lhs == rhs);
        }
    }

    public enum DirectionEnum { Up, Down, Left, Right, None };

    // Para las 4 direcciones
    public struct Direction
    {
        public DirectionEnum dir;

        public Direction(DirectionEnum newDir)
        {
            dir = newDir;
        }

        public void setDirection(DirectionEnum newDir)
        {
            dir = newDir;
        }

        public Direction inverse()
        {
            if (dir == DirectionEnum.Right)
                return new Direction(DirectionEnum.Left);
            else if (dir == DirectionEnum.Left)
                return new Direction(DirectionEnum.Right);
            else if (dir == DirectionEnum.Up)
                return new Direction(DirectionEnum.Down);
            else if (dir == DirectionEnum.Down)
                return new Direction(DirectionEnum.Up);
            else
                return new Direction(DirectionEnum.None);
        }
    }
}
