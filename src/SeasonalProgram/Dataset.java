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
        int rows = data.length;
        int columns = data[nameListPos].length;
        
        //finds the position of the header #setNum
        int startColumn = -1;
        int count = 0;
        
        for(int i = 0; i<columns;i++){
            if(nameHeader[i]!=""){
                if (count == setNum){
                    startColumn = i;
                    break;
                }else{
                    count++;
                }
            }
        }
        //Under the dataset name list by two rows starts the data
        
        for(int i = nameListPos+2;i<rows;i++){
            
            //CANT GET IT TO PARSE DATE - CONTINUE
            System.out.println(data[i][startColumn]+""+i);
            
            String DATE_FORMAT = "yyyy/MM/dd";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            dates[i]= sdf.parse(data[i][startColumn]);
            
            opens[i]=Double.parseDouble(data[i][startColumn+1]);
            highs[i]=Double.parseDouble(data[i][startColumn+2]);
            lows[i]=Double.parseDouble(data[i][startColumn+3]);
            closes[i]=Double.parseDouble(data[i][startColumn+4]);
            volumes[i] = Double.parseDouble(data[i][startColumn+5]);
        }
        
        
        
        
        
    }
}
