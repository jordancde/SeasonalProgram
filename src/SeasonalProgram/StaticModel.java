/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author jordandearsley
 */
public class StaticModel extends Model {
    
    
    public Portfolio pf;
    
    public double[] allocations;
    public ArrayList<Sector> heldSectors;
    
    public StaticModel(JTextField startDateText,
         JTextField endDateText,
         JRadioButton model,
         JRadioButton individual,
        
         ArrayList<JComponent[]> coreInput,
        
         ArrayList<JComponent[]> sectorsInput,
        
         ArrayList<JComponent[]> triggersInput
         ) throws ParseException, IOException{
        super(startDateText,endDateText,model,individual,coreInput,sectorsInput,triggersInput);
        
        pf = new Portfolio("Seasonal Portfolio",startDate,endDate);
        //cores listed first in allocations
        allocations = new double[cores.size()+sectors.size()];
        for(int i = 0;i<cores.size();i++){
            allocations[i] = cores.get(i).allocation;
        }
        for(int i = cores.size();i<allocations.length;i++){
            allocations[i] = 0;
        }
        
        Date currentDay = startDate;
        while(currentDay.before(endDate)){
            
        }
        
    }
    
    //COME BACK TO THIS
    public void reallocate(Date current){
        for(Sector s: sectors){
            if(!heldSectors.contains(s)&&current.after(s.startDate)){
                
                heldSectors.add(s);
                allocations[cores.size()+sectors.indexOf(s)] = s.allocation;
                double sectorAllocation = s.allocation;
                for(int i = 0;i<cores.size();i++){
                    allocations[i]-= sectorAllocation*(cores.get(i).allocation/100);
                }
                
            }else if(heldSectors.contains(s)&&current.after(s.stopDate)){
                heldSectors.remove(s);
                allocations[cores.size()+sectors.indexOf(s)] = 0;
                double sectorAllocation = s.allocation;
                for(int i = 0;i<cores.size();i++){
                    allocations[i]-= sectorAllocation*(cores.get(i).allocation/100);
                }
            }
        }
    }
    
    
    
    


}

