/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
        
        public JTextField startDateText;
        public JTextField endDateText;
        public JRadioButton model;
        public JRadioButton individual;
        
        public ArrayList<JComponent[]> coresInput = new ArrayList<JComponent[]>();
        public ArrayList<JComponent[]> sectorsInput = new ArrayList<JComponent[]>();
        public ArrayList<JComponent[]> triggersInput = new ArrayList<JComponent[]>();

    
        public UI(){
            super("Seasonal Program");

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setDefaultLookAndFeelDecorated(true);
            getContentPane().setPreferredSize(new Dimension(400,400));
            
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            
            add(sectionOne());
            add(sectionTwo());
            add(sectionThree(3));
            add(sectionFour());
            add(sectionFive());
            
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
            panelHolder[0][0].add(new JLabel("Start Date (yyyy/mm/dd)"));
            panelHolder[0][1].add(new JLabel("End Date (yyyy/mm/dd)"));
            startDateText = new JTextField(20);
            panelHolder[0][0].add(startDateText);
            endDateText = new JTextField(20);
            panelHolder[0][1].add(endDateText);
            
            //Row Two
            model = new JRadioButton("Model");
            individual = new JRadioButton("Individual");
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
            
            
            JPanel secTwo = new JPanel(new GridLayout());
            
            
            GridLayout gl = new GridLayout(0,3);
            secTwo.setLayout(gl);
            //Row One 
            secTwo.add(new JLabel("Core"));
            secTwo.add(new JLabel("Buy Date"));
            secTwo.add(new JLabel("Sell Date"));
            
            //Row Two
            //JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JComponent[] components = new JComponent[3];
       
            components[0] = new JComboBox(SeasonalProgram.data.getDatasetNames());
            components[1] = new JTextField(20);
            components[2] = new JTextField(20);
            coresInput.add(components);
            for(JComponent jc:components){
                secTwo.add(jc);
            }
            
            
            JButton addCore = new JButton("Add Core");
            secTwo.add(addCore);

            addCore.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JComponent[] components = new JComponent[3];
                    components[0] = new JComboBox(SeasonalProgram.data.getDatasetNames());
                    components[1] = new JTextField(20);
                    components[2] = new JTextField(20);
                    coresInput.add(components);
                    secTwo.remove(addCore);
                    for(JComponent jc:components){
                        secTwo.add(jc);
                    }
                    secTwo.add(addCore);
                    secTwo.revalidate();
                }
            });
            
            secTwo.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f)));

            return secTwo;
        }
        
        public JPanel sectionThree(int sectors){
        //Creates Grid to [][] array
            
            
            int columns = 4;
            GridLayout gl = new GridLayout(0,4);
            
            JPanel secThree = new JPanel(gl);
            
            //Row One 
            secThree.add(new JLabel("Sector"));
            secThree.add(new JLabel("Start"));
            secThree.add(new JLabel("Stop"));
            secThree.add(new JLabel("Lev %"));
            
            JComponent[] components = new JComponent[4];
       
            components[0] = new JComboBox(SeasonalProgram.data.getDatasetNames());
            components[1] = new JTextField(20);
            components[2] = new JTextField(20);
            components[3] = new JTextField(20);
            
            sectorsInput.add(components);
            for(JComponent jc:components){
                secThree.add(jc);
            }
            
            JButton addSector = new JButton("Add Sector");
            secThree.add(addSector);
            
            addSector.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JComponent[] components = new JComponent[4];

                    components[0] = new JComboBox(SeasonalProgram.data.getDatasetNames());
                    components[1] = new JTextField(20);
                    components[2] = new JTextField(20);
                    components[3] = new JTextField(20);

                    sectorsInput.add(components);
                    for(JComponent jc:components){
                        secThree.add(jc);
                    }
                    
                    secThree.remove(addSector);
                    for(JComponent jc:components){
                        secThree.add(jc);
                    }
                    secThree.add(addSector);
                    secThree.revalidate();
                }
            });
            
            secThree.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f)));

            return secThree;
        }
        
        public JPanel sectionFour(){
            
            int columns = 5;
            GridLayout gl = new GridLayout(0,columns);
            
            JPanel secFour = new JPanel(gl);
            
            //Row One 
            secFour.add(new JLabel("Triggers"));
            secFour.add(new JLabel("Type"));
            secFour.add(new JLabel("Param"));
            secFour.add(new JLabel("Type"));
            secFour.add(new JLabel("Param"));
            
            
            JComponent[] components = new JComponent[5];
            String[] triggerNames = {"Relative Strength","Moving Averages","RSI","Brooke Thackray RS"};
            String[] options = {"Exponential","Simple"};
            
            components[0] = new JComboBox(triggerNames);
            components[1] = new JComboBox(options);
            components[2] = new JTextField(4);
            components[3] = new JComboBox(options);
            components[4] = new JTextField(4);
            
            triggersInput.add(components);
            for(JComponent jc:components){
                secFour.add(jc);
            }
            
            JButton addTrigger = new JButton("Add Trigger");
            secFour.add(addTrigger);

            addTrigger.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JComponent[] components = new JComponent[5];

                    components[0] = new JComboBox(triggerNames);
                    components[1] = new JComboBox(options);
                    components[2] = new JTextField(4);
                    components[3] = new JComboBox(options);
                    components[4] = new JTextField(4);

                    triggersInput.add(components);
                    for(JComponent jc:components){
                        secFour.add(jc);
                    }
                    
                    secFour.remove(addTrigger);
                    for(JComponent jc:components){
                        secFour.add(jc);
                    }
                    secFour.add(addTrigger);
                    secFour.revalidate();
                }
            });
           
            secFour.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f)));

            return secFour;
        }
        
        public JPanel sectionFive(){   
            JPanel secFive = new JPanel();
            //
            JButton submitButton = new JButton("Submit");
            secFive.add(submitButton);
            
            JLabel status = new JLabel("Click Submit to Run");
            secFive.add(status);
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        SeasonalProgram.seasonalModel = new Model(
                            startDateText,
                            endDateText,
                            model,
                            individual,
                            coresInput,
                            sectorsInput,
                            triggersInput      
                        );
                        status.setForeground(Color.green);
                        status.setText("Model ran successfully");
                    } catch (Exception ex) {
                        status.setForeground(Color.red);
                        status.setText("Error: Check date format or empty field");
                        Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }              
            });
            return secFive;
        }

}
