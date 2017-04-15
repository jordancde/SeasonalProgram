/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author jordandearsley
 */
class Model {

    public Date startDate;
    public Date endDate;
    public boolean isModel;
    
    public ArrayList<Core> cores = new ArrayList<Core>();
   
    public ArrayList<Sector> sectors = new ArrayList<Sector>();
    
    public ArrayList<Trigger> triggers = new ArrayList<Trigger>();
    
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
   
    public Model(
         JTextField startDateText,
         JTextField endDateText,
         JRadioButton model,
         JRadioButton individual,
        
         ArrayList<JComponent[]> coreInput,
        
         ArrayList<JComponent[]> sectorsInput,
        
         JCheckBox RSBox,
         JComboBox RSCombo1,
         JTextField RSText1,
         JComboBox RSCombo2,
         JTextField RSText2,
         JCheckBox MABox,
         JComboBox MACombo1,
         JTextField MAText1,
         JComboBox MACombo2,
         JTextField MAText2,
         JCheckBox RSIBox,
         JComboBox RSICombo1,
         JTextField RSIText1,
         JComboBox RSICombo2,
         JTextField RSIText2,
         JCheckBox BTBox,
         JComboBox BTCombo1,
         JTextField BTText1,
         JComboBox BTCombo2,
         JTextField BTText2) throws ParseException {
    
        startDate = sdf.parse(startDateText.getText());
        endDate = sdf.parse(endDateText.getText());

        if(model.isSelected()){
            isModel = true;
        }else if(individual.isSelected()){
            isModel = false;
        }

        for(JComponent[] component:coreInput){
            JComboBox jcb = (JComboBox)component[0];
            JTextField jtb1 = (JTextField)component[1];
            JTextField jtb2 = (JTextField)component[2];
            cores.add(new Core(jcb.getSelectedItem().toString(),sdf.parse(jtb1.getText()),sdf.parse(jtb2.getText())));
        }
        
        for(JComponent[] component:sectorsInput){
            JComboBox jcb = (JComboBox)component[0];
            JTextField jtb1 = (JTextField)component[1];
            JTextField jtb2 = (JTextField)component[2];
            JTextField jtb3 = (JTextField)component[3];
            sectors.add(new Sector(jcb.getSelectedItem().toString(),sdf.parse(jtb1.getText()),sdf.parse(jtb2.getText()),Double.parseDouble(jtb3.getText())));
        }
        
        
        
        
        if(RSBox.isSelected()){
            triggers.add(new Trigger("Relative Strength",checkCombo(RSCombo1),Integer.parseInt(RSText1.getText()),checkCombo(RSCombo2),Integer.parseInt(RSText2.getText())));
        }
        if(MABox.isSelected()){
            triggers.add(new Trigger("Moving Averages",checkCombo(MACombo1),Integer.parseInt(MAText1.getText()),checkCombo(MACombo2),Integer.parseInt(MAText2.getText())));
        }
        if(RSIBox.isSelected()){
            triggers.add(new Trigger("RSI",checkCombo(RSICombo1),Integer.parseInt(RSIText1.getText()),checkCombo(RSICombo2),Integer.parseInt(RSIText2.getText())));
        }
        if(BTBox.isSelected()){
            triggers.add(new Trigger("Brooke Thackray RS",checkCombo(BTCombo1),Integer.parseInt(BTText1.getText()),checkCombo(BTCombo2),Integer.parseInt(BTText2.getText())));
        }
    
    }
    public boolean checkCombo(JComboBox cb){
        return cb.getSelectedItem().toString().equals("Exponential");
    }
    
}
