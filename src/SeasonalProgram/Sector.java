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
public class Sector extends Security{
    public String type;
    public String sellType;
    public Sector(String name, String type, String sellType, double allocation){
        super(name, allocation);
        this.type = type;
        this.sellType = sellType;
    }
    public Sector(String name, Date buyDate, Date sellDate, double allocation, double leverage){
        super(name, buyDate, sellDate, allocation);

    }
    
    public Sector(String name, Date buyDate, Date sellDate){
        super(name, buyDate, sellDate);

    }
    public Sector(String name){
        super(name);

    }
}
