/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author alchemist
 */
public class AppHelp extends JFrame{
    private JLabel Message = new JLabel("");
    private JButton BOK = new JButton("ОК");
    public AppHelp(){
        //создание окна
        super("Help");
	this.setBounds(400,300,300,100);
	this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //контейнер
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(2,1,1,7));
        container.add(Message);
        container.add(BOK); 
    }
    public void Show(String iMessage){
        Message.setText(iMessage);
        setVisible(true);
    }
}
