/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author jordandearsley
 */
public class Portfolio {
    public String name;
    public ArrayList<String[]> portfolioTable;
    public int tableColumns = 17;
    public Date startDate;
    public Date endDate;
    public Portfolio(String name, Date startDate, Date endDate) throws IOException{
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        //use one dataset to 
        portfolioTable = makeTable();
        //writeTable(name,portfolioTable);
    }
    
    public ArrayList<String[]> makeTable(){
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
        
        //data rows
        for(int i = getFirstYear();i<=getLastYear();i++){
            String[] row = new String[tableColumns];
            row[0] = Integer.toString(i);
            table.add(row);
        }
        //Total Row
        String[] total = new String[tableColumns];
        total[0] = "TOTAL";
        table.add(total);
        //Average Row
        String[] average = new String[tableColumns];
        average[0] = "AVG";
        table.add(average);
        
        return table;
    }
    
    public void writeTable(String name, ArrayList<String[]> table) throws IOException{
        URL location = SeasonalProgram.class.getProtectionDomain().getCodeSource().getLocation();

        String path = location.getFile()+"OUTPUT "+name+".csv";
            
        if(!(new File(path).canRead())){
            (new File(path)).createNewFile();
        }else{
            System.out.println("output file name exists");
            return;
        }
        try(
            FileWriter fw = new FileWriter(path, false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {       
            for(String[] row:table){
                for(String s: row){
                    if(s==null){
                        s = "";
                    }
                    out.print(s+",");
                }
                out.println("");
            }
            
        } catch (IOException e) {
            System.out.println(e);
        }
        
    }
    
    public int getFirstYear(){
        /*int lowestYear = Integer.MAX_VALUE;
        for(Dataset d:datasets){
            Calendar cal = Calendar.getInstance();
            cal.setTime(d.dates[0]);
            int year = cal.get(Calendar.YEAR);
            if(year<lowestYear){
                lowestYear = year;
            }
        }*/
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int year = cal.get(Calendar.YEAR);
        
        return year;
    }
    public int getLastYear(){
        /*int highestYear = Integer.MIN_VALUE;
        for(Dataset d:datasets){
            Calendar cal = Calendar.getInstance();
            cal.setTime(d.dates[d.dates.length-1]);
            int year = cal.get(Calendar.YEAR);
            if(year>highestYear){
                highestYear = year;
            }
        }*/
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        int year = cal.get(Calendar.YEAR);
        
        return year;
        
    }


    
}
