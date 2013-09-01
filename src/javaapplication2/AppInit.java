/*
 * Стартовое окно для выбора количества игроков,
 * уровня сложности и распределения ролей между игроками
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
     final int MaxGamers=5;  //максимальное количество игроков
     int GNumGamers; //количество игроков,выбранное пользователем
     private JButton ChooseNumber[] = new JButton[MaxGamers];// массив кнопок,
        //используемый как для выбора количества игроков, так и для выбора сложности
     private JAvatar Avatars[] = new JAvatar[MaxGamers]; //портреты персонажей 
     private ActionListener actions[]=new ActionListener[MaxGamers];// действи кнопок
     public AppInit(){
        //создание окна
        super("Personage");
	this.setBounds(100,100,500,200);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //контейнер
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(2,4,1,7));
        //кнопки
        for(int i=0;i<MaxGamers;i++){
            ChooseNumber[i]=new JButton(i+1+"");
            actions[i]=new TChooseNumber(i+1);
            ChooseNumber[i].addActionListener(actions[i]);
            container.add(ChooseNumber[i]); 
        }
        //аватары(пока не видны пользователю)
        for(int i=0;i<MaxGamers;i++){
           Avatars[i]=new JAvatar();
           container.add(Avatars[i]); 
        }
        //показываем окно
        setVisible(true);
        
     }
     public static void main(String[] args) {
        // создаём окно
        AppInit ChooseGamers=new AppInit(); // создание окна программы
        
    }
     
    //класс событий кнопок для выбора количества игроков
    class TChooseNumber implements ActionListener {
        int  NumGamers;// номер кнопки
        public TChooseNumber(int iNumGamers){
            NumGamers=iNumGamers;
        }
        
        //выполняется при первом нажатии кнопки
        public void actionPerformed(ActionEvent e) { 
            // меняем названия кнопок, на настройку уровней сложности
            ChooseNumber[0].setText("Easy");
            ChooseNumber[1].setText("Normal");
            ChooseNumber[2].setText("Hard");
            ChooseNumber[3].setText("Very Hard");
            ChooseNumber[4].setText("Creazy");
            // меняем события на кнопках
            for(int i=0; i<MaxGamers; i++){
                ChooseNumber[i].removeActionListener(actions[i]);
                actions[i]=new TChooseLevel(i+1);
                ChooseNumber[i].addActionListener(actions[i]);
            }
            // распределяем роли игроков
            for(int i=0; i<MaxGamers; i++){
                //Avatars[i].setPicture("/javaapplication2/mainback.jpg");
            }
            //сохраняем количество игроков
            GNumGamers=NumGamers;           
        }

    } 
    
    //класс событий кнопок для выбора уровня сложности
    class TChooseLevel implements ActionListener {
        int  Level; // уровень сложности, соответствующий кнопке
        public TChooseLevel(int iLevel){
            Level=iLevel;
        }
        //выполняется при втором нажатии кнопки
        public void actionPerformed(ActionEvent e) { 
            Infection form=new Infection("Infection",GNumGamers,Level); // создание основного окна програмы   
        }

    }
    
    //класс, обеспечивающий вывод аватаров персонажей на экран
    class JAvatar extends JPanel {
        private BufferedImage image;
        
        //инициализация
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
        
        //перерисовка
        public void paintComponent(Graphics g) {
            g.drawImage(image, 0, 0, null); 
        }

        
        
    }
    
}
