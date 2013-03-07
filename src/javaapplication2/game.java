/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    int NumGamers,Level;
    int ActGamer;
    int Actions;
    int Rate;
    int NumLaboratory;
    int numEpidemic;
    boolean boolVactine[];
    String Mode;
    Vector<Integer> StrikeTurn;
    Vector<gamer> Gamers=new Vector();
    Vector<City> Cities=new Vector();
    Vector<track> trackers=new Vector();
    /*Connection conn;
    Statement stmt;
    ResultSet rs;*/
    String userName = "root";
    String password = "sandro23";
    String url = "jdbc:mysql://localhost/pandemija";
    game(int numgamers, int level){
        boolVactine=new boolean[4];
        for(int i=0;i<4;i++){
            boolVactine[i]=false;
        }
        numEpidemic=level+3;
        StrikeTurn=new Vector();
        Mode="";
        Rate=4;
        Random rnd= new Random();
           
        
        /*conn = null;
        try
        {
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
            conn = DriverManager.getConnection (url, userName, password);            
        }
        catch (Exception e)
        {
            int n=5;
        }*/
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
            while((FileStr=Input.readLine())!=null){
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
        Cities.get(0).BuildLab();
            for(int z=0;z<3;z++){
                for(int i=1;i<4;i++){
                    int City=rnd.nextInt(Cities.size());
                    for(int j=0; j<i;j++)
                    {
                        Infection(City);
                    }
                }
            }
        /*try
        {
            
            
            stmt= conn.createStatement();
            rs=stmt.executeQuery("Select * from city");
            while(rs.next()){
                String Name=rs.getString("Name");
                int x=rs.getInt("x");
                int y=rs.getInt("y");
                int color=rs.getInt("Color");
                City temp=new City(Name,x,y,color);
                Cities.add(temp);
            }
            rs=stmt.executeQuery("Select * from track");
            while(rs.next()){
                int begin=rs.getInt("begin");
                int end=rs.getInt("end");
                City Begin=Cities.get(begin-1);
                City End=Cities.get(end-1);
                track temp2=new track(begin,end,Begin.getX(),Begin.getY(),End.getX(),End.getY());
                trackers.add(temp2);
            }
            
            
            //stmt.executeQuery("");
            //rs=stmt.executeQuery("select * from track_downloads");
            //System.out.print("Correct");
        }
        catch (Exception e)
        {
            System.err.println ("Incorrect command"); 
        }*/
        for(int i=0;i<numgamers;i++){
            gamer temp=new gamer("sandro", i+1, 0);
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
    void onClick(int x, int y, int radiusX, int radiusY){
        int Num=trackers.size();
        int begin = Gamers.get(ActGamer).Position;
        if(Actions>0){
            if(Mode==""){
                for(int i=0; i<Num; i++){
                    if(trackers.get(i).getBegin()==begin){
                        int end=trackers.get(i).getEnd();
                        track temp=trackers.get(i);
                        if((temp.getXEnd()>x-radiusX) && (temp.getXEnd()<x+radiusX) && 
                                (temp.getYEnd()>y-radiusY) && (temp.getYEnd()<=y+radiusY)){
                                Gamers.get(ActGamer).Move(end);
                                Actions--;
                                break;
                        }
                    }
                    else if(trackers.get(i).getEnd()==begin){
                        int end=trackers.get(i).getBegin();
                        track temp=trackers.get(i);
                        if((temp.getXBegin()>x-radiusX) && (temp.getXBegin()<x+radiusX) && 
                            (temp.getYBegin()>y-radiusY) && (temp.getYBegin()<=y+radiusY)){
                            Gamers.get(ActGamer).Move(end);
                            Actions--;
                            break;
                        }   
                    }
                }
            }
            else if(Mode=="Chart"){
                for(int i=0; i<Cities.size(); i++){
                    City temp=Cities.get(i);
                    if(temp.getX()>x-radiusX && temp.getX()<x+radiusX && 
                            temp.getY()>y-radiusY && temp.getY()<y+radiusY){
                        Gamers.get(ActGamer).Move(i);
                        Actions--;
                        break;
                    }
                }
            }
            else if(Mode=="Teleport"){
                for(int i=0; i<Cities.size(); i++){
                    City temp=Cities.get(i);
                    if(temp.getX()>x-radiusX && temp.getX()<x+radiusX && 
                            temp.getY()>y-radiusY && temp.getY()<y+radiusY){
                        if(temp.isLab()){
                            Gamers.get(ActGamer).Move(i); 
                            Actions--;
                            break;
                        }
                    }
                }
                Mode="";
            }
        }
       /* try
        {
            //int begin = Gamers.get(ActGamer).Position+1;
            int end=0;
            if (Actions>0) {
                stmt= conn.createStatement();
                rs=stmt.executeQuery("Select * from city where x>" +(x-radiusX)+
                    " and x<"+(x+radiusX)+" and y>"+(y-radiusY)+
                    " and y<"+(y+radiusY));
                if(rs.next()){
                    if(Mode==""){
                        end = rs.getInt("id");
                        rs=stmt.executeQuery("Select * from track where (begin = "+
                            begin +" and end = " + end + ") or (begin="+end
                            +" and end="+begin+")");
                        if(rs.next()){
                            Gamers.get(ActGamer).Move(end-1);
                            Actions--;
                        }
                    }
                    else if(Mode=="Chart"){
                        end = rs.getInt("id");
                        Gamers.get(ActGamer).Move(end-1);
                        Actions--;
                        Mode="";
                    }
                    else if(Mode=="Teleport"){
                        end = rs.getInt("id");
                        if(Cities.get(end-1).isLab()){
                            Gamers.get(ActGamer).Move(end-1);
                            Actions--;
                            Mode="";
                        }
                    }
                }
            }                     
        }
        catch(Exception e){
            int n=5;
        }*/
    }
    void nextTurn()
    {
        int n=Gamers.get(ActGamer).Arm.size();
        if(n<8){
            StrikeTurn.removeAllElements();
            Actions=4;
            Random rnd= new Random();
            for(int i=0;i<2;i++){
                int temp=rnd.nextInt(Cities.size());   
                Infection(temp);
            }
            for(int i=0;i<2;i++){
                int temp=rnd.nextInt(Cities.size()+numEpidemic);   
                if(temp<Cities.size()){
                    Gamers.get(ActGamer).addCard(temp);
                }
                else{
                    int City=rnd.nextInt(Cities.size());
                    Infection(City);
                    Infection(City);
                    Infection(City);
                }
            }
            if(ActGamer<NumGamers-1){
                ActGamer++;
            }
            else{
                ActGamer=0;
            }
        }
    }
    void Infection(int City){
        StrikeTurn.add(City);
        if(Cities.get(City).addInfect()){
            for(int i=0; i<trackers.size(); i++){
               if(trackers.get(i).getBegin()==City){
                  int end=trackers.get(i).getEnd();
                  Infection(end);   
               }
               else if(trackers.get(i).getEnd()==City){
                  int end=trackers.get(i).getBegin();
                  Infection(end);    
               }
            }
            /*try{
                stmt= conn.createStatement();
                rs=stmt.executeQuery("Select * from track where begin = " + (City+1));
                while(rs.next()){
                    int temp=rs.getInt("end")-1;
                    boolean flag=false;
                    for(int i=0; i<StrikeTurn.size(); i++){
                        if(StrikeTurn.get(i)==temp){
                            flag=true;
                            break;
                        }
                    }
                    if(!flag){
                        Infection(temp);
                    }
                }
                rs=stmt.executeQuery("Select * from track where end = " + (City+1));
                while(rs.next()){
                    int temp=rs.getInt("begin")-1;
                    boolean flag=false;
                    for(int i=0; i<StrikeTurn.size(); i++){
                        if(StrikeTurn.get(i)==temp){
                            flag=true;
                            break;
                        }
                    }
                    if(!flag){
                        Infection(temp);
                    }
                }
            }
            catch(SQLException e){
                    
            }*/
        }       
    }
    void Hill()
    {
       int NumCity = Gamers.get(ActGamer).getPosition();
       if(Actions>0 && Cities.get(NumCity).getAlert()>0){     
         if(boolVactine[Cities.get(NumCity).getColor()]){
            if(Gamers.get(ActGamer).getRole()=='D'){
                Cities.get(NumCity).HillAll();
            }
            else{
                Cities.get(NumCity).HillAll();
                Actions--;
            } 
         }
         else{
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
    int FindCity(String s){
        int ret=-1;
        for(int i=0;i<Cities.size();i++){
            if(Cities.get(i).getName()==s){
                ret=i;
            }
        }
        return ret;
    }
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
    void BuildLab()
    {
        if(NumLaboratory<5){
          int pos = Gamers.get(ActGamer).getPosition();
          if(Gamers.get(ActGamer).getRole()=='L'){
             if(Cities.get(pos).BuildLab()){
                   Actions--;
                   NumLaboratory++;
             }
          }
         else
         {
            boolean flag=false;
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
    void Teleport(){
        int Current=Gamers.get(ActGamer).getPosition();
        if(Cities.get(Current).isLab()){
            Mode="Teleport";;
        }      
    }
    void Vactine(){
        if(Actions>0){
        int Current=Gamers.get(ActGamer).getPosition();   
            if(Cities.get(Current).isLab()){
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
                for(int i=0;i<Arm.size();i++){
                    int Num=Arm.get(i);
                    a[Cities.get(Num).getColor()]++;
                    if (a[Cities.get(Num).getColor()]>=NeadCard){
                        ret=Cities.get(Num).getColor();
                        break;
                    }
                }
                if(ret>=0 && (!boolVactine[ret])){
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
                    boolVactine[ret]=true;
                    Actions--;
                }
            }
        }
    }
    int getActGamer(){
       return ActGamer; 
    }
}
