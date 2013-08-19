/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

/**
 *
 * @author alchemist
 */
public class AppInit  extends JFrame {
     final int MaxGamers=4;
     int GNumGamers;
     private JButton ChooseNumber[] = new JButton[MaxGamers];
     private JAvatar Avatars[] = new JAvatar[MaxGamers];
     private ActionListener actions[]=new ActionListener[MaxGamers];
     public AppInit(){
        super("Personage");
	this.setBounds(100,100,500,200);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(2,4,1,7));
        for(int i=0;i<MaxGamers;i++){
            ChooseNumber[i]=new JButton(i+1+"");
            actions[i]=new TChooseNumber(i+1);
            ChooseNumber[i].addActionListener(actions[i]);
            container.add(ChooseNumber[i]); 
        }
        for(int i=0;i<MaxGamers;i++){
           Avatars[i]=new JAvatar();
           container.add(Avatars[i]); 
        }
        setVisible(true);
        
     }
     public static void main(String[] args) {
        // TODO code application logic private JButton ChooseNumber[] = new JButton[MaxGamers]; here
        //String s=NumGamers.getText();
        AppInit ChooseGamers=new AppInit();
        
    }
     
    class TChooseNumber implements ActionListener {
        int  NumGamers;
        public TChooseNumber(int iNumGamers){
            NumGamers=iNumGamers;
        }
        public void actionPerformed(ActionEvent e) { 
            ChooseNumber[0].setText("Easy");
            ChooseNumber[1].setText("Normal");
            ChooseNumber[2].setText("Hard");
            ChooseNumber[3].setText("Very Hard");
            for(int i=0; i<MaxGamers; i++){
                ChooseNumber[i].removeActionListener(actions[i]);
                actions[i]=new TChooseLevel(i+1);
                ChooseNumber[i].addActionListener(actions[i]);
            }
            for(int i=0; i<MaxGamers; i++){
                //Avatars[i].setPicture("/javaapplication2/mainback.jpg");
            }
            GNumGamers=NumGamers;           
        }

    } 
    class TChooseLevel implements ActionListener {
        int  Level;
        public TChooseLevel(int iLevel){
            Level=iLevel;
        }
        public void actionPerformed(ActionEvent e) { 
            Infection form=new Infection("Infection",GNumGamers,Level); // создание окна программы   
        }

    }
    class JAvatar extends JPanel {
        private BufferedImage image;
        
        public void setPicture(String iAvatar){
            
            try {        
               File temp=new File(iAvatar);
               image = ImageIO.read(temp);
            } 
            catch (Exception ex) {
               int a; 
               a=1;
            }

        }
        
        public void paintComponent(Graphics g) {
            g.drawImage(image, 0, 0, null); 
        }

        
        
    }
    
}
