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
public class MonthlyStaticReturnsTable extends Table {
    
    public MonthlyStaticReturnsTable(String name, Map<String, Double> data, Date startDate, Date endDate) throws IOException {
        super(name, data, startDate, endDate);
        portfolioTable = makeTable(data);
    }
    
    public ArrayList<String[]> makeTable(Map<String,Double> data){
        
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
        
        int[] monthValueCounts = new int[12];
        double[] monthValueTotals = new double[12];
        
        //data rows
        for(int i = getFirstYear();i<=getLastYear();i++){
            String[] row = new String[tableColumns];
            row[0] = Integer.toString(i);
            for(int j = getFirstMonth(i,data);j<=getLastMonth(i,data);j++){
                String dateString = j+"/"+i;
                String returnValue = String.format("%.5f", data.get(dateString));
                row[j+1] = returnValue;
                if(data.get(dateString)!=null){
                    monthValueTotals[j]+=data.get(dateString);
                }
                monthValueCounts[j]++;
            }
            row[row.length-1] = round(getAverage(row));
            row[row.length-2] = round(getTotal(row));
            table.add(row);
        }
        //Total Row
        String[] total = new String[tableColumns];
        total[0] = "TOTAL";
        for(int i = 0;i<12;i++){
            total[i+1] = round(monthValueTotals[i]);
        }
        total[total.length-1] = round(getAverage(total));
        total[total.length-2] = round(getTotal(total));
        table.add(total);

        //Average Row
        String[] average = new String[tableColumns];
        average[0] = "AVG";
        for(int i = 0;i<12;i++){
            average[i+1] = round(monthValueTotals[i]/monthValueCounts[i]);
        }
        average[average.length-1] = round(getAverage(average));
        average[average.length-2] = round(getTotal(average));
        table.add(average);
        
        return table;
    }
}
