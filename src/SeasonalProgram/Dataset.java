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
        dates = new Date[lastRow-rowOffset];
        opens = new double[lastRow-rowOffset];
        highs = new double[lastRow-rowOffset];
        lows = new double[lastRow-rowOffset];
        closes = new double[lastRow-rowOffset];
        volumes = new double[lastRow-rowOffset];
        
        //Under the dataset name list by two rows starts the data
        for(int i = nameListPos+rowOffset;i<lastRow;i++){
            dates[i-nameListPos-rowOffset]=new SimpleDateFormat("yyyy/MM/dd").parse(data[i][startColumn]);
            opens[i-nameListPos-rowOffset]=Double.parseDouble(data[i][startColumn+1]);
            highs[i-nameListPos-rowOffset]=Double.parseDouble(data[i][startColumn+2]);
            lows[i-nameListPos-rowOffset]=Double.parseDouble(data[i][startColumn+3]);
            closes[i-nameListPos-rowOffset]=Double.parseDouble(data[i][startColumn+4]);
            volumes[i-nameListPos-rowOffset] = Double.parseDouble(data[i][startColumn+5]);
        }

    }
    
    
}
