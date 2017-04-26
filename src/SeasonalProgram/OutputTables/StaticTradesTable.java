/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram.OutputTables;

import SeasonalProgram.Trade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import SeasonalProgram.Core;
import SeasonalProgram.Sector;

/**
 *
 * @author jordandearsley
 */
public class StaticTradesTable extends Table {

    public StaticTradesTable(String name, ArrayList<Trade> data, Date startDate, Date endDate) throws IOException {
        super(name, data, startDate, endDate);
        portfolioTable = makeTable(data);
    }
    
    public ArrayList<String[]> makeTable(ArrayList<Trade> data){
        ArrayList<String[]> table = new ArrayList<String[]>();
        
        String[] title = new String[tableColumns];
        title[0] =  name;
        table.add(title);
        
        String[] startDateRow = new String[tableColumns];
        startDateRow[0] = "Start Date";
        startDateRow[1] = startDate.toString();
        table.add(startDateRow);
        
        String[] endDateRow = new String[tableColumns];
        endDateRow[0] = "End Date";
        endDateRow[1] = endDate.toString();
        table.add(endDateRow);
        
        String[] tableTitles = new String[tableColumns];
        tableTitles[0] = "Sector";
        tableTitles[1] = "Start Date";
        tableTitles[2] = "End Date";
        tableTitles[3] = "Sector Gain";
        tableTitles[4] = "Benchmark";
        tableTitles[5] = "Difference";
        table.add(tableTitles);
        
        for(Trade t:data){
            if(t.to instanceof Sector){
                String[] row = new String[tableColumns];
                row[0] = t.to.name;
                row[1] = t.to.buyDate.toString();
                row[2] = t.to.sellDate.toString();
                for(Trade sellTrade:data){
                    if(sellTrade.from.equals(t.to)){
                        row[3] = Double.toString((t.toValue-sellTrade.fromValue)/sellTrade.fromValue);
                        row[4] = Double.toString((t.fromValue-sellTrade.toValue)/sellTrade.toValue);
                        break;
                    }
                }  
                row[5] = Double.toString(Double.parseDouble(row[3])-Double.parseDouble(row[4]));
                System.out.println("Worked");
                table.add(row);
            }   
        }
        
        String[] avgRow = new String[tableColumns];
        avgRow[0] = "Average";
        for(int i = 0;i<2;i++){
            double sum = 0;
            for(int j = 0;j<table.size()-4;j++){
                sum += Double.parseDouble(table.get(j+4)[3+i]);
            }
            avgRow[3+i] = Double.toString(sum/(table.size()-4));
        }
        table.add(avgRow);
        
        String[] fqRow = new String[tableColumns];
        fqRow[0] = "Fq > 0";
        for(int i = 0;i<2;i++){
            double sum = 0;
            //-5 to skip average row
            for(int j = 0;j<table.size()-5;j++){
                if(Double.parseDouble(table.get(j+4)[3+i])>0){
                    sum++;
                }
            }
            fqRow[3+i] = Double.toString(sum);
        }
        table.add(fqRow);
        
        return table;
        
    }
}
