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
    
    public Date[] trimdates;
    public double[] trimopens;
    public double[] trimhighs;
    public double[] trimlows;
    public double[] trimcloses;
    public double[] trimvolumes;
    
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
        for(int i = 0;i<lastRow;i++){
            
            dates[i]=new SimpleDateFormat("yyyy/MM/dd").parse(data[lastRow-i-1][0]);
            opens[i]=Double.parseDouble(data[lastRow-i-1][1]);
            highs[i]=Double.parseDouble(data[lastRow-i-1][2]);
            lows[i]=Double.parseDouble(data[lastRow-i-1][3]);
            closes[i]=Double.parseDouble(data[lastRow-i-1][4]);
        }

    }
    
    public Dataset compareTo(Dataset s) throws ParseException{

        int arrayLength = Math.min(s.dates.length, dates.length);
        String[][] newData = new String[arrayLength][5];


        for(int i = 0;i<arrayLength;i++){
            newData[i][0] = new SimpleDateFormat("yyyy/MM/dd").format(dates[dates.length-1-i]);
            newData[i][1] = Double.toString(opens[opens.length-1-i]/s.opens[s.opens.length-1-i]);
            newData[i][2] = Double.toString(highs[highs.length-1-i]/s.highs[s.highs.length-1-i]);
            newData[i][3] = Double.toString(lows[lows.length-1-i]/s.lows[s.lows.length-1-i]);
            newData[i][4] = Double.toString(closes[closes.length-1-i]/s.closes[s.closes.length-1-i]);
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
        
        Date[] tempDates = new Date[endDateIndex-startDateIndex];
        double[] tempOpens = new double[endDateIndex-startDateIndex];
        double[] tempHighs = new double[endDateIndex-startDateIndex];
        double[] tempLows = new double[endDateIndex-startDateIndex];
        double[] tempCloses = new double[endDateIndex-startDateIndex];
        double[] tempVolumes = new double[endDateIndex-startDateIndex];
        
        for(int i = 0;i<tempDates.length;i++){
            tempDates[i] = dates[startDateIndex+i];
            tempOpens[i] = opens[startDateIndex+i];
            tempHighs[i] = highs[startDateIndex+i];
            tempLows[i] = lows[startDateIndex+i];
            tempCloses[i] = closes[startDateIndex+i];
//            tempVolumes[i] = volumes[startDateIndex+i];
        }
        trimdates = tempDates;
        trimopens = tempOpens;
        trimhighs = tempHighs;
        trimlows = tempLows;
        trimcloses = tempCloses;
        trimvolumes = tempVolumes;
    }
    
    
    
    
}
