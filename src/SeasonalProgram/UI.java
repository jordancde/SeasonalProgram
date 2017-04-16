/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import static SeasonalProgram.Data.FILEPATH;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
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
import javax.swing.JOptionPane;
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
        public ButtonGroup bg;
        
        public JPanel one;
        public JPanel two;
        public JPanel three;
        public JPanel four;
        public JPanel five;
        
        public ArrayList<JComponent[]> coresInput = new ArrayList<JComponent[]>();
        public ArrayList<JComponent[]> sectorsInput = new ArrayList<JComponent[]>();
        public ArrayList<JComponent[]> triggersInput = new ArrayList<JComponent[]>();
        
        public JButton addCore;
        public JButton addSector;
        public JButton addTrigger;
        
        public String[] triggerNames = {"Relative Strength","Moving Averages","RSI","Brooke Thackray RS"};
        public String[] options = {"Exponential","Simple"};

    
        public UI(){
            super("Seasonal Program");

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setDefaultLookAndFeelDecorated(true);
            getContentPane().setPreferredSize(new Dimension(400,400));
            
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            
            one = sectionOne();
            two = sectionTwo();
            three = sectionThree();
            four = sectionFour();
            five = sectionFive();
            add(one);
            add(two);
            add(three);
            add(four);
            add(five);
            
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
            bg = new ButtonGroup();
            bg.add(model);
            bg.add(individual);
            
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
            
            
            addCore = new JButton("Add Core");
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
        
        public JPanel sectionThree(){
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
            
            addSector = new JButton("Add Sector");
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
            
            
            components[0] = new JComboBox(triggerNames);
            components[1] = new JComboBox(options);
            components[2] = new JTextField(4);
            components[3] = new JComboBox(options);
            components[4] = new JTextField(4);
            
            triggersInput.add(components);
            for(JComponent jc:components){
                secFour.add(jc);
            }
            
            addTrigger = new JButton("Add Trigger");
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
            
            JButton loadPresets = new JButton("Load Presets");
            secFive.add(loadPresets);
            loadPresets.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        try {
                            loadPreset(loadPresets());
                        } catch (IOException ex) {
                            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }              
            });
            
            JButton savePresets = new JButton("Save Presets");
            secFive.add(savePresets);
            savePresets.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        savePresets();
                    } catch (IOException ex) {
                        Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }              
            });
            
            JButton deletePreset = new JButton("Delete Preset");
            secFive.add(deletePreset);
            deletePreset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        try {
                            deletePreset(loadPresets());
                        } catch (IOException ex) {
                            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }              
            });
            
            
            return secFive;
        }
        public void deletePreset(String path) throws URISyntaxException, IOException{
            (new File(path)).delete();
        }
        
        public String loadPresets() throws URISyntaxException, IOException{
            URL location = SeasonalProgram.class.getProtectionDomain().getCodeSource().getLocation();
            File folder = new File(location.toURI());
            File[] listOfFiles = folder.listFiles();
            
            int numPresets = 0;
            for(int i = 0; i < listOfFiles.length; i++){
                if(listOfFiles[i].getName().contains("PRESET")){
                    numPresets++;
                }
            }
            String[] fileNames = new String[numPresets];
            //current position in fileNames
            int currentPos = 0;
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()&&listOfFiles[i].getName().contains("PRESET")) {
                  String fileName = listOfFiles[i].getName();
                  fileName = fileName.replace("PRESET", "");
                  fileName = fileName.replace(".csv", "");
                  fileNames[currentPos] = fileName;
                  currentPos++;
                }
            }
           
            String input = (String) JOptionPane.showInputDialog(null, "Select Preset",
            "Presets", JOptionPane.QUESTION_MESSAGE, null, fileNames,fileNames[0]);
            
            String path = location.getFile()+"PRESET"+input+".csv";
            return path;
            
            
        }
        
        public void loadPreset(String path) throws IOException{
            
            ArrayList<String[]> preset = readFile(path);
            startDateText.setText(preset.get(0)[0]);
            endDateText.setText(preset.get(0)[1]);
            if(!Boolean.parseBoolean(preset.get(0)[3])){
                bg.setSelected(individual.getModel(),true);
            }
            
            int lengthOfCore = coresInput.get(0).length;
            int numCores = (int)preset.get(1).length/lengthOfCore;
            
            for(int i = 0;i<preset.get(1).length;i+=lengthOfCore){
                if(coresInput.size()<i/lengthOfCore){
                    JComponent[] components = new JComponent[3];
                    components[0] = new JComboBox(SeasonalProgram.data.getDatasetNames());
                    components[1] = new JTextField(20);
                    components[2] = new JTextField(20);
                    coresInput.add(components);
                    two.remove(addCore);
                    for(JComponent jc:components){
                        two.add(jc);
                    }
                    two.add(addCore);
                    two.revalidate();
                }
            }
            for(int i = 0;i<coresInput.size();i++){
                //FIX
                JComboBox jcb = (JComboBox)coresInput.get(i)[0];
                JTextField jtb1 = (JTextField)coresInput.get(i)[1];
                JTextField jtb2 = (JTextField)coresInput.get(i)[2];
                jcb.setSelectedItem(preset.get(1)[0+i*3]);
                jtb1.setText(preset.get(1)[1+i*3]);
                jtb2.setText(preset.get(1)[2+i*3]);
                
            }
              
           
            
            int lengthOfSector = sectorsInput.get(0).length;
            int numSectors = (int)preset.get(2).length/lengthOfSector;
            
            for(int i = 0;i<preset.get(2).length;i+=lengthOfSector){
                if(sectorsInput.size()<i/lengthOfSector){
                    JComponent[] components = new JComponent[4];

                    components[0] = new JComboBox(SeasonalProgram.data.getDatasetNames());
                    components[1] = new JTextField(20);
                    components[2] = new JTextField(20);
                    components[3] = new JTextField(20);

                    sectorsInput.add(components);
                    for(JComponent jc:components){
                        three.add(jc);
                    }
                    
                    three.remove(addSector);
                    for(JComponent jc:components){
                        three.add(jc);
                    }
                    three.add(addSector);
                    three.revalidate();
                }
            }
            
            for(int i = 0;i<sectorsInput.size();i++){
                JComboBox jcb = (JComboBox)sectorsInput.get(i)[0];
                JTextField jtb1 = (JTextField)sectorsInput.get(i)[1];
                JTextField jtb2 = (JTextField)sectorsInput.get(i)[2];
                JTextField jtb3 = (JTextField)sectorsInput.get(i)[3];
                jcb.setSelectedItem(preset.get(2)[0+i*4]);
                jtb1.setText(preset.get(2)[1+i*4]);
                jtb2.setText(preset.get(2)[2+i*4]);
                jtb3.setText(preset.get(2)[3+i*4]);
            }
            
            int lengthOfTrigger = triggersInput.get(0).length;
            int numTriggers = (int)preset.get(3).length/lengthOfTrigger;
            
            for(int i = 0;i<preset.get(3).length;i+=lengthOfTrigger){
                if(triggersInput.size()<i/lengthOfTrigger){
                    JComponent[] components = new JComponent[5];

                    components[0] = new JComboBox(triggerNames);
                    components[1] = new JComboBox(options);
                    components[2] = new JTextField(4);
                    components[3] = new JComboBox(options);
                    components[4] = new JTextField(4);

                    triggersInput.add(components);
                    for(JComponent jc:components){
                        four.add(jc);
                    }
                    
                    four.remove(addTrigger);
                    for(JComponent jc:components){
                        four.add(jc);
                    }
                    four.add(addTrigger);
                    four.revalidate();
                }
            }
            
            for(int i = 0;i<triggersInput.size();i++){
                JComboBox jcb = (JComboBox)triggersInput.get(i)[0];
                JComboBox jcb2 = (JComboBox)triggersInput.get(i)[1];
                JTextField jtf1 = (JTextField)triggersInput.get(i)[2];
                JComboBox jcb3 = (JComboBox)triggersInput.get(i)[3];
                JTextField jtf2 = (JTextField)triggersInput.get(i)[4];
                jcb.setSelectedItem(preset.get(3)[0+i*5]);
                jcb2.setSelectedItem(preset.get(3)[1+i*5]);
                jtf1.setText(preset.get(3)[2+i*5]);
                jcb3.setSelectedItem(preset.get(3)[3+i*5]);
                jtf2.setText(preset.get(3)[4+i*5]);
            } 
            
        }
        
        public static ArrayList<String[]> readFile(String path) throws IOException{
        
            String line = "";
            String cvsSplitBy = ",";
            ArrayList<String[]> historicalData = new ArrayList<String[]>();

            try (BufferedReader br = new BufferedReader(new FileReader(path))) {

                while ((line = br.readLine()) != null) {

                    // use comma as separator
                    String[] data = line.split(cvsSplitBy, -1);
                    historicalData.add(data);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return historicalData;
        }
        
        public void savePresets() throws IOException{
            String presetName = JOptionPane.showInputDialog("Enter Preset Name:");
            
            URL location = SeasonalProgram.class.getProtectionDomain().getCodeSource().getLocation();

            String path = location.getFile()+"PRESET"+presetName+".csv";
            int result = 0;
            if(!(new File(path).canRead())){
                (new File(path)).createNewFile();
            }else{
                //cancel ==2, ok ==0
                result = JOptionPane.showConfirmDialog((Component) null, "Preset exists, overwrite?","alert",JOptionPane.OK_CANCEL_OPTION);
                
            }
            //if ok
            if(result == 0){
                try(
                FileWriter fw = new FileWriter(path, false);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
                {       
                    out.print(startDateText.getText()+",");
                    out.print(endDateText.getText()+",");
                    out.print(model.isSelected()+",");
                    out.print(individual.isSelected()+",");
                    out.println("");
                    for(JComponent[] c:coresInput){
                        JComboBox jcb = (JComboBox)c[0];
                        JTextField jtb1 = (JTextField)c[1];
                        JTextField jtb2 = (JTextField)c[2];
                        out.print(jcb.getSelectedItem().toString()+",");
                        out.print(jtb1.getText()+",");
                        out.print(jtb2.getText()+",");
                    }  
                    out.println("");
                    for(JComponent[] c:sectorsInput){
                        JComboBox jcb = (JComboBox)c[0];
                        JTextField jtb1 = (JTextField)c[1];
                        JTextField jtb2 = (JTextField)c[2];
                        JTextField jtb3 = (JTextField)c[3];
                        out.print(jcb.getSelectedItem().toString()+",");
                        out.print(jtb1.getText()+",");
                        out.print(jtb2.getText()+",");
                        out.print(jtb3.getText()+",");
                    }
                    out.println("");
                    for(JComponent[] c:triggersInput){
                        JComboBox jcb = (JComboBox)c[0];
                        JComboBox jcb2 = (JComboBox)c[1];
                        JTextField jtf1 = (JTextField)c[2];
                        JComboBox jcb3 = (JComboBox)c[3];
                        JTextField jtf2 = (JTextField)c[4];
                        out.print(jcb.getSelectedItem().toString()+",");
                        out.print(jcb2.getSelectedItem().toString()+",");
                        out.print(jtf1.getText()+",");
                        out.print(jcb3.getSelectedItem().toString()+",");
                        out.print(jtf2.getText()+",");
                    }
                    out.println("");

                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            
            
        }
}
