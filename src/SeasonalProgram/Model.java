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
    public SimpleDateFormat monthsdf = new SimpleDateFormat("MM/dd");
   
    public Model(
         JTextField startDateText,
         JTextField endDateText,
         JRadioButton model,
         JRadioButton individual,
        
         ArrayList<JComponent[]> coreInput,
        
         ArrayList<JComponent[]> sectorsInput,
        
         ArrayList<JComponent[]> triggersInput
         ) throws ParseException {
    
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
            JTextField jtb3 = (JTextField)component[3];
            cores.add(new Core(jcb.getSelectedItem().toString(),sdf.parse(jtb1.getText()),sdf.parse(jtb2.getText()),Double.parseDouble(jtb3.getText())));
        }
        
        for(JComponent[] component:sectorsInput){
            JComboBox jcb = (JComboBox)component[0];
            JTextField jtb1 = (JTextField)component[1];
            JTextField jtb2 = (JTextField)component[2];
            JTextField jtb3 = (JTextField)component[3];
            JTextField jtb4 = (JTextField)component[4];
            sectors.add(new Sector(jcb.getSelectedItem().toString(),monthsdf.parse(jtb1.getText()),monthsdf.parse(jtb2.getText()),Double.parseDouble(jtb3.getText()),Double.parseDouble(jtb4.getText())));
        }

        for(JComponent[] component:triggersInput){
            JComboBox jcb = (JComboBox)component[0];
            JComboBox jcb2 = (JComboBox)component[1];
            JTextField jtf1 = (JTextField)component[2];
            JComboBox jcb3 = (JComboBox)component[3];
            JTextField jtf2 = (JTextField)component[4];
            triggers.add(new Trigger(jcb.getSelectedItem().toString(),checkCombo(jcb2),Integer.parseInt(jtf1.getText()),checkCombo(jcb3),Integer.parseInt(jtf2.getText())));
        }    
    
    }
    public boolean checkCombo(JComboBox cb){
        return cb.getSelectedItem().toString().equals("Exponential");
    }
    
}
