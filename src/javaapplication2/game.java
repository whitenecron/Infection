/*
 * Основной модуль игры
 */
package javaapplication2;

//import java.sql.*;
import java.io.*;
import java.util.Vector;
import java.util.Random;
import java.nio.channels.*;
import java.nio.*;
import java.util.regex.*;
//import javaapplication2.city;

/**
 *
 * @author alchemist
 */
public class game {
    // главный класс игры
    int NumGamers; // количество игроков
    int Level; // уровень сложности
    int ActGamer; // текщий игрок
    int Actions; // количесво оставшихся действий текущего игрока в этом ходу 
    int Rate; 
    int NumLaboratory; // количество лабораторий
    int numEpidemic; // количество карт эпидемий в колоде
    boolean boolVactine[]; // найдено ли лекарство от конкрктного заболевания
    String Mode; // режим перемещения(перемещение, телепортация, чартерный рейс)
    Vector<Integer> StrikeTurn; /* номера городов, в которых 
     * произошли эпидемии на этом ходу
     */
    Vector<gamer> Gamers=new Vector(); /* список игроков*/
    Vector<City> Cities=new Vector(); /* список городов*/
    Vector<track> trackers=new Vector(); /* список путей*/
    game(int numgamers, int level){
        boolVactine=new boolean[4];
        for(int i=0;i<4;i++){
            boolVactine[i]=false;
        }
        numEpidemic=level+3;/* установка количества эпидемий в колоде
         * относительно уровня сложности
         */     
        StrikeTurn=new Vector();
        Mode="";
        Rate=4;
        Random rnd= new Random();
        
        // считывание из файла и формирование списка городов   
        try{
            BufferedReader Input = new BufferedReader(new FileReader(
                    "src/javaapplication2/City.txt"));
            String FileStr=Input.readLine();
            Integer N=Integer.valueOf(FileStr);
            for(int i=0; i<N;i++){
                FileStr=Input.readLine();
                Pattern PatQuesh=Pattern.compile("[-a-zA-Z_]+ ");
                Matcher MatQuesh=PatQuesh.matcher(FileStr);
                MatQuesh.find();
                String city=MatQuesh.group();
                
                PatQuesh=Pattern.compile("[0-9]+ ");
                MatQuesh=PatQuesh.matcher(FileStr);
                MatQuesh.find();
                String temp=MatQuesh.group();              
                Integer Color=Integer.parseInt(temp.substring(0,
                        temp.length()-1));
                int end=MatQuesh.end();
                MatQuesh.find(end);
                temp=MatQuesh.group();
                Integer x=Integer.parseInt(temp.substring(0, temp.length()-1));
                end=MatQuesh.end();
                PatQuesh=Pattern.compile("[0-9]+");
                MatQuesh=PatQuesh.matcher(FileStr);
                MatQuesh.find(end);
                //int end=MatQuesh.end(end);
                Integer y=Integer.parseInt(MatQuesh.group());
                Cities.add(new City(city,x,y,Color));
            }
            // формирование списка путей между городами
            while(Input.ready()){
                FileStr=Input.readLine(); 
                Pattern PatQuesh=Pattern.compile("[0-9]+ ");
                Matcher MatQuesh=PatQuesh.matcher(FileStr);
                MatQuesh.find();
                String temp=MatQuesh.group();              
                Integer start=Integer.parseInt(temp.substring(0, 
                        temp.length()-1));
                int end=MatQuesh.end();
                PatQuesh=Pattern.compile("[0-9]+ ");
                MatQuesh=PatQuesh.matcher(FileStr);
                MatQuesh.find(end);
                //int end=MatQuesh.end(end);
                temp=MatQuesh.group();
                Integer finish=Integer.parseInt(temp.substring(0, 
                        temp.length()-1));
                trackers.add(new track(start-1, finish-1, Cities.get(start-1).getX(), 
                        Cities.get(start-1).getY(), Cities.get(finish-1).getX(),
                        Cities.get(finish-1).getY()));
            }
            Input.close();
           
            
            
        }
        catch(Exception e){
            int n=5;  
        }
        NumLaboratory=1;
        Cities.get(0).BuildLab(); // постройка 1ой лаборатории в Москве
        // начальное заражение городов
        for(int z=0;z<3;z++){
           for(int i=1;i<4;i++){
              int City=rnd.nextInt(Cities.size());
              for(int j=0; j<i;j++)
              {
                  Infection(City);
              }
            }
        }
        // набор карт в руки игроков
        for(int i=0;i<numgamers;i++){
            gamer temp=new gamer(i+1, 0);
            Gamers.add(temp);
            for(int j=0; j<6-numgamers; j++){
                int tempcity=rnd.nextInt(Cities.size());
                Gamers.get(i).addCard(tempcity);
            }
        }
        NumGamers=numgamers;
        ActGamer=0;
        Level=level;
        Actions=4;
        
    }
    Vector<City> getCities()
    {
        return Cities;
    }
    Vector<track> getTrackers()
    {
        return trackers;
    }
    Vector<gamer> getGamers()
    {
        return Gamers;
    }
    int getActions()
    {
        return Actions;
    }
    /* метод обработки клика в координатах x y
     * int radiusX, int radiusY радиус города на форме
     */
    void onClick(int x, int y, int radiusX, int radiusY){
        int Num=trackers.size();
        int begin = Gamers.get(ActGamer).Position; // начальная позиция игрока
        if(Actions>0){ // если у игрока остальсь действия
            if(Mode==""){ // если выбран режим перемещения(по умолчанию)
                for(int i=0; i<Num; i++){ // ищем города с которыми граничит текущий город
                    if(trackers.get(i).getBegin()==begin){
                        int end=trackers.get(i).getEnd();
                        track temp=trackers.get(i);
                        // проверяем на этот ли город был клик
                        if((temp.getXEnd()>x-radiusX) && (temp.getXEnd()<x+radiusX) && 
                                (temp.getYEnd()>y-radiusY) && (temp.getYEnd()<=y+radiusY)){
                                Gamers.get(ActGamer).Move(end);//перемещение
                                Actions--;
                                break;
                        }
                    }
                    else if(trackers.get(i).getEnd()==begin){/* аналогично для
                     * случая когда начальная позиция совпадает с концом дороги
                     */
                        int end=trackers.get(i).getBegin();
                        track temp=trackers.get(i);
                        if((temp.getXBegin()>x-radiusX) && (temp.getXBegin()<x+radiusX) && 
                            (temp.getYBegin()>y-radiusY) && (temp.getYBegin()<=y+radiusY)){
                            Gamers.get(ActGamer).Move(end);// перемещение
                            Actions--;
                            break;
                        }   
                    }
                }
            }
            else if(Mode=="Chart"){// если режим чартерного рейса
                for(int i=0; i<Cities.size(); i++){// ищем город на котором был клик
                    City temp=Cities.get(i);
                    if(temp.getX()>x-radiusX && temp.getX()<x+radiusX && 
                            temp.getY()>y-radiusY && temp.getY()<y+radiusY){
                        Gamers.get(ActGamer).Move(i); // перемещение в этот город
                        Actions--;
                        break;
                    }
                }
            }
            else if(Mode=="Teleport"){// режим телепортации
                for(int i=0; i<Cities.size(); i++){
                    City temp=Cities.get(i);
                    if(temp.getX()>x-radiusX && temp.getX()<x+radiusX && 
                            temp.getY()>y-radiusY && temp.getY()<y+radiusY){
                        if(temp.isLab()){// если в выбранном городе есть лаборатория
                            Gamers.get(ActGamer).Move(i); 
                            Actions--;
                            break;
                        }
                    }
                }
                Mode="";//режим перемещения
            }
        }
    }
    // следующий ход
    void nextTurn()
    {
        int n=Gamers.get(ActGamer).Arm.size();
        if(n<8){// переход хода осуществляется при наличии у игрока менее 8 карт
            StrikeTurn.removeAllElements();
            Actions=4;
            Random rnd= new Random();
            for(int i=0;i<2;i++){ // разыгрывание карт инфекции
                int temp=rnd.nextInt(Cities.size());   
                Infection(temp);
            }
            for(int i=0;i<2;i++){ // получение карт
                int temp=rnd.nextInt(Cities.size()+numEpidemic);   
                if(temp<Cities.size()){ // если карта города, то в руку
                    Gamers.get(ActGamer).addCard(temp);
                }
                else{ // если карта эпидемии то тройное заражение города
                    int City=rnd.nextInt(Cities.size());
                    Infection(City);
                    Infection(City);
                    Infection(City);
                }
            }
            if(ActGamer<NumGamers-1){// переход хода к следующему игроку
                ActGamer++;
            }
            else{
                ActGamer=0;
            }
        }
    }
    // заражение города City
    void Infection(int City){  
        StrikeTurn.add(City);
        if(Cities.get(City).addInfect()){ // если спровоцировал эпидемию
            for(int i=0; i<trackers.size(); i++){ // заражение соседних городов
               if(trackers.get(i).getBegin()==City){
                  int end=trackers.get(i).getEnd();
                  Infection(end); // рекурсивный обход  
               }
               else if(trackers.get(i).getEnd()==City){
                  int end=trackers.get(i).getBegin();
                  Infection(end); // рекурсивный обход   
               }
            }
        }       
    }
    // лечение города при трате конкретного игрока 1 действия
    void Hill()
    {
       int NumCity = Gamers.get(ActGamer).getPosition();
       if(Actions>0 && Cities.get(NumCity).getAlert()>0){     
         if(boolVactine[Cities.get(NumCity).getColor()]){ // если есть лекарство от болезни
            if(Gamers.get(ActGamer).getRole()=='D'){
                Cities.get(NumCity).HillAll(); 
            }
            else{
                Cities.get(NumCity).HillAll();
                Actions--;
            } 
         }
         else{// если нет лекарства от болезни
            if(Gamers.get(ActGamer).getRole()=='D'){
                Cities.get(NumCity).HillAll(); 
                Actions--;
            }
            else{
                Cities.get(Gamers.get(ActGamer).Position).HillOne();
                Actions--;
            }
         }
      } 
    }
    Vector<Integer> getArm(){
        return Gamers.get(ActGamer).getArm();
    }
    //возвращает номер города с данным названием
    int FindCity(String s){
        int ret=-1;
        for(int i=0;i<Cities.size();i++){
            if(Cities.get(i).getName()==s){
                ret=i;
            }
        }
        return ret;
    }
    // перемещение игрока в город с номером to
    // возвращает false в случае нехватки действий
    boolean Move(int to){
        if (Actions>0){
            Gamers.get(ActGamer).Move(to);
            Actions--;
            return true;
        }
        else{
            return false;
        }
    }
    // активация рещима чартеного перелёта
    void Chart(){
        int Current=Gamers.get(ActGamer).getPosition();
        boolean flag=false;
        for(int i=0;i<Cities.size();i++){
            if(Current==Gamers.get(ActGamer).getArm().get(i)){
                flag=true;
                Gamers.get(ActGamer).getArm().remove(i);
                Mode="Chart";
                break;
            }
        }
        
    }
    // постройка лаборатории в текущем городе
    void BuildLab()
    {
        if(NumLaboratory<5){ // лаборторий не должно быть больше 5
          int pos = Gamers.get(ActGamer).getPosition();
          if(Gamers.get(ActGamer).getRole()=='L'){// если текущий персонаж руководитель
             if(Cities.get(pos).BuildLab()){// ему не  нужна карта
                   Actions--;
                   NumLaboratory++;
             }
          }
          else // если не руководитель
          {
            boolean flag=false;
            // проверка есть ли карта на руках
            for(int i=0;i<Gamers.get(ActGamer).getArm().size();i++){
                if(pos==Gamers.get(ActGamer).getArm().get(i)){
                    if(Cities.get(pos).BuildLab()){
                        flag=true;
                        Gamers.get(ActGamer).getArm().remove(i);
                        Actions--;
                        NumLaboratory++;
                    }
                    break;
                }
            }
          }
        }
    }
    // активация режима телепортации
    void Teleport(){
        int Current=Gamers.get(ActGamer).getPosition();
        if(Cities.get(Current).isLab()){
            Mode="Teleport";;
        }      
    }
    // создание вакцины
    void Vactine(){
        if(Actions>0){//проверка наличия действий
            int Current=Gamers.get(ActGamer).getPosition();   
            if(Cities.get(Current).isLab()){//проверка наличия лаборатории
                //необходимое количество карт для исследования
                int NeadCard;
                if(Gamers.get(ActGamer).getRole()=='S'){
                    NeadCard=4;
                }
                else{
                    NeadCard=5;
                }
                Vector<Integer> Arm = Gamers.get(ActGamer).getArm();
                int a[] = new int[4];
                int ret=-1;
                // подсчёт количества карт разного цвета
                for(int i=0;i<Arm.size();i++){
                    int Num=Arm.get(i);
                    a[Cities.get(Num).getColor()]++;
                    if (a[Cities.get(Num).getColor()]>=NeadCard){
                        ret=Cities.get(Num).getColor();
                        break;
                    }
                }
                if(ret>=0 && (!boolVactine[ret])){
                    // если найден цвет, для которого имеются необходимые карты
                    // удалить карты
                    for(int i=Arm.size()-1;i>=0;i--){
                        int Num=Arm.get(i);
                        if (Cities.get(Num).getColor()==ret){
                            Arm.remove(i);
                            NeadCard--;
                            if(NeadCard==0){
                                break;
                            }
                        //break;
                        }
                    }
                    // изобретение вакцины
                    boolVactine[ret]=true;
                    Actions--;
                }
            }
        }
    }
    int getActGamer(){
       return ActGamer; 
    }
    
    // проверка есть ли вакцина номер Vactine
    boolean getVactine(int  Vactine){
        return boolVactine[Vactine];
    }
}
