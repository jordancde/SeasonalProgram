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
public class NewDataset {
    public String name;
    public double[][] set;
    
    public Date[] dates;
    public double[] values;
    
    public Date[] trimdates;
    public double[] trimvalues;
    
    public String[][] data;
    public int setNum;
    public int nameListPos;
    public int dataColumnCount;
    
    public NewDataset(String[][] data,int setNum, int nameListPos, int dataColumnCount) throws ParseException{
        this.data = data;
        this.setNum = setNum;
        this.nameListPos = nameListPos;
        this.dataColumnCount = dataColumnCount;
        
        String[] nameHeader = data[nameListPos];
        //how many rows after header data starts
        int rowOffset = 3;
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
        values = new double[lastRow-rowOffset-nameListPos];

        //volumes = new double[lastRow-rowOffset-nameListPos];
        
        //Under the dataset name list by two rows starts the data
        for(int i = 0;i<lastRow-nameListPos-rowOffset;i++){
            dates[i]=new SimpleDateFormat("yyyy/MM/dd").parse(data[lastRow-i-1][startColumn]);
            values[i]=Double.parseDouble(data[lastRow-i-1][startColumn+1]);
        }
        
        

    }
    public NewDataset(String[][] data) throws ParseException{
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
        values = new double[data.length];

        
        //Under the dataset name list by two rows starts the data
        for(int i = 0;i<lastRow;i++){
            
            dates[i]=new SimpleDateFormat("yyyy/MM/dd").parse(data[lastRow-i-1][0]);
            values[i]=Double.parseDouble(data[lastRow-i-1][1]);

        }

    }
    
    public NewDataset compareTo(NewDataset s) throws ParseException{

        int arrayLength = Math.min(s.dates.length, dates.length);
        String[][] newData = new String[arrayLength][5];


        for(int i = 0;i<arrayLength;i++){
            newData[i][0] = new SimpleDateFormat("yyyy/MM/dd").format(dates[dates.length-1-i]);
            newData[i][1] = Double.toString(values[values.length-1-i]/s.values[s.values.length-1-i]);

        }
        NewDataset d = new NewDataset(newData);
        
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
        double[] tempValues = new double[endDateIndex-startDateIndex];
        
        for(int i = 0;i<tempDates.length;i++){
            tempDates[i] = dates[startDateIndex+i];
            tempValues[i] = values[startDateIndex+i];

//            tempVolumes[i] = volumes[startDateIndex+i];
        }
        trimdates = tempDates;
        trimvalues = tempValues;
    }
    
    
    
    
}
