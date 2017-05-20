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
public class Trade {
    public Date date;
    public Security from;
    public Security to;
    public double percentage;

    public Trade(Date date, Security from,Security to, double percentage){
        this.date = date;
        this.from = from;
        this.to = to;
        this.percentage = percentage;

    }
    public Trade(Date date, Security from,Security to){
        this.date = date;
        this.from = from;
        this.to = to;
    }
    public Trade(Date date){
        this.date = date;
    }
}
