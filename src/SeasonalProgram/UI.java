/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javafx.scene.control.ComboBox;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author jordandearsley
 */
public class UI extends JFrame {
        
        public UI(){
            super("Program Name");

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setDefaultLookAndFeelDecorated(true);
            getContentPane().setPreferredSize(new Dimension(400,400));
            
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            add(sectionOne());
            add(sectionTwo());
            add(sectionThree(3));
            add(sectionFour());
            
            pack();
        }
        
        public JPanel sectionOne(){
        //Creates Grid to [][] array
            int i = 2;
            int j = 2;
            JPanel secOne = new JPanel(new GridLayout(i, j));
            
            
            
            JPanel[][] panelHolder = new JPanel[i][j];    
            secOne.setLayout(new GridLayout(i,j,0,0));

            for(int m = 0; m < i; m++) {
               for(int n = 0; n < j; n++) {
                  panelHolder[m][n] = new JPanel(new FlowLayout(FlowLayout.LEFT));
                  secOne.add(panelHolder[m][n]);
               }
            }
            
            //Row One 
            panelHolder[0][0].add(new JLabel("Start Date"));
            panelHolder[0][1].add(new JLabel("End Date"));
            JTextField startDateText = new JTextField(20);
            panelHolder[0][0].add(startDateText);
            JTextField endDateText = new JTextField(20);
            panelHolder[0][1].add(endDateText);
            
            //Row Two
            JRadioButton model = new JRadioButton("Model");
            JRadioButton individual = new JRadioButton("Individual");
            ButtonGroup bG = new ButtonGroup();
            bG.add(model);
            bG.add(individual);
            
            panelHolder[1][0].add(model);
            panelHolder[1][0].add(individual);
            model.setSelected(true);
            secOne.setPreferredSize(secOne.getPreferredSize());
            secOne.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f)));
            return secOne;
        }
        
        public JPanel sectionTwo(){
        //Creates Grid to [][] array
            
            int i = 4;
            int j = 3;
            JPanel secTwo = new JPanel(new GridLayout(i, j));
            
            
            JPanel[][] panelHolder = new JPanel[i][j];    
            secTwo.setLayout(new GridLayout(i,j,0,0));

            for(int m = 0; m < i; m++) {
               for(int n = 0; n < j; n++) {
                  panelHolder[m][n] = new JPanel(new FlowLayout(FlowLayout.LEFT));
                  secTwo.add(panelHolder[m][n]);
               }
            }
            
            //Row One 
            panelHolder[0][0].add(new JLabel("Core"));
            panelHolder[0][1].add(new JLabel("Buy Date"));
            panelHolder[0][2].add(new JLabel("Sell Date"));
            
            //Row Two
            JCheckBox SPBox = new JCheckBox("S&P 500");
            panelHolder[1][0].add(SPBox);
            JTextField SPBuy = new JTextField(20);
            panelHolder[1][1].add(SPBuy);
            JTextField SPSell = new JTextField(20);
            panelHolder[1][2].add(SPSell);
            
            //Row Three
            JCheckBox TSXBox = new JCheckBox("TSX");
            panelHolder[2][0].add(TSXBox);
            JTextField TSXBuy = new JTextField(20);
            panelHolder[2][1].add(TSXBuy);
            JTextField TSXSell = new JTextField(20);
            panelHolder[2][2].add(TSXSell);
            
            //Row Four
            JCheckBox OtherBox = new JCheckBox("Other");
            panelHolder[3][0].add(OtherBox);
            JTextField OtherBuy = new JTextField(20);
            panelHolder[3][1].add(OtherBuy);
            JTextField OtherSell = new JTextField(20);
            panelHolder[3][2].add(OtherSell);
            
            secTwo.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f)));

            return secTwo;
        }
        
        public JPanel sectionThree(int sectors){
        //Creates Grid to [][] array
            
            int rows = 1+sectors;
            int columns = 4;
            JPanel secThree = new JPanel(new GridLayout(rows, columns));
            
            
            JPanel[][] panelHolder = new JPanel[rows][columns];    
            secThree.setLayout(new GridLayout(rows,columns,0,0));

            for(int m = 0; m < rows; m++) {
               for(int n = 0; n < columns; n++) {
                  panelHolder[m][n] = new JPanel(new FlowLayout(FlowLayout.LEFT));
                  secThree.add(panelHolder[m][n]);
               }
            }
            
            //Row One 
            panelHolder[0][0].add(new JLabel("Sector"));
            panelHolder[0][1].add(new JLabel("Start"));
            panelHolder[0][2].add(new JLabel("Stop"));
            panelHolder[0][3].add(new JLabel("Lev %"));
            
            JTextField[][] fieldHolder = new JTextField[rows][columns]; 
            for(int i = 0; i<sectors;i++){
                for(int j = 0;j<columns;j++){
                    fieldHolder[i][j] = new JTextField(20);
                    panelHolder[i+1][j].add(fieldHolder[i][j]);
                }
            }
            secThree.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f)));

            return secThree;
        }
        
        public JPanel sectionFour(){
        //Creates Grid to [][] array
            
            int rows = 5;
            int columns = 3;
            JPanel secFour = new JPanel(new GridLayout(rows, columns));
            
            
            JPanel[][] panelHolder = new JPanel[rows][columns];    
            secFour.setLayout(new GridLayout(rows,columns,0,0));

            for(int m = 0; m < rows; m++) {
               for(int n = 0; n < columns; n++) {
                  panelHolder[m][n] = new JPanel(new FlowLayout(FlowLayout.LEFT));
                  secFour.add(panelHolder[m][n]);
               }
            }
            
            //Row One 
            panelHolder[0][0].add(new JLabel("Triggers"));
            
            
            String[] options = {"Exponential","Simple"};

            //Row Two
            JCheckBox RSBox = new JCheckBox("Relative Strength");
            panelHolder[1][0].add(RSBox);
            JComboBox RSCombo1 = new JComboBox(options);
            panelHolder[1][1].add(RSCombo1);
            JTextField RSText1 = new JTextField(4);
            panelHolder[1][1].add(RSText1);
            JComboBox RSCombo2 = new JComboBox(options);
            panelHolder[1][2].add(RSCombo2);
            JTextField RSText2 = new JTextField(4);
            panelHolder[1][2].add(RSText2);
            
            //Row Three
            JCheckBox MABox = new JCheckBox("Moving Averages");
            panelHolder[2][0].add(MABox);
            JComboBox MACombo1 = new JComboBox(options);
            panelHolder[2][1].add(MACombo1);
            JTextField MAText1 = new JTextField(4);
            panelHolder[2][1].add(MAText1);
            JComboBox MACombo2 = new JComboBox(options);
            panelHolder[2][2].add(MACombo2);
            JTextField MAText2 = new JTextField(4);
            panelHolder[2][2].add(MAText2);
            
            //Row Four
            JCheckBox RSIBox = new JCheckBox("RSI");
            panelHolder[3][0].add(RSIBox);
            JComboBox RSICombo1 = new JComboBox(options);
            panelHolder[3][1].add(RSICombo1);
            JTextField RSIText1 = new JTextField(4);
            panelHolder[3][1].add(RSIText1);
            JComboBox RSICombo2 = new JComboBox(options);
            panelHolder[3][2].add(RSICombo2);
            JTextField RSIText2 = new JTextField(4);
            panelHolder[3][2].add(RSIText2);
            
            //Row Four
            JCheckBox BTBox = new JCheckBox("Brooke Thackray RS");
            panelHolder[4][0].add(BTBox);
            JComboBox BTCombo1 = new JComboBox(options);
            panelHolder[4][1].add(BTCombo1);
            JTextField BTText1 = new JTextField(4);
            panelHolder[4][1].add(BTText1);
            JComboBox BTCombo2 = new JComboBox(options);
            panelHolder[4][2].add(BTCombo2);
            JTextField BTText2 = new JTextField(4);
            panelHolder[4][2].add(BTText2);
           
            secFour.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f)));

            return secFour;
        }

}
