/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import SeasonalProgram.OutputTables.Table;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author jordandearsley
 */
public class PointAndFigure extends Table{
    public int reversalBoxes;
    public int signalBoxes;
    
    public double currentBoxLevel;
    public ArrayList<Integer> columns;
    
    public ArrayList<ArrayList<Double>> boxes;
    
    public PointAndFigure(String name, Dataset data, Date startDate, Date endDate, 
            double boxSizePercent, int reversalBoxes, int signalBoxes) throws IOException, CloneNotSupportedException, ParseException{
        super(name, data, startDate, endDate);
        
        columns = new ArrayList<Integer>();
        this.reversalBoxes = reversalBoxes;
        this.signalBoxes = signalBoxes;
        this.data = data;
        
        data.trimData(data.dates[0],endDate);
        
        boxes = new ArrayList<ArrayList<Double>>();
        
        double currentValue = getMin(data.trimlows);
        double maxValue = getMax(data.trimhighs);
        double increment = Math.log(1+boxSizePercent/100);
        while(currentValue<maxValue){
            double lnOfValue = Math.log(currentValue);
            
            ArrayList<Double> row = new ArrayList<Double>();
            row.add(currentValue);
            boxes.add(row);
            
            lnOfValue+=increment;
            currentValue=Math.exp(lnOfValue);
        }
        Collections.reverse(boxes);
        
        
        ArrayList<double[]> datasets = new ArrayList<double[]>();
        //datasets.add(this.data.opens);
        //datasets.add(this.data.highs);
        //datasets.add(this.data.lows);
        datasets.add(data.trimcloses);
        
        ArrayList<ArrayList<String>> graph = runPnF(datasets,reversalBoxes);
        table = convertToArray(graph);
        
        //writeTable();
        
    }

    
    public ArrayList<ArrayList<String>> runPnF(ArrayList<double[]> datasets, int reversalBoxes){
        
        ArrayList<ArrayList<String>> filledBoxes = new ArrayList<ArrayList<String>>();
        for(ArrayList<Double> row:boxes){
            ArrayList<String> stringRow = new ArrayList<String>();
            stringRow.add(row.get(0).toString());
            filledBoxes.add(stringRow);
        }
        
        double currentValue = datasets.get(0)[0];
        int currentColumn = 0;
        
        boolean goingUp = false;
        boolean goingDown = false;
        
        for(int i = 0;i<datasets.get(0).length;i++){
            for(double[] column:datasets){
                if(!goingUp&&getCrossedBoxes(column[i],currentValue)>=reversalBoxes){
                    goingUp = true;
                    goingDown = false;
                    currentColumn++;
                    columns.add(0);
                }else if(!goingDown&&getCrossedBoxes(currentValue,column[i])>=reversalBoxes){
                    goingUp = false;
                    goingDown = true;
                    currentColumn++;
                    columns.add(0);
                }
                for(ArrayList<String> row:filledBoxes){
                    while(row.size()<currentColumn){
                        row.add("");
                    }
                }
                
                if(goingUp&&getCrossedBoxes(currentValue,column[i])>=1){
                    
                    while(column[i]>currentValue){
                        currentValue=getNextBox(true,currentValue);
                        filledBoxes.get(getRow(currentValue)).add("X");
                        columns.set(columns.size()-1, columns.get(columns.size()-1)+1);
                    }
                }else if(goingDown&&getCrossedBoxes(currentValue,column[i])>=1){
                    int nextRow = getRow(column[i]);
                    
                    while(column[i]<currentValue){
                        currentValue=getNextBox(false,currentValue);
                        filledBoxes.get(getRow(currentValue)).add("O");
                        columns.set(columns.size()-1, columns.get(columns.size()-1)-1);
                        
                    }
                }
                
                
            }
        }
        return filledBoxes;
        
    }
    

    
    public int getRow(double value){

        int index = 0;
        for(ArrayList<Double> row:boxes){
            if(value>=row.get(0)){
                index = boxes.indexOf(row);
                break;
            }
        }
        return index;
    }
    
    public ArrayList<String[]> convertToArray(ArrayList<ArrayList<String>> input){
        ArrayList<String[]> output = new ArrayList<String[]>();
        for(ArrayList<String> row: input){
            String[] stringRow = new String[row.size()];
            for(int i = 0;i<row.size();i++){
                stringRow[i] = row.get(i);
            }
            output.add(stringRow);
        }
        return output;
    }
    
    public double getValue(int row){
        return boxes.get(row).get(0);
    }
    //START HERE
    public int getCrossedBoxes(double startValue, double endValue){
        int crossedBoxes = 0;
     
        for(ArrayList<Double> row:boxes){
            if(startValue>=row.get(0)){
                
                if(endValue>startValue){
                    while(startValue<endValue){
                        startValue = boxes.get(boxes.indexOf(row)-1).get(0);
                        crossedBoxes++;
                    }
                }else if(endValue<startValue){
                    while(endValue<startValue){
                        startValue = boxes.get(boxes.indexOf(row)+1).get(0);
                        crossedBoxes++;
                    }
                }
                return crossedBoxes;
            }
        }
            
        
        return 0;
    }
    
    public double getNextBox(boolean upOrDown, double currentValue){
        for(ArrayList<Double> row:boxes){
            if(currentValue>=row.get(0)){
                if(upOrDown){
                    return boxes.get(boxes.indexOf(row)-1).get(0);
                }else{
                    return boxes.get(boxes.indexOf(row)+1).get(0);
                }
            }
        }
 
        return -1;
    }
    
    public double getMax(double[] trimhighs){
        double max = Integer.MIN_VALUE;
        for(double d: trimhighs){
            if(d>max){
                max = d;
            }
        
        }
        return max;
    }
    
    public double getMin(double[] trimlows){
        double min = Integer.MAX_VALUE;
        for(double d: trimlows){
            if(d<min){
                min = d;
            }
        
        }
        return min;
    }
    
    public boolean buySignal(){
        if(columns.get(columns.size()-1)>=signalBoxes){
            if(columns.get(columns.size()-1)>columns.get(columns.size()-3)){
                return true;
            }
        }
        return false;
    }
    public boolean sellSignal(){
        if(columns.get(columns.size()-1)<=signalBoxes*(-1)){
            if(columns.get(columns.size()-1)<columns.get(columns.size()-3)){
                return true;
            }
        }
        return false;
    }
}
