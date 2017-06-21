/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.util.Date;

/**
 *
 * @author jordandearsley
 */
public class Security {
    public String name;
    public Date buyDate;
    public Date sellDate;
    public NewDataset dataset;
    public double allocation;
    
    public Security(String name, Date buyDate, Date sellDate, double allocation){
        this.name = name;
        this.buyDate = buyDate;
        this.sellDate = sellDate;
        this.allocation = allocation;
        dataset = SeasonalProgram.data.getDataset(name);
        //dataset.trimData(buyDate, sellDate);
    }
    public Security(String name,  double allocation){
        this.name = name;
        this.allocation = allocation;
        dataset = SeasonalProgram.data.getDataset(name);
        //dataset.trimData(buyDate, sellDate);
    }
    public Security(){}
    
    public Security(String name){
        this.name = name;

        dataset = SeasonalProgram.data.getDataset(name);
        //dataset.trimData(buyDate, sellDate);
    }
    public Security(String name, Date buyDate, Date sellDate){
        this.name = name;
        this.buyDate = buyDate;
        this.sellDate = sellDate;
        
        dataset = SeasonalProgram.data.getDataset(name);
        //dataset.trimData(buyDate, sellDate);
    }
    
}
