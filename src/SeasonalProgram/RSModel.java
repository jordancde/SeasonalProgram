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
    
    public ArrayList<BoxSize> sizes = new ArrayList<BoxSize>();
    
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
        
         ArrayList<JComponent[]> sectorsInput,
        
         ArrayList<JComponent[]> sizesInput
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
            
            minCoreAllocation = Double.parseDouble(jtb1.getText());
            reversalBoxes = Integer.parseInt(jtb2.getText());
            signalBoxes = Integer.parseInt(jtb3.getText());
            
            core = (new Core(jcb.getSelectedItem().toString(),startDate,endDate,Double.parseDouble(jtb1.getText())));
        }
        
        for(JComponent[] component:sectorsInput){
            JComboBox jcb = (JComboBox)component[0];
            JComboBox jcb2 = (JComboBox)component[1];
            JComboBox jcb3 = (JComboBox)component[2];
            JTextField jtb1 = (JTextField)component[3];

            sectors.add(new Sector(jcb.getSelectedItem().toString(),jcb2.getSelectedItem().toString(),jcb3.getSelectedItem().toString(),startDate,endDate,Double.parseDouble((jtb1.getText()))));
        }

        for(JComponent[] component:sizesInput){

            JTextField jtf1 = (JTextField)component[0];
            JTextField jtf2 = (JTextField)component[1];
            JTextField jtf3 = (JTextField)component[2];

            sizes.add(new BoxSize(Double.parseDouble(jtf1.getText()),Double.parseDouble(jtf2.getText()),Double.parseDouble(jtf3.getText())));
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
