/*
 * Модуль хранения городов 
 */
package javaapplication2;

/**
 *
 * @author alchemist
 */
public class City {
    /*
     * класс для хранения городов
     */
    String Name; //название города
    int X,Y; // координаты города на мировой карте
    int Alert; // уровень заражения города
    int Color; /*0-чёрный 1-синий 2-жёлтый 3-оранжевый
     * каким вирусом заражён конкретный город
     */
    boolean Laboratory; // флаг, определяющий построена ли в городе лаборатория
    City(String name,int x, int y,int color){
        Name=name;
        X=x;
        Y=y;
        Alert=0;
        Laboratory=false;
        Color=color;
    }
    int getX(){
        return X;
    }
    int getY(){
        return Y;
    }
    int getAlert(){
        return Alert;
    }
    int getColor(){
        return Color;
    }
    String getName(){
        return Name;
    }
    /* повышение уровня заражения города, в случае эпидемии
     * (заражением вирусом соседних городов) возвращает true
     * если вирус не покидает границы города возвращает false
     */
    boolean addInfect(){
        if (Alert<3){
            Alert++;
            return false;
        }
        else{
            //Alert++;
            return true;
        }
    }
    /* лечение города уменьшение уровня заражения на 1
     * если город не заражён возвращает false
     */
    boolean HillOne()
    {
        if(Alert>0){
            Alert--;
            return true;
        }
        else
        {
            return false;
        }
    }
    /* полное лечение города
     * если город не заражён возвращает false
     */
    boolean HillAll()
    {
        if(Alert>0){
            Alert=0;
            return true;
        }
        else
        {
            return false;
        }
    }
    /* постройка лаборатории
     * возвращает true если все условия соблюдены для постройки лаборатории
     */
    boolean BuildLab()
    {
        if (Laboratory==false){
            Laboratory=true;
            return true;
        }
        else{
            return false;
        }
    }
    boolean isLab(){
        return Laboratory;
    }
}
