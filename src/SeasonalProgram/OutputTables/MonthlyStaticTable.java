/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram.OutputTables;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author jordandearsley
 */
public class MonthlyStaticTable extends Table {
    public ArrayList<String[]> dataRows;
    public ArrayList<String[]> totalRows;
    
    public final int PORTFOLIO = 0;
    public final int BENCHMARK = 1;
    public final int RELATIVE_PORTFOLIO = 2;
    public final int FREQ_PORTFOLIO = 3;
    public final int FREQ_BENCHMARK = 4;
    public final int FREQ_RELATIVE_PORTFOLIO = 5;
    public final int CASH = 6;
    public final int TRADES = 7;
    
    
    public int type;
    
    public MonthlyStaticTable(String name, Map<String, Double[]> data, Date startDate, Date endDate, int type) throws IOException {
        super(name, startDate, endDate);
        this.type = type;
        portfolioTable = makeTable(data);
    }
    
    public ArrayList<String[]> makeTable(Map<String, Double[]> data){
        
        ArrayList<String[]> table = new ArrayList<String[]>();
        //Title Row
        String[] title = new String[tableColumns];
        title[0] = name;
        table.add(title);
        //Dates Header Row
        String[] dates = new String[tableColumns];
        String[] months = new DateFormatSymbols().getMonths();
        for(int i = 0;i<months.length;i++){
            dates[i+1] = months[i];
        }
        dates[dates.length-2] = "Total Year";
        dates[dates.length-1] = "Avg";
        table.add(dates);
        
        ArrayList<ArrayList<Double>> monthValueTotals = new ArrayList<ArrayList<Double>>();
        for(int i = 0;i<12;i++){monthValueTotals.add(new ArrayList<Double>());}
        double[] monthValueCounts = new double[12];
        
        for(int i = getFirstYear();i<=getLastYear();i++){
            
            String[] row = new String[tableColumns];
            row[0] = Integer.toString(i);
            for(int j = getFirstMonth(i,data);j<=getLastMonth(i,data);j++){
                String dateString = j+"/"+i;
                //to account for no trades
                try{
                    row[j+1] = String.format("%.5f", data.get(dateString)[0]);
                    monthValueCounts[j]++;
                    monthValueTotals.get(j).add(data.get(dateString)[0]);
                }catch(Exception e){
                    row[j+1] = String.format("%.5f", 0.0);
                    monthValueCounts[j]++;
                    monthValueTotals.get(j).add(0.0);
                }
                
                
            }
            
            if(type>2){
                row[row.length-1] = round(getTotal(row)/countValues(row));
                row[row.length-2] = round(getTotal(row));
            }else if(type==0||type==1){
                String startString = getFirstMonth(i,data)+"/"+i;
                String endString = getLastMonth(i,data)+"/"+i;
                row[row.length-1] = round((data.get(endString)[2]-data.get(startString)[1])/data.get(startString)[1]/countValues(row));
                row[row.length-2] = round((data.get(endString)[2]-data.get(startString)[1])/data.get(startString)[1]);
            }else if(type==2){
                String startString = getFirstMonth(i,data)+"/"+i;
                String endString = getLastMonth(i,data)+"/"+i;
                double coreGrowth = (data.get(endString)[4]-data.get(startString)[3])/data.get(startString)[3];
                double portfolioGrowth = (data.get(endString)[2]-data.get(startString)[1])/data.get(startString)[1];
                row[row.length-1] = round((portfolioGrowth-coreGrowth)/countValues(row));
                row[row.length-2] = round(portfolioGrowth-coreGrowth);
            }
            table.add(row);
        }
        
        //Total Row
        String[] total = new String[tableColumns];
        total[0] = "TOTAL";
        for(int i = 0;i<12;i++){
            if(type>2){
                total[i+1] = round(getMonthlyTotal(monthValueTotals.get(i)));
            }else{
                total[i+1] = round(getCompoundedTotals(monthValueTotals.get(i)));
            }
        }

        table.add(total);

        //Average Row
        String[] average = new String[tableColumns];
        average[0] = "AVG";
        for(int i = 0;i<12;i++){
            average[i+1] = round(Double.parseDouble(total[i+1])/monthValueCounts[i]);
        }

        table.add(average);
        
        return table;
    }
    
    public int countValues(String[] row){
        int sum = 0;
        for(String d: row){
            //check
            if(d!=null){
                sum++;
            }
        }
        return sum-1;
    }
  

}