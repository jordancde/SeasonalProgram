/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import SeasonalProgram.Trade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import SeasonalProgram.Core;
import SeasonalProgram.OutputTables.Table;
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
        
        for(Trade buyTrade:data){
            if(buyTrade.to instanceof Sector){
                String[] row = new String[tableColumns];
                row[0] = buyTrade.to.name;
                row[1] = buyTrade.date.toString();
                
                double sectorBuyValue = 0;
                double sectorSellValue = 0;
                double coreBuyValue = 0;
                double coreSellValue= 0;
                
                Trade sellTrade = buyTrade.sell;
        
                if(sellTrade==null){
                    System.out.println("Null");
                    sectorBuyValue = SeasonalProgram.portfolio.getValue(buyTrade.date,buyTrade.to);
                    sectorSellValue = SeasonalProgram.portfolio.getValue(endDate,buyTrade.to);//???
                    coreBuyValue = SeasonalProgram.portfolio.getCoreValue(buyTrade.date);
                    coreSellValue = SeasonalProgram.portfolio.getCoreValue(endDate);
                    row[2] = endDate.toString();
                    row[3] = Double.toString((sectorSellValue-sectorBuyValue)/sectorBuyValue);
                    row[4] = Double.toString((coreSellValue-coreBuyValue)/coreBuyValue);
                }else{
                    System.out.println("Not Null");

                    row[2] = sellTrade.date.toString();
                                                
                    sectorBuyValue = SeasonalProgram.portfolio.getValue(buyTrade.date,buyTrade.to);
                    sectorSellValue = SeasonalProgram.portfolio.getValue(sellTrade.date,sellTrade.from);//???
                    coreBuyValue = SeasonalProgram.portfolio.getCoreValue(buyTrade.date);
                    coreSellValue = SeasonalProgram.portfolio.getCoreValue(sellTrade.date);

                    row[3] = Double.toString((sectorSellValue-sectorBuyValue)/sectorBuyValue);
                    row[4] = Double.toString((coreSellValue-coreBuyValue)/coreBuyValue);
                }
                
                row[5] = Double.toString(Double.parseDouble(row[3])-Double.parseDouble(row[4]));
                table.add(row);
            }   
        }
        
        
        String[] avgRow = new String[tableColumns];
        avgRow[0] = "Average";
        for(int i = 0;i<3;i++){
            double sum = 0;
            for(int j = 0;j<table.size()-4;j++){
                sum += Double.parseDouble(table.get(j+4)[3+i]);
            }
            avgRow[3+i] = Double.toString(sum/(table.size()-4));
        }
        table.add(avgRow);
        
        String[] fqRow = new String[tableColumns];
        fqRow[0] = "Fq > 0";
        for(int i = 0;i<3;i++){
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
