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
    public int tableColumns = 18;
    public SimpleDateFormat monthsdf = new SimpleDateFormat("MM/dd");
    
    public IndividualTable(String name, Date startDate, Date endDate,HashMap<DateSet,ArrayList<IndividualYearRow>> data) throws IOException{
        super(name, startDate, endDate);
        this.data = data;
        table = makeTable(data);
    }
    
    //@Override
    public ArrayList<String[]> makeTable(HashMap<DateSet,ArrayList<IndividualYearRow>> data){
        ArrayList<String[]> table = new ArrayList<String[]>();
        
        
        ArrayList<DateSet> orderedDates = new ArrayList<DateSet>();
        for(DateSet d:data.keySet()){
            if(orderedDates.size()==0){
                orderedDates.add(d);
                
            }else{
                int index = 0;
                for(DateSet i:orderedDates){
                    
                    if(i.startDate.after(d.startDate)){
                        index = orderedDates.indexOf(i);
                        
                        break;
                    }
                }
                orderedDates.add(index, d);

               
            }
        }
        
        for(DateSet d:orderedDates){
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
            
            for(int i = 1;i<tableColumns;i++){
                //in case of empty or date row
                try{
                    avgRow[i] = round(calcAverage(dataRows, i));
                }catch(Exception e){}
            }
            table.add(avgRow);
            String[] space = new String[tableColumns];
            table.add(space);

        }
        
        //Compounding Section
        String[] titleCompound = new String[tableColumns];
        titleCompound[0] = "Compound Growth";
        table.add(titleCompound);
        String[] headerCompound = new String[tableColumns];
        headerCompound[1] = "Benchmark Return";
        headerCompound[2] = name+" Return";
        headerCompound[3] = name+ " PnF Return";
        headerCompound[4] = name+" Return - Benchmark";
        headerCompound[5] = "PnF - "+name;
        headerCompound[6] = "PnF - Benchmark";
        
        table.add(headerCompound);
        
        ArrayList<Integer> years = new ArrayList<Integer>();
        ArrayList<ArrayList<Double>> benchMark = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Double>> sector = new ArrayList<ArrayList<Double>>();
        ArrayList<ArrayList<Double>> pnF = new ArrayList<ArrayList<Double>>();

        
        

        //sorry shit code, just wanted a quick fix
        boolean checked = false;
        
        for(DateSet d:data.keySet()){
            for(int i = 0;i<data.get(d).size();i++){
                if(i==0&&!checked){
                    
                    for(int j = 0;j<data.get(d).size();j++){
                        years.add(0);
                        benchMark.add(new ArrayList<Double>());
                        sector.add(new ArrayList<Double>());
                        pnF.add(new ArrayList<Double>());

                    }
                    checked = true;
                }
                Calendar c = Calendar.getInstance();
                c.setTime(data.get(d).get(i).date);
                years.set(i, c.get(Calendar.YEAR));
                
                if(d.longShort.equals("Long")){
                    benchMark.get(i).add(data.get(d).get(i).benchmarkGains);
                    sector.get(i).add(data.get(d).get(i).sectorGains);
                    pnF.get(i).add(data.get(d).get(i).PnF);
                }else{
                    benchMark.get(i).add(-1*data.get(d).get(i).benchmarkGains);
                    sector.get(i).add(-1*data.get(d).get(i).sectorGains);
                    pnF.get(i).add(-1*data.get(d).get(i).PnF);
                }

                
                
            }
            
            
        }
        ArrayList<String[]> compoundRows = new ArrayList<String[]>();
        for(int i = 0;i<benchMark.size();i++){
            String[] row = new String[tableColumns];
            
            row[0] = Integer.toString(years.get(i));
            row[1] = round(getCompoundedTotals(benchMark.get(i)));
            row[2] = round(getCompoundedTotals(sector.get(i)));
            row[3] = round(getCompoundedTotals(pnF.get(i)));
            row[4] = round(getCompoundedTotals(sector.get(i))-getCompoundedTotals(benchMark.get(i)));
            row[5] = round(getCompoundedTotals(pnF.get(i))-getCompoundedTotals(sector.get(i)));
            row[6] = round(getCompoundedTotals(pnF.get(i))-getCompoundedTotals(benchMark.get(i)));
            compoundRows.add(row);
            
            table.add(row);
        }
        
        
        String[] avgRow2 = new String[tableColumns];
        avgRow2[0] = "Average";
            
        for(int i = 1;i<tableColumns;i++){
            //in case of empty or date row
            try{
                avgRow2[i] = round(calcAverage(compoundRows, i));
            }catch(Exception e){}
        }
        table.add(avgRow2);
        
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

   public boolean compareDates(Date date1,Date date2){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        
        
        if(cal1.get(Calendar.MONTH)>cal2.get(Calendar.MONTH)){
            return true;
        }else if(cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH)){
            if(cal1.get(Calendar.DAY_OF_MONTH)>cal2.get(Calendar.DAY_OF_MONTH)){
                return true;
            }
        }
        return false;
        
    }

    
}
