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
class Core {
    public String name;
    public Date buyDate;
    public Date sellDate;
    public Dataset dataset;
    public Core(String name, Date buyDate, Date sellDate){
        this.name = name;
        this.buyDate = buyDate;
        this.sellDate = sellDate;
        dataset = SeasonalProgram.data.getDataset(name);
        dataset.trimData(buyDate, sellDate);
    }
    
    
    
}
