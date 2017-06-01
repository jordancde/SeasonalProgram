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
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author jordandearsley
 */
public class PointAndFigure2 extends Table{
    
    public ArrayList<Double[]> boxSizes;
    public int reversalBoxes;
    
    public double currentBoxLevel;
    
    public PointAndFigure2(String name, Dataset data, Date startDate, Date endDate, 
            ArrayList<Double[]> boxSizes, int reversalBoxes) throws IOException, CloneNotSupportedException, ParseException{
        super(name, data, startDate, endDate);
        
        this.boxSizes = boxSizes;
        this.reversalBoxes = reversalBoxes;
        
        this.data = data;
        this.data = new Dataset(data.data,data.setNum,data.nameListPos,data.dataColumnCount);
        this.data.trimData(startDate, endDate);
        
        ArrayList<ArrayList<Double>> boxes = new ArrayList<ArrayList<Double>>();
        double currentValue = 0;
        
        while(currentValue<boxSizes.get(boxSizes.size()-1)[1]){
            ArrayList<Double> row = new ArrayList<Double>();
            row.add(currentValue);
            boxes.add(row);
            currentValue+=getBoxSize(currentValue);
        }
        Collections.reverse(boxes);
        
        ArrayList<double[]> datasets = new ArrayList<double[]>();
        datasets.add(this.data.opens);
        datasets.add(this.data.highs);
        datasets.add(this.data.lows);
        datasets.add(this.data.closes);
        
        
        
        ArrayList<ArrayList<String>> graph = runPnF(boxes, datasets,reversalBoxes);
        
        table = convertToArray(graph);
        
        writeTable();
        
    }

    
    public ArrayList<ArrayList<String>> runPnF(ArrayList<ArrayList<Double>> boxes, ArrayList<double[]> datasets, int reversalBoxes){
        
        ArrayList<ArrayList<String>> filledBoxes = new ArrayList<ArrayList<String>>();
        for(ArrayList<Double> row:boxes){
            ArrayList<String> stringRow = new ArrayList<String>();
            stringRow.add(row.get(0).toString());
            filledBoxes.add(stringRow);
        }
        
        int currentRow = getRow(datasets.get(0)[0],boxes);
        int currentColumn = 0;
        
        boolean goingUp = false;
        boolean goingDown = false;
        
        for(int i = 0;i<datasets.get(0).length;i++){
            for(double[] column:datasets){
                
                if(!goingUp&&column[i]-getValue(currentRow,boxes)>getBoxSize(column[i])*reversalBoxes){
                    goingUp = true;
                    goingDown = false;
                    currentColumn++;
                }else if(!goingDown&&getValue(currentRow,boxes)-column[i]>getBoxSize(column[i])*reversalBoxes){
                    goingUp = false;
                    goingDown = true;
                    currentColumn++;
                }
                for(ArrayList<String> row:filledBoxes){
                    while(row.size()<currentColumn){
                        row.add("");
                    }
                }
                
                if(goingUp&&column[i]-getValue(currentRow,boxes)>getBoxSize(column[i])){
                    int nextRow = getRow(column[i],boxes);
                    while(nextRow<currentRow){
                        filledBoxes.get(currentRow).add("X");
                        currentRow--;
                    }
                }else if(goingDown&&getValue(currentRow,boxes)-column[i]>getBoxSize(column[i])){
                    int nextRow = getRow(column[i],boxes);
                    while(nextRow>currentRow){
                        filledBoxes.get(currentRow).add("O");
                        currentRow++;
                    }
                }
                
                
            }
        }
        
        return filledBoxes;
        
    }
    

    
    public int getRow(double value,ArrayList<ArrayList<Double>> boxes){

        int index = 0;
        for(ArrayList<Double> row:boxes){
            if(value<row.get(0)){
                index = boxes.indexOf(row);            
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
    
    public double getValue(int row,ArrayList<ArrayList<Double>> boxes){
        return boxes.get(row).get(0);
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