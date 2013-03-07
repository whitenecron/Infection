/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;


import java.util.Vector;
/**
 *
 * @author alchemist
 */
public class gamer {
    String Name;
    int Role; // 1-врач 2-учёный 3-руководитель 4-исследователь 5-диспетчер
    int Position; 
    Vector<Integer> Arm;
    gamer(String name, int role, int position){
        Arm = new Vector(); 
        Name=name;
        Role=role;
        Position=position;
    }
    void Move(int position){
        Position=position;
    }
    int getPosition(){
        return Position;
    }
    char getRole(){
        char ret;
        switch(Role){
            case 1:
                ret='D'; break;
            case 2:
                ret='S'; break;
            case 3:
                ret='L'; break;
            case 4:
                ret='R'; break;
            default:
                ret='M'; break;
        }
        return ret;
    }
    String getRoleStr(){
        String ret;
        switch(Role){
            case 1:
                ret="Doctor"; break;
            case 2:
                ret="Scientist"; break;
            case 3:
                ret="Lider"; break;
            case 4:
                ret="Researcher"; break;
            default:
                ret="Manager"; break;
        }
        return ret;
    }
    int getRoleNum(){
        return Role;
    }
    Vector<Integer> getArm(){
        return Arm;
    }
    void addCard(int s){
        Arm.add(s);
    }
}
