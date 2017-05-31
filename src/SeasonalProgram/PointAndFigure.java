/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import SeasonalProgram.OutputTables.Table;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author jordandearsley
 */
public class PointAndFigure extends Table{
    
    public ArrayList<Double[]> boxSizes;
    public int reversalBoxes;
    
    public PointAndFigure(String name, Dataset data, Date startDate, Date endDate, 
            ArrayList<Double[]> boxSizes, int reversalBoxes) throws IOException{
        super(name, data, startDate, endDate);
        
        this.boxSizes = boxSizes;
        this.reversalBoxes = reversalBoxes;
        
        data.trimData(startDate, endDate);
        
        ArrayList<Integer> boxes = getBoxes();
        
        ArrayList<Integer> cleanedBoxes = cleanBoxes(boxes, reversalBoxes);
        
        table = makeTable(cleanedBoxes, boxSizes);
        
        writeTable();
    }
    
    
    public ArrayList<String[]> makeTable(ArrayList<Integer> cleanedBoxes, ArrayList<Double[]> boxSizes){
        ArrayList<ArrayList<String>> boxes = new ArrayList<ArrayList<String>>();
        double currentValue = 0;
        while(currentValue<boxSizes.get(boxSizes.size()-1)[1]){
            ArrayList<String> row = new ArrayList<String>();
            row.add(Double.toString(currentValue));
            boxes.add(row);
            currentValue+=getBoxSize(currentValue);
        }
        Collections.reverse(boxes);

        int row = getRow(data.opens[0],boxes);
        int column = 1;
        
        for(Integer streak:cleanedBoxes){
            if(streak>0){
                for(int i = 0;i<streak;i++){
                    boxes.get(row-i).add("x");
                }
                row-=streak;
                
            }else{
                for(int i = streak;i<0;i++){
                    boxes.get(row-i).add("o");
                }
                row-=streak;
                
            }
            column++;
            for(ArrayList<String> stringRow:boxes){
                while(stringRow.size()<column){
                    stringRow.add("");
                }
            }
        }
        
        ArrayList<String[]> table = new ArrayList<String[]>();
        for(ArrayList<String> stringRow:boxes){
            String[] rawRow = new String[stringRow.size()];
            for(int i = 0;i<stringRow.size();i++){
                rawRow[i] = stringRow.get(i);
            }
            table.add(rawRow);
        }
        return table;
        
    }
    
    public int getRow(double value,ArrayList<ArrayList<String>> boxes){

        int index = 0;
        for(ArrayList<String> row:boxes){
            if(value<Double.parseDouble(row.get(0))){
                index = boxes.indexOf(row);            
            }
        }
        return index;
    }
    
    public ArrayList<Integer> cleanBoxes(ArrayList<Integer> boxes, int reversalBoxes){
        ArrayList<Integer> cleanedBoxes = new ArrayList<Integer>();
       
        for(int i = 0;i<boxes.size()-1;i++){
            int addedIndex = 0;
            while(boxes.get(i)==boxes.get(i+addedIndex)){
                addedIndex++;
            }
            if(boxes.get(i)==1){
                cleanedBoxes.add(addedIndex);
            }else{
                cleanedBoxes.add(-1*addedIndex);
            }
            
            i = i+addedIndex-1;
        }
        return cleanedBoxes;
    }
    
    public ArrayList<Integer> getBoxes(){
        ArrayList<Integer> boxes = new ArrayList<Integer>();
        ArrayList<double[]> datasets = new ArrayList<double[]>();
        datasets.add(data.opens);
        datasets.add(data.highs);
        datasets.add(data.lows);
        datasets.add(data.closes);
        
        double lastBoxValue = data.opens[0];
        for(int i = 0;i<data.dates.length;i++){
            for(double[] column: datasets){
               
                if(Math.abs(column[i]-lastBoxValue)>=getBoxSize(column[i])){
                    if(column[i]-lastBoxValue>0){
                        int numBoxes = (int)Math.floor((column[i]-lastBoxValue)/getBoxSize(column[i]));
                        for(int j = 0;j<numBoxes;j++){
                            boxes.add(1);
                        }
                    }else{
                        int numBoxes = (int)Math.floor((lastBoxValue-column[i])/getBoxSize(column[i]));
                        for(int j = 0;j<numBoxes;j++){
                            boxes.add(0);
                        }
                    }
                    lastBoxValue = column[i];
                }
            } 
        }
        return boxes;
    }
    
    public double getMax(double[] values){
        double max = Double.MIN_VALUE;
        for(double d:values){
            if(d>max){
                max = d;
            }
        }
        return max;
    }
    
    public double getBoxSize(double value){
        
        for(Double[] range:boxSizes){
            if(value>range[0]&&value<=range[1]||value==0&&value==range[0]){
                return range[2];
            }
        }
        return -1;
    }
    
  
}
