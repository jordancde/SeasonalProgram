/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author jordandearsley
 */
public class Dataset {
    public String name;
    public double[][] set;
    
    public Date[] dates;
    public double[] opens;
    public double[] highs;
    public double[] lows;
    public double[] closes;
    public double[] volumes;
    
    public String[][] data;
    public int setNum;
    public int nameListPos;
    public int dataColumnCount;
    
    public Dataset(String[][] data,int setNum, int nameListPos, int dataColumnCount) throws ParseException{
        this.data = data;
        this.setNum = setNum;
        this.nameListPos = nameListPos;
        this.dataColumnCount = dataColumnCount;
        
        String[] nameHeader = data[nameListPos];
        //how many rows after header data starts
        int rowOffset = 2;
        int columns = data[nameListPos].length;
        
        //finds the position of the header #setNum
        int startColumn = -1;
        
        int count = 0;
        
        for(int i = 0; i<columns;i++){
            if(!nameHeader[i].equals("")){
                if (count == setNum){
                    startColumn = i;
                    name = nameHeader[i];
                    break;
                }else{
                    count++;
                }
            }
        }
        
        //count rows
        //Under the dataset name list by two rows starts the data
        int lastRow = data.length-1;
        for(int i = nameListPos+rowOffset;i<data.length;i++){
            if("".equals(data[i][startColumn])){
                lastRow = i;
                break;
            }
           
        }
        

        //initialize arrays
        dates = new Date[lastRow-nameListPos-rowOffset];
        opens = new double[lastRow-rowOffset-nameListPos];
        highs = new double[lastRow-rowOffset-nameListPos];
        lows = new double[lastRow-rowOffset-nameListPos];
        closes = new double[lastRow-rowOffset-nameListPos];
        //volumes = new double[lastRow-rowOffset-nameListPos];
        
        //Under the dataset name list by two rows starts the data
        for(int i = 0;i<lastRow-nameListPos-rowOffset;i++){
            dates[i]=new SimpleDateFormat("yyyy/MM/dd").parse(data[lastRow-i-1][startColumn]);
            opens[i]=Double.parseDouble(data[lastRow-i-1][startColumn+1]);
            highs[i]=Double.parseDouble(data[lastRow-i-1][startColumn+2]);
            lows[i]=Double.parseDouble(data[lastRow-i-1][startColumn+3]);
            closes[i]=Double.parseDouble(data[lastRow-i-1][startColumn+4]);
            //volumes[i] = Double.parseDouble(data[lastRow-i-1][startColumn+5]);
        }

    }
    public Dataset(String[][] data) throws ParseException{
        this.data = data;
        
        String[] nameHeader = data[0];
        //how many rows after header data starts
        int rowOffset = 0;
        int columns = data[0].length;
        
        //finds the position of the header #setNum
        int startColumn = 0;
        
        
        
        //count rows
        //Under the dataset name list by two rows starts the data
        int lastRow = data.length;

        //initialize arrays
        dates = new Date[data.length];
        opens = new double[data.length];
        highs = new double[data.length];
        lows = new double[data.length];
        closes = new double[data.length];
        
        //Under the dataset name list by two rows starts the data
        for(int i = 0;i<lastRow-1;i++){
            
            dates[i]=new SimpleDateFormat("yyyy/MM/dd").parse(data[i][0]);
            opens[i]=Double.parseDouble(data[i][1]);
            highs[i]=Double.parseDouble(data[i][2]);
            lows[i]=Double.parseDouble(data[i][3]);
            closes[i]=Double.parseDouble(data[i][4]);
        }

    }
    
    public Dataset compareTo(Dataset s) throws ParseException{
         System.out.println("test");

        int arrayLength = Math.min(s.dates.length, dates.length);
        String[][] newData = new String[arrayLength+1][5];


        for(int i = dates.length-1;i>=0;i--){
            newData[i][0] = new SimpleDateFormat("yyyy/MM/dd").format(dates[i]);
            newData[i][1] = Double.toString(opens[i]/s.opens[i]);
            newData[i][2] = Double.toString(highs[i]/s.highs[i]);
            newData[i][3] = Double.toString(lows[i]/s.lows[i]);
            newData[i][4] = Double.toString(closes[i]/s.closes[i]);
        }
        Dataset d = new Dataset(newData);
        
        return d;
    }
    
    public void trimData(Date startDate, Date endDate){
        int startDateIndex = 0;
        for(int i = 0;i<dates.length;i++){
            if(dates[i].compareTo(startDate)>=0){
                startDateIndex = i;
                break;
            }
        }
        int endDateIndex = 0;
        for(int i = 0;i<dates.length;i++){
            if(dates[i].compareTo(endDate)>=0){
                endDateIndex = i;
                break;
            }
        }
        
        Date[] tempDates = new Date[dates.length-startDateIndex-1 - (dates.length-endDateIndex-1)];
        double[] tempOpens = new double[dates.length-startDateIndex-1 - (dates.length-endDateIndex-1)];
        double[] tempHighs = new double[dates.length-startDateIndex-1 - (dates.length-endDateIndex-1)];
        double[] tempLows = new double[dates.length-startDateIndex-1 - (dates.length-endDateIndex-1)];
        double[] tempCloses = new double[dates.length-startDateIndex-1 - (dates.length-endDateIndex-1)];
        double[] tempVolumes = new double[dates.length-startDateIndex-1 - (dates.length-endDateIndex-1)];
        
        for(int i = 0;i<tempDates.length;i++){
            tempDates[i] = dates[startDateIndex+i];
            tempOpens[i] = opens[startDateIndex+i];
            tempHighs[i] = highs[startDateIndex+i];
            tempLows[i] = lows[startDateIndex+i];
            tempCloses[i] = closes[startDateIndex+i];
            tempVolumes[i] = volumes[startDateIndex+i];
        }
        dates = tempDates;
        opens = tempOpens;
        highs = tempHighs;
        lows = tempLows;
        closes = tempCloses;
        volumes = tempVolumes;
    }
    

    
    
}
