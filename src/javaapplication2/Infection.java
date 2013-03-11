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
    final int radiusCity=30;
    static Button Next=new Button("Next turn");
    static Button Hill=new Button("Hill");
    static Button Reset=new Button("Reset");
    static Button Use=new Button("MoveTo");
    static Button Chart=new Button("Chart");
    static Button Build=new Button("Build");
    static Button Vactine=new Button("Vactine");
    static Button Teleport=new Button("Teleport");
    static Image Background;
    static Vector<Button> Cards = new Vector();
    static game logic;
    static Infection form;
    static int actCard;
    Infection(String s){
        super(s);
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
        Reset.setSize(200, 40);
        Reset.setVisible(true);
        c.add(Reset);
        Use.setSize(200, 40);
        Use.setVisible(true);
        c.add(Use);
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
        for(int i=0;i<9;i++){
            Cards.add(new Button());
            //Cards.get(i).setLocation(0,500);
            Cards.get(i).setLabel("sandro23");
            Cards.get(i).setActionCommand(""+i);
            Cards.get(i).setVisible(true);
            c.add(Cards.get(i));
        }
        
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
        for(int i=0;i<num;i++){
            int XBegin=Trackers.get(i).XBegin;
            int YBegin=Trackers.get(i).YBegin;
            int XEnd=Trackers.get(i).XEnd;
            int YEnd=Trackers.get(i).YEnd;
            g.drawLine(XBegin*W/1000+radiusCity/2, YBegin*H/1000+radiusCity/2, XEnd*W/1000+radiusCity/2, YEnd*H/1000+radiusCity/2);
        }
        Vector<City> Cities = logic.getCities();
        num=Cities.size();
        for(int i=0; i<num; i++)
        {
            int color=Cities.get(i).getColor();
            switch(color){
            case 0:    
                g.setColor(Color.BLACK); break;
            case 1:
                g.setColor(Color.BLUE); break;
            case 2:
                g.setColor(Color.YELLOW); break;
            case 3:
                g.setColor(Color.ORANGE); break;
            }
            
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
        Font H1=new Font(null, num, 50);
        Font normal = new Font(null, num, 12);
        g.setFont(H1);
        String strTurn = Gamers.get(logic.getActGamer()).getRoleStr()+
                "'s turn("+logic.getActions()+")";
        g.clearRect(W/4, 40, W/2+20,80);
        g.drawString(strTurn, W/4, 100);
        g.setFont(normal);
        Vector<Integer> Arm = logic.getArm();
        int n=Arm.size();
        for(int i=0;i<9;i++){
            if(i<n){
                String s=Cities.get(Arm.get(i)).getName();
                //Cards.get(i).setLocation(0,800);
                Cards.get(i).setLabel(s);
                int color=Cities.get(Arm.get(i)).getColor();
                switch(color){
                case 0:    
                    Cards.get(i).setBackground(Color.BLACK); 
                    Cards.get(i).setForeground(Color.WHITE);break;
                case 1:
                    Cards.get(i).setBackground(Color.BLUE); 
                    Cards.get(i).setForeground(Color.WHITE);break;
                case 2:
                    Cards.get(i).setBackground(Color.YELLOW); 
                    Cards.get(i).setForeground(Color.BLACK);break;
                case 3:
                    Cards.get(i).setBackground(Color.ORANGE);
                    Cards.get(i).setForeground(Color.WHITE);break;
                }
            }
            else{
                Cards.get(i).setBackground(Color.WHITE);
                Cards.get(i).setLabel("       ");
            }     
        }
        for(int i=0; i<4; i++){
            g.fillOval(i*radiusCity*3+W/3,H-radiusCity*3,radiusCity*2,radiusCity*2);
        }
    }
   
    //private void setColor(Graphics g)
    
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
    public static void main(String[] args) {
        // TODO code application logic here
        //String s=NumGamers.getText();
        logic = new game(5,1);
        form = new Infection("Infection");
        
    }
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

     public class CustomListener implements MouseListener {

          public void mouseClicked(MouseEvent e) {
              int X=e.getX()-radiusCity/2;
              int Y=e.getY()-radiusCity/2;
              logic.onClick(X*1000/form.getWidth(),Y*1000/form.getHeight(),
                      radiusCity*1000/form.getWidth(),
                      radiusCity*1000/form.getHeight());
              form.repaint();
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
}
