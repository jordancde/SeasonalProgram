/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import SeasonalProgram.OutputTables.Table;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author jordandearsley
 */
public class IndividualTable extends Table{
    
    public HashMap<DateSet,ArrayList<IndividualYearRow>> data;
    
    public IndividualTable(String name, Date startDate, Date endDate,HashMap<DateSet,ArrayList<IndividualYearRow>> data) throws IOException{
        super(name, startDate, endDate);
        this.data = data;
        table = makeTable(data);
    }
    
    //@Override
    public ArrayList<String[]> makeTable(HashMap<DateSet,ArrayList<IndividualYearRow>> data){
        
    }
}
