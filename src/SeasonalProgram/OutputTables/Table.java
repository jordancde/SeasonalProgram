/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram.OutputTables;

import SeasonalProgram.SeasonalProgram;
import SeasonalProgram.SeasonalProgram;
import SeasonalProgram.Trade;
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
import java.util.Map;

/**
 *
 * @author jordandearsley
 */
public class Table {
    public String name;
    public ArrayList<String[]> portfolioTable;
    public int tableColumns = 17;
    public Date startDate;
    public Date endDate;
    public String outputName = "Out";
    public Table(String name, Map<String,Double> data, Date startDate, Date endDate) throws IOException{
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        
        
    }
    public Table(String name, ArrayList<Trade> data, Date startDate, Date endDate) throws IOException {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }
 
    
    public String round(double d){
        return String.format("%.5f", d);
    }
    
    //START HERE
    public double getTotal(String[] input){
        double total = 0;
        for(int i = 1;i<=12;i++){
            if(!(input[i]==null)&&!(input[i].equals("null"))){
                total+=Double.parseDouble(input[i]);
            }
        }
        return total;
    }
    public double getAverage(String[] input){
        double total = 0;
        int count = 0;
        for(int i = 1;i<=12;i++){
            if(!(input[i]==null)&&!(input[i].equals("null"))){
                total+=Double.parseDouble(input[i]);
                count++;
            }
        }
        return total/count;
    }
    
    public int getFirstMonth(int year, Map<String,Double> data){
        ArrayList<String> datesInYear = new ArrayList<String>();
        for(String s:data.keySet()){
            if(Integer.parseInt(s.substring(s.lastIndexOf("/") + 1))==year){
                datesInYear.add(s);
                
            }
        }
        ArrayList<Integer> monthsInYear = new ArrayList<Integer>();
        int minMonth = Integer.MAX_VALUE;
        for(String s:datesInYear){
            
            if(Integer.parseInt(s.substring(0,s.indexOf("/")))<minMonth){
                minMonth = Integer.parseInt(s.substring(0,s.indexOf("/")));
                
            }
        }
        //System.out.println(year+"/"+minMonth);
        return minMonth;
    }
    public int getLastMonth(int year, Map<String,Double> data){
        ArrayList<String> datesInYear = new ArrayList<String>();
        for(String s:data.keySet()){
            if(Integer.parseInt(s.substring(s.lastIndexOf("/") + 1))==year){
                datesInYear.add(s);
            }
        }
        ArrayList<Integer> monthsInYear = new ArrayList<Integer>();
        int maxMonth = 0;
        for(String s:datesInYear){
            if(Integer.parseInt(s.substring(0,s.indexOf("/")))>maxMonth){
                maxMonth = Integer.parseInt(s.substring(0,s.indexOf("/")));
            }
        }
        //System.out.println(year+"/"+maxMonth);
        return maxMonth;
    }
    
    
    public void writeTable() throws IOException{
        URL location = SeasonalProgram.class.getProtectionDomain().getCodeSource().getLocation();

        String path = location.getFile()+"OUTPUT "+outputName+".csv";
            
        if(!(new File(path).canRead())){
            (new File(path)).createNewFile();
        }/*else{
            System.out.println("output file name exists");
            return;
        }*/
        
        /*for(String[] s:table){
            for(String string:s){
                System.out.print(string+",");
            }
            System.out.println("");
        }*/
        
        try(
            FileWriter fw = new FileWriter(path, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {      
            out.println("");
            for(String[] row:portfolioTable){
                for(String s: row){
                    if(s==null){
                        s = "";
                    }else if(s.equals("null")){
                        s = "0";
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
