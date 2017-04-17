/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author jordandearsley
 */
public class SeasonalPortfolio extends Portfolio {
    public SeasonalPortfolio(String s,Data data, String dataName) throws IOException{
        super(s,data);
        datasets.add(data.getDataset(dataName));
        portfolioTable = calculateTable(portfolioTable);
    }
    public ArrayList<String[]> calculateTable(ArrayList<String[]> emptyTable){
        //CONTINUE HERE
    }
}
