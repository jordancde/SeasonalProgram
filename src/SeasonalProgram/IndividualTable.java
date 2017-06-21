/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import SeasonalProgram.OutputTables.Table;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author jordandearsley
 */
public class IndividualTable extends Table{
    
    public HashMap<DateSet,ArrayList<IndividualYearRow>> data;
    public int tableColumns = 17;
    public SimpleDateFormat monthsdf = new SimpleDateFormat("MM/dd");
    
    public IndividualTable(String name, Date startDate, Date endDate,HashMap<DateSet,ArrayList<IndividualYearRow>> data) throws IOException{
        super(name, startDate, endDate);
        this.data = data;
        table = makeTable(data);
    }
    
    //@Override
    public ArrayList<String[]> makeTable(HashMap<DateSet,ArrayList<IndividualYearRow>> data){
        ArrayList<String[]> table = new ArrayList<String[]>();
        
        for(DateSet d:data.keySet()){
        //Title Row
            String[] title = new String[tableColumns];


            title[0] = name+" "+monthsdf.format(d.startDate)+" to "+
                    monthsdf.format(d.endDate);

            title[1] = "% Gain";
            title[11] = "Fq>0";
            title[15] = "Fq > Benchmark";
            title[17] = "Fq > Sector";

            table.add(title);

            //headers
            String[] header = new String[tableColumns];
            header[1] = "Benchmark";
            header[2] = name;
            header[3] = name+" PnF";
            header[4] = "PnF Entry Date";
            header[5] = "PnF Exit Date";
            header[7] = name+" > Benchmark";
            header[8] = name+" PnF > Benchmark";
            header[9] = name+" PnF > "+name;
            header[11] = "Benchmark";
            header[12] = name;
            header[13] = name+" PnF";
            header[15] = name;
            header[16] = name+" PnF";
            header[17] = name+" PnF > "+name;

            table.add(header);
        
        
            int firstYear = getFirstYear(d);
            int lastYear = getLastYear(d);
            
            
            ArrayList<String[]> dataRows = new ArrayList<String[]>();
            for(IndividualYearRow input:data.get(d)){
                String[] datarow = new String[tableColumns];
                Calendar c = Calendar.getInstance();
                c.setTime(input.date);
                datarow[0] = Integer.toString(c.get(Calendar.YEAR));
                datarow[1] = round(input.benchmarkGains);
                datarow[2] = round(input.sectorGains);
                datarow[3] = round(input.PnF);
                
                //in case PnF trade never occured
                try{
                    datarow[4] = monthsdf.format(input.PnFEntry);
                    datarow[5] = monthsdf.format(input.PnFExit);
                }catch (Exception e){}
                
                datarow[7] = round(input.sectorGains-input.benchmarkGains);
                datarow[8] = round(input.PnF-input.benchmarkGains);
                datarow[9] = round(input.PnF-input.sectorGains);
                
                datarow[11] = fq(input.benchmarkGains);
                datarow[12] = fq(input.sectorGains);
                datarow[13] = fq(input.PnF);
                
                datarow[15] = fq(input.sectorGains-input.benchmarkGains);
                datarow[16] = fq(input.PnF-input.benchmarkGains);
                datarow[17] = fq(input.PnF-input.sectorGains);
                
                dataRows.add(datarow);
                table.add(datarow);
                
            }
            
            //Average Row
            String[] avgRow = new String[tableColumns];
            avgRow[0] = "Average";
            
            for(int i = 0;i<tableColumns;i++){
                //in case of empty or date row
                try{
                    avgRow[i] = round(calcAverage(dataRows, i));
                }catch(Exception e){}
            }
            
            String[] space = new String[tableColumns];
            table.add(space);

        }
        
        //Compounding Section
        String[] titleCompound = new String[tableColumns];
        titleCompound[0] = "Compound Growth";
        table.add(titleCompound);
        String[] headerCompound = new String[tableColumns];
        headerCompound[0] = "Benchmark Return";
        headerCompound[1] = name+" Return";
        headerCompound[2] = "DIFF";
        table.add(headerCompound);
        
        
        
        return table;
        
    }
    
    public String fq(double value){
        if(value>0){
            return "1";
        }
        return "0";
    }
    
    public int getFirstYear(DateSet d){
        int lowestYear = Integer.MAX_VALUE;        
        for(IndividualYearRow year:data.get(d)){
            Calendar c = Calendar.getInstance();
            c.setTime(year.date);
            
            if(c.get(Calendar.YEAR)<lowestYear){
                lowestYear = c.get(Calendar.YEAR);
            }
        }
        return lowestYear;
    }
    
    public int getLastYear(DateSet d){
        int highestYear = Integer.MIN_VALUE;        
        for(IndividualYearRow year:data.get(d)){
            Calendar c = Calendar.getInstance();
            c.setTime(year.date);
            
            if(c.get(Calendar.YEAR)>highestYear){
                highestYear = c.get(Calendar.YEAR);
            }
        }
        return highestYear;
    }

    private double calcAverage(ArrayList<String[]> dataRows, int i) {
        double sum = 0;
        double count = 0;
        for(String[] row:dataRows){
            sum+=Double.parseDouble(row[i]);
            count++;
        }
        
        return sum/count;
    }

    
}
