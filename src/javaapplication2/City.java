/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

/**
 *
 * @author alchemist
 */
public class City {
    String Name;
    int X,Y;
    int Alert;; 
    int Color;//0-чёрный 1-синий 2-жёлтый 3-оранжевый
    boolean Laboratory;
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
