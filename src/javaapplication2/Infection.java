/*
 * Основное окно игры
 * карта мира
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
    
    //кнопки правой панели
    final int SizeBigButton=100; // размер кнопок
    final int NUM_BIG_BUTTON = 5; // их количество
    //индексы
    final int NEXT_TURN = 0; // передача хода 
    final int HILL = NEXT_TURN+1; // лечение города
    final int BUILD = HILL+1; // постройка лаборатории
    final int VACTINE = BUILD+1; // изобретение вакцины
    final int TELEPORT = VACTINE+1; // телепортация между лабораториями
    Image BigButtons[]=new Image[NUM_BIG_BUTTON];// сами кнопки
    
    static Image Background;// карта мира
    static game logic; // класс игры
    //static Infection form;
    static int actCard; // номер активной карты
    static int Left=0,Top=0,Width=500,Height=500; // положение камеры
    
    Infection(String s, int iNumGamers, int iLevel){
        super(s);
        logic = new game(iNumGamers, iLevel);// создание класса игры
        getContentPane().setBackground(Color.WHITE);
        
        //инициализация фоновой картинки и картинок на кнопках правой панели
        Toolkit tool= getToolkit();
        Background = tool.createImage(Canvas.class.getResource("/picture/mainback.jpg"));
        BigButtons[NEXT_TURN]=tool.createImage(Canvas.class.getResource("/picture/nextturn.jpg"));
        BigButtons[HILL]=tool.createImage(Canvas.class.getResource("/picture/hill.jpg"));
        BigButtons[TELEPORT]=tool.createImage(Canvas.class.getResource("/picture/teleport.jpg"));
        BigButtons[VACTINE]=tool.createImage(Canvas.class.getResource("/picture/vactine.jpg"));
        BigButtons[BUILD]=tool.createImage(Canvas.class.getResource("/picture/build.jpg"));
        
        //создание окна
        setSize(1200, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addMouseListener(new CustomListener());      
        //setSize(1000, 700);
        actCard=-1;
    }
    
    //перевод из координат игры в координаты на экране
    int getPaintX(int iLogicX){
        int W=getWidth();
        return (iLogicX-Left)*W/Width;
    }
    
    int getPaintY(int iLogicY){
        int H=getHeight();
        return (iLogicY-Top)*H/Height;
    }
    
    // перерисовка сцены
    public void paint(Graphics g)
    {        
        // очистка экрана
        int H=getHeight(),W=getWidth();
        g.clearRect(0, 0, W, H);
        
        //прорисовка карты мира
        g.drawImage(Background,getPaintX(0),getPaintY(0),getPaintX(1000)-getPaintX(0),getPaintY(1000)-getPaintY(0),this);
        
        //получение массива путей
        g.setColor(Color.red);
        Vector<track> Trackers = logic.getTrackers();
        int num=Trackers.size();
        // отображение линий путей
        for(int i=0;i<num;i++){
            int XBegin=getPaintX(Trackers.get(i).XBegin);
            int YBegin=getPaintY(Trackers.get(i).YBegin);
            int XEnd=getPaintX(Trackers.get(i).XEnd);
            int YEnd=getPaintY(Trackers.get(i).YEnd);
            g.drawLine(XBegin+radiusCity/2, YBegin+radiusCity/2, XEnd+radiusCity/2, YEnd+radiusCity/2);
        }
        // отображение городов
        Vector<City> Cities = logic.getCities();
        num=Cities.size();
        for(int i=0; i<num; i++)
        {
            setColor(g, Cities.get(i).getColor());
            
            int X=Cities.get(i).getX();
            int Y=Cities.get(i).getY();
            g.drawOval(getPaintX(X), getPaintY(Y),radiusCity,radiusCity);
            Integer Alert = Cities.get(i).getAlert();
            g.setColor(Color.BLACK);
            g.drawString(Alert.toString(),getPaintX(X)+radiusCity/2, getPaintY(Y)+radiusCity/2);
            String Name = Cities.get(i).getName();
            g.drawString(Name,getPaintX(X)+radiusCity, getPaintY(Y)+radiusCity);
            if(Cities.get(i).isLab()){
                g.drawLine(getPaintX(X)+radiusCity/2, getPaintY(Y)+radiusCity/2,getPaintX(X)+radiusCity/2, getPaintY(Y)-radiusCity/2);
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
                g.fillOval(getPaintX(X)+X1, getPaintY(Y)+Y1,radiusCity/2,radiusCity/2);
                g.setColor(Color.BLACK);
            }
            else{
                g.fillOval(getPaintX(X)+X1, getPaintY(Y)+Y1,radiusCity/2,radiusCity/2);
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
        for(int i=0;i<9 && i<n;i++){
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
        
        
        
        // отображение лекарств
        for(int i=0; i<4; i++){
            if(logic.getVactine(i)){
                setColor(g, i);
                g.fillOval(i*radiusCity*3+W/3,H-radiusCity*3,radiusCity*2,radiusCity*2);
            }
        }
        
        // отображение уровня эпидемий
        int Danger=logic.getDanger();
        for(int i=0; i<Danger && i<8; i++){
            g.setColor(Color.RED);
            g.fillOval(i*radiusCity*3+W/4,H/4-radiusCity*3,radiusCity*2,radiusCity*2);
        }
        for(int i=Danger; i<8; i++){
            g.setColor(Color.BLUE);
            g.fillOval(i*radiusCity*3+W/4,H/4-radiusCity*3,radiusCity*2,radiusCity*2);
        }
        
        // отображение правой панели кнопок
        for(int i=0;i<NUM_BIG_BUTTON;i++){
            g.drawImage(BigButtons[i],W-SizeBigButton,H-SizeBigButton*(i+1),SizeBigButton,SizeBigButton,this);
        }
    }
   
    // установка цвета в зависимости от индекса вируса Num
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
    
    //обработка клика мыши
    class CustomListener implements MouseListener {
          int XPush=0,YPush=0;
          //событие происходящее при клике мыши
          public void mouseClicked(MouseEvent e) {
              int X=e.getX()-radiusCity/2;
              int Y=e.getY()-radiusCity/2;
              
              //обработка нажатия на кнопки
              int ActCard=(getHeight()-Y+5)/35-1;
              Vector<Integer> Arm=logic.getArm();
              //сброс
              if(X>=0 && X<=30){ // сброс карты
                Arm.remove(ActCard);
              }
              else if(X>=40 && X<=170){ // активация карты (перенос в город)
                if(logic.Move(Arm.get(ActCard))){
                  Arm.remove(ActCard);
                }
              }
              // обработка кнопок правой панели
              else if(X>getWidth()-SizeBigButton-15 && Y>getHeight()-SizeBigButton*NUM_BIG_BUTTON-10){
                int Num=(getHeight()-Y-15)/SizeBigButton;  
                if(Num==NEXT_TURN){
                  logic.nextTurn();
                  actCard=-1;  
                }
                else if(Num==HILL){
                  logic.Hill(); 
                }
                else if(Num==BUILD){
                  logic.BuildLab();
                }
                else if(Num==VACTINE){
                  logic.Vactine();
                }
                else if(Num==TELEPORT){
                  logic.Teleport();
                } 
              }
              //обработка клика на карте мира
              else{
                logic.onClick(e.getX()*Width/getWidth()+Left-radiusCity/2,e.getY()*Height/getHeight()+Top-radiusCity/2,
                      radiusCity*Width/getWidth(),
                      radiusCity*Height/getHeight());
              }
              repaint();// перерисовка
          }

          public void mouseEntered(MouseEvent e) {
               
                   
                   
               
          }

          public void mouseExited(MouseEvent e) {
             
          }
          
          /* 
           * реализация
           * прокрутки
           */
          // при нажатии кнопки мыши
          public void mousePressed(MouseEvent e) {
              // сохранить координаты точки нажатия
              XPush=e.getX();
              YPush=e.getY();
          }
          
          // при отпускании кнопки мыши
          public void mouseReleased(MouseEvent e) {
              //вычисляем горизонтальное смещение
              Left-=(e.getX()-XPush)/3;
              if(Left<0) Left=0;
              if(Left+Width>1000)Left=1000-Width;
              // вертикальное смещение
              Top-=(e.getY()-YPush)/3;
              if(Top<0) Top=0;
              if(Top+Height>1000)Top=1000-Height;
              repaint();// перерисовка
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
