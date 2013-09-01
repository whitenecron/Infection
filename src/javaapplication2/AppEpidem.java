package javaapplication2;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;

/**
 *
 * @author alchemist
 */
public class AppEpidem extends JFrame{
    final int NUMSTRING=10;
    final int HEIGHTSTRING=10;
    private JLabel Strings[] = new JLabel[NUMSTRING];
    private JButton BOK = new JButton("ОК");
    public AppEpidem(){
        //создание окна
        super("Cities Infection");
	this.setBounds(30,300,500,200);
	this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //контейнер
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(NUMSTRING+1,1,1,7));
        for(int i=0;i<NUMSTRING;i++){
            Strings[i]=new JLabel("");
            container.add(Strings[i]);
        }
        container.add(BOK); 
    }
    public void ShowEpidem(Vector<Integer> iStrikeTurn,Vector<City> iCities){
        int posYStr=0;
        for(int i=0;i<iStrikeTurn.size()&&posYStr<NUMSTRING;i++,posYStr++){
            Strings[i].setText(iCities.get(iStrikeTurn.get(i)).getName());
        } 
        for(int i=iStrikeTurn.size();i<NUMSTRING;i++){
            Strings[i].setText("");
        }
        this.setVisible(true);
    }
}
