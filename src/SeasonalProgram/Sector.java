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
class Sector {
    public String name;
    public Date startDate;
    public Date stopDate;
    public double leverage;
    public Sector(String name, Date startDate, Date stopDate, double leverage){
        this.name = name;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.leverage = leverage;
        
    }
}
