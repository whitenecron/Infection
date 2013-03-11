/*
 * Модуль хранения игроков
 */
package javaapplication2;


import java.util.Vector;
/**
 *
 * @author alchemist
 */
public class gamer {
    //String Name;
    int Role; /*1-врач 2-учёный 3-руководитель 4-исследователь 5-диспетчер
     * класс персонажа каждый имеет свои преимущества
     */
    int Position; // позиция персонажа
    Vector<Integer> Arm; // карты на руке у игрока
    gamer(int role, int position){
        Arm = new Vector(); 
        //Name=name;
        Role=role;
        Position=position;
    }
    // передвижение персонажа
    void Move(int position){
        Position=position;
    }
    int getPosition(){
        return Position;
    }
    // получение первого символа названия класса персонажа
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
    // возвращает полное название класса персонажа
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
    // возвращает номер класса персонажа
    int getRoleNum(){
        return Role;
    }
    Vector<Integer> getArm(){
        return Arm;
    }
    // взять карту s в руку
    void addCard(int s){
        Arm.add(s);
    }
}
