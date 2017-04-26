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
    public double fromValue;
    public double toValue;
    public Trade(Date date, Security from,double fromValue,Security to, double toValue,double percentage){
        this.date = date;
        this.from = from;
        this.to = to;
        this.percentage = percentage;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }
}
