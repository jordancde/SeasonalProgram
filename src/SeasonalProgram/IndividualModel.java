package SeasonalProgram
;

import SeasonalProgram.Core;
import SeasonalProgram.DateSet;
import SeasonalProgram.Sector;
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
class IndividualModel {

    public Date startDate;
    public Date endDate;
    //public boolean isModel;
    
    public Core core;
    public Sector sector;
    
    public double boxSizePercent;
    public int reversalBoxes;
    public int signalBoxes;
   
    public ArrayList<DateSet> dates = new ArrayList<DateSet>();
        
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    public SimpleDateFormat monthsdf = new SimpleDateFormat("MM/dd");
   
    public IndividualModel(
         JTextField startDateText,
         JTextField endDateText,
         /*JRadioButton model,
         JRadioButton individual,*/
        
         ArrayList<JComponent[]> coreInput,
        
         ArrayList<JComponent[]> datesInput
        
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
            JComboBox jcb2 = (JComboBox)component[1];
            JTextField jtb = (JTextField)component[2];
            JTextField jtb2 = (JTextField)component[3];
            JTextField jtb3 = (JTextField)component[4];
            
            boxSizePercent = Double.parseDouble(jtb.getText());
            reversalBoxes = Integer.parseInt(jtb2.getText());
            signalBoxes = Integer.parseInt(jtb3.getText());
            
            core = (new Core(jcb.getSelectedItem().toString()));
            sector = (new Sector(jcb2.getSelectedItem().toString()));
        }
        
        for(JComponent[] component:datesInput){
            JTextField jtb1 = (JTextField)component[0];
            JTextField jtb2 = (JTextField)component[1];
            JComboBox jcb = (JComboBox)component[2];
            dates.add(new DateSet(monthsdf.parse(jtb1.getText()),monthsdf.parse(jtb2.getText()),jcb.getSelectedItem().toString()));
        }

  
    
    }

    

    
    
}