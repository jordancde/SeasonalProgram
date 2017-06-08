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
class StaticModel {

    public Date startDate;
    public Date endDate;
    //public boolean isModel;
    
    public Core core;
   
    public ArrayList<Sector> sectors = new ArrayList<Sector>();
        
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    public SimpleDateFormat monthsdf = new SimpleDateFormat("MM/dd");
   
    public StaticModel(
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
            core = (new Core(jcb.getSelectedItem().toString(),monthsdf.parse(jtb1.getText()),monthsdf.parse(jtb2.getText()),Double.parseDouble(jtb3.getText())));
        }
        
        for(JComponent[] component:sectorsInput){
            JComboBox jcb = (JComboBox)component[0];
            JTextField jtb1 = (JTextField)component[1];
            JTextField jtb2 = (JTextField)component[2];
            JTextField jtb3 = (JTextField)component[3];
            JTextField jtb4 = (JTextField)component[4];
            sectors.add(new Sector(jcb.getSelectedItem().toString(),monthsdf.parse(jtb1.getText()),monthsdf.parse(jtb2.getText()),Double.parseDouble(jtb3.getText()),Double.parseDouble(jtb4.getText())));
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