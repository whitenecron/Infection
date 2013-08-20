/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.lang.Object;
import java.lang.Math;
import java.math.*;
import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.text.*;
import java.util.Vector;
import java.awt.event.*;
import java.text.Normalizer.Form;
//import javaapplication2.game;
/**
 *
 * @author alchemist
 */
public class Infection extends JFrame{

    /**
     * @param args the command line arguments
     */
    final int radiusCity=30;// отображаемый радиус города 
    static Button Next=new Button("Next turn");
    static Button Hill=new Button("Hill");
    static Button Chart=new Button("Chart");
    static Button Build=new Button("Build");
    static Button Vactine=new Button("Vactine");
    static Button Teleport=new Button("Teleport");
    static Image Background;
    static game logic; // класс игры
    //static Infection form;
    static int actCard; // номер активной карты
    Infection(String s, int iNumGamers, int iLevel){
        super(s);
        logic = new game(iNumGamers, iLevel);// создание класса игры
        getContentPane().setBackground(Color.WHITE);
        Toolkit tool= getToolkit();
        Background = tool.createImage(Canvas.class.getResource("/javaapplication2/mainback.jpg"));
        setSize(1200, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        c.setSize(400,400);
        addMouseListener(new CustomListener());
        Next.setSize(200, 40);
        Next.setVisible(true);
        c.add(Next);
        Hill.setSize(200, 40);
        Hill.setVisible(true);
        c.add(Hill);
        Chart.setSize(200, 40);
        Chart.setVisible(true);
        c.add(Chart);
        Build.setSize(200, 40);
        Build.setVisible(true);
        c.add(Build);
        Vactine.setSize(200, 40);
        Vactine.setVisible(true);
        c.add(Vactine);
        Teleport.setSize(200, 40);
        Teleport.setVisible(true);
        c.add(Teleport);        
        //setSize(1000, 700);
        actCard=-1;
    }
    
    
    public void paint(Graphics g)
    {        
        //Hill.setLocation(getWidth()-100, 0);
        int H=getHeight(),W=getWidth();
        g.clearRect(0, 0, W, H);
        
        g.drawImage(Background,0,0,W,H,this);
        g.setColor(Color.red);
        Vector<track> Trackers = logic.getTrackers();
        int num=Trackers.size();
        // отображение линий путей
        for(int i=0;i<num;i++){
            int XBegin=Trackers.get(i).XBegin;
            int YBegin=Trackers.get(i).YBegin;
            int XEnd=Trackers.get(i).XEnd;
            int YEnd=Trackers.get(i).YEnd;
            g.drawLine(XBegin*W/1000+radiusCity/2, YBegin*H/1000+radiusCity/2, XEnd*W/1000+radiusCity/2, YEnd*H/1000+radiusCity/2);
        }
        // отображение городов
        Vector<City> Cities = logic.getCities();
        num=Cities.size();
        for(int i=0; i<num; i++)
        {
            setColor(g, Cities.get(i).getColor());
            
            int X=Cities.get(i).getX();
            int Y=Cities.get(i).getY();
            g.drawOval(X*W/1000, Y*H/1000,radiusCity,radiusCity);
            Integer Alert = Cities.get(i).getAlert();
            g.setColor(Color.BLACK);
            g.drawString(Alert.toString(),X*W/1000+radiusCity/2, Y*H/1000+radiusCity/2);
            String Name = Cities.get(i).getName();
            g.drawString(Name,X*W/1000+radiusCity, Y*H/1000+radiusCity);
            if(Cities.get(i).isLab()){
                g.drawLine(X*W/1000+radiusCity/2, Y*H/1000+radiusCity/2,X*W/1000+radiusCity/2, Y*H/1000-radiusCity/2);
            }
        }
        //отображение игроков 
        Vector<gamer> Gamers = logic.getGamers();
        for(int i=0; i<Gamers.size();i++){
            int city=Gamers.get(i).getPosition();
            int X=Cities.get(city).X;
            int X1=(int)(radiusCity*0.75*Math.cos(6.28*(Gamers.get(i).getRoleNum()-1.5)/6))+radiusCity/4;
            int Y=Cities.get(city).Y;
            int Y1=(int)(radiusCity*0.75*Math.sin(6.28*(Gamers.get(i).getRoleNum()-1.5)/6))+radiusCity/4;
            if(logic.getActGamer()==i){
                g.setColor(Color.RED);
                g.fillOval(X*W/1000+X1, Y*H/1000+Y1,radiusCity/2,radiusCity/2);
                g.setColor(Color.BLACK);
            }
            else{
                g.fillOval(X*W/1000+X1, Y*H/1000+Y1,radiusCity/2,radiusCity/2);
            }
        }
        
        // отображение информирующей строки
        
        Font H1=new Font(null, num, 50);
        Font normal = new Font(null, num, 12);
        g.setFont(H1);
        String strTurn;
        if(logic.isWin()){
           strTurn = "WE WIIIIIIIIIIIIIIIIIIIIIIIIIIIN";
        }
        else if(logic.isLosing()){
           strTurn = "WE LOOOOOOOOOOOOOOOOOOOOOOOOOSING"; 
        }
        else{
           strTurn = Gamers.get(logic.getActGamer()).getRoleStr()+
                "'s turn("+logic.getActions()+") remaining cards - " 
                   + logic.remCards ;
        }
        g.clearRect(0, 40, W,80);
        g.drawString(strTurn, 0, 100);
        g.setFont(normal);
        
        //новое отображение карт на руках
        Vector<Integer> Arm = logic.getArm();
        int n=Arm.size();
        for(int i=0;i<9;i++){
            if(i<n){
                String s=Cities.get(Arm.get(i)).getName();
                //Cards.get(i).setLocation(0,800);
                int color=Cities.get(Arm.get(i)).getColor();
                setColor(g,color);
                g.fillRoundRect(50,(H-10)-(i+1)*35, 130, 25,40,40);
                g.fillRoundRect(10,(H-10)-(i+1)*35, 30, 25,20,20);
                if(color<2){
                    g.setColor(Color.WHITE);
                }
                else{
                    g.setColor(Color.BLACK);
                }
                g.drawString(s, 70, (H-10)-(i+1)*35+20);
                g.drawString("<-", 15, (H-10)-(i+1)*35+20);
            }  
        }
        
        
        
        // отображение лекарств
        for(int i=0; i<4; i++){
            if(logic.getVactine(i)){
                setColor(g, i);
                g.fillOval(i*radiusCity*3+W/3,H-radiusCity*3,radiusCity*2,radiusCity*2);
            }
        }
    }
   
    private void setColor(Graphics g, int Num){
       switch(Num){
       case 0:    
          g.setColor(Color.BLACK); break;
       case 1:
          g.setColor(Color.BLUE); break;
       case 2:
          g.setColor(Color.YELLOW); break;
       case 3:
          g.setColor(Color.ORANGE); break;
       }
    }
    
    // обработка нажатия кнопок
    public boolean action(Event evt, Object arg) {

        if(arg=="Next turn"){
            logic.nextTurn();
            actCard=-1;
        }
        else if(arg=="Hill"){
            logic.Hill();
        }
        else if(arg=="Reset"){
            if (actCard>=0){
                Vector<Integer> Arm=logic.getArm();
                for(int i=0; i<Arm.size();i++)
                {
                    if(Arm.get(i)==actCard){
                        Arm.remove(i);
                        break;
                    }
                }
            }
            actCard=-1;
        }
        else if(arg=="MoveTo"){
            if (actCard>=0){
                if(logic.Move(actCard)){
                    Vector<Integer> Arm=logic.getArm();
                    for(int i=0; i<Arm.size();i++)
                    {
                        if(Arm.get(i)==actCard){
                            Arm.remove(i);
                            break;
                        }
                    }
                }
                actCard=-1;
            }
        }
         else if(arg=="Chart"){
            logic.Chart();
        }
         else if(arg=="Build"){
             logic.BuildLab();
         }
         else if(arg=="Teleport"){
             logic.Teleport();
         }
         else if(arg=="Vactine"){
             logic.Vactine();
         }
        else{
            actCard=logic.FindCity(arg.toString());
        }
        repaint();
        return true;
    }
    
    
    class CustomListener implements MouseListener {
          
          //событи происходящее при клике мыши
          public void mouseClicked(MouseEvent e) {
              int X=e.getX()-radiusCity/2;
              int Y=e.getY()-radiusCity/2;
              // обработка нажатия кнопок
              
              //g.fillRoundRect(50,(H-10)-(i+1)*35, 130, 25,40,40);
              //g.fillRoundRect(10,(H-10)-(i+1)*35, 30, 25,20,20);
              
              int ActCard=(getHeight()-Y+5)/35-1;
              Vector<Integer> Arm=logic.getArm();
              //сброс
              if(X>=0 && X<=30){
                Arm.remove(ActCard);
              }
              else if(X>=40 && X<=170){
                if(logic.Move(Arm.get(ActCard))){
                  Arm.remove(ActCard);
                }
              }     
              logic.onClick(X*1000/getWidth(),Y*1000/getHeight(),
                      radiusCity*1000/getWidth(),
                      radiusCity*1000/getHeight());
              repaint();
          }

          public void mouseEntered(MouseEvent e) {
               
          }

          public void mouseExited(MouseEvent e) {
              
          }

          public void mousePressed(MouseEvent e) {
              
          }

          public void mouseReleased(MouseEvent e) {
               
          }
        
        
    }
    /*public static void main(String[] args) {
        // TODO code application logic here
        //String s=NumGamers.getText();
        logic = new game(3,4);// создание класса игры
        form = new Infection("Infection"); // создание окна программы
        
    }*/
  /*  public static void main(String[] args) {
          javax.swing.SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    JFrame frame = new TestFrame();
                    frame.setPreferredSize(new Dimension(330, 160));
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
               }
          });
     }*/
     
     // обработчик действий мыши
     
}
