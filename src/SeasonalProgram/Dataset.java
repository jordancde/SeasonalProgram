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
    
    public Dataset(String[][] data,int setNum, int nameListPos, int dataColumnCount) throws ParseException{
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
        int lastRow = data.length;
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
        volumes = new double[lastRow-rowOffset-nameListPos];
        
        //Under the dataset name list by two rows starts the data
        for(int i = 0;i<lastRow-nameListPos-rowOffset;i++){
            dates[i]=new SimpleDateFormat("yyyy/MM/dd").parse(data[lastRow-i-1][startColumn]);
            opens[i]=Double.parseDouble(data[lastRow-i-1][startColumn+1]);
            highs[i]=Double.parseDouble(data[lastRow-i-1][startColumn+2]);
            lows[i]=Double.parseDouble(data[lastRow-i-1][startColumn+3]);
            closes[i]=Double.parseDouble(data[lastRow-i-1][startColumn+4]);
            volumes[i] = Double.parseDouble(data[lastRow-i-1][startColumn+5]);
        }

    }
    
    
    
    public void trimData(Date startDate, Date endDate){
        int startDateIndex = 0;
        for(int i = 0;i<dates.length;i++){
            if(dates[i].compareTo(startDate)>=0){
                startDateIndex = i;
                break;
            }
        }
        Date[] tempDates = new Date[dates.length-startDateIndex-1];
        double[] tempOpens = new double[dates.length-startDateIndex-1];
        double[] tempHighs = new double[dates.length-startDateIndex-1];
        double[] tempLows = new double[dates.length-startDateIndex-1];
        double[] tempCloses = new double[dates.length-startDateIndex-1];
        double[] tempVolumes = new double[dates.length-startDateIndex-1];
        
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
