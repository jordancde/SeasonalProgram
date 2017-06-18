/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.io.IOException;
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
class RSModel {

    public Date startDate;
    public Date endDate;
    //public boolean isModel;
    
    public Core core;
   
    public ArrayList<Sector> sectors = new ArrayList<Sector>();
    
    public double boxSizePercent;
    
    public int reversalBoxes;
    public int signalBoxes;
    public double minCoreAllocation;
    
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    public SimpleDateFormat monthsdf = new SimpleDateFormat("MM/dd");
   
    public RSModel(
         JTextField startDateText,
         JTextField endDateText,
         /*JRadioButton model,
         JRadioButton individual,*/
        
         ArrayList<JComponent[]> coreInput,
        
         ArrayList<JComponent[]> sectorsInput
        
  
         ) throws ParseException {
    
        startDate = sdf.parse(startDateText.getText());
        
        endDate = sdf.parse(endDateText.getText());
        /*if(model.isSelected()){
            isModel = true;
        }else if(individual.isSelected()){
            isModel = false;
        }*/

        for(JComponent[] component:coreInput){
            JComboBox jcb = (JComboBox)component[0];
            JTextField jtb1 = (JTextField)component[1];
            JTextField jtb2 = (JTextField)component[2];
            JTextField jtb3 = (JTextField)component[3];
            JTextField jtb4 = (JTextField)component[4];
            JTextField jtb5 = (JTextField)component[5];
            JTextField jtb6 = (JTextField)component[6];
            
            minCoreAllocation = Double.parseDouble(jtb3.getText());
            reversalBoxes = Integer.parseInt(jtb4.getText());
            signalBoxes = Integer.parseInt(jtb5.getText());
            boxSizePercent = Double.parseDouble(jtb6.getText());
            
            core = (new Core(jcb.getSelectedItem().toString(),monthsdf.parse(jtb1.getText()),monthsdf.parse(jtb2.getText()),Double.parseDouble(jtb3.getText())));
        }
        
        for(JComponent[] component:sectorsInput){
            JComboBox jcb = (JComboBox)component[0];
            JComboBox jcb2 = (JComboBox)component[1];
            JComboBox jcb3 = (JComboBox)component[2];
            JTextField jtb1 = (JTextField)component[3];

            sectors.add(new Sector(jcb.getSelectedItem().toString(),jcb2.getSelectedItem().toString(),jcb3.getSelectedItem().toString(),Double.parseDouble((jtb1.getText()))));
        }

           
    
    }
    public boolean checkCombo(JComboBox cb){
        return cb.getSelectedItem().toString().equals("Exponential");
    }
    
    public ArrayList<Security> getSecurities(){
        ArrayList<Security> securities = new ArrayList<Security>();
        securities.add(core);
        for(Sector s:sectors){
            securities.add(s);
        }
        return securities;
    }
    
    
    
}
