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
public class IndividualYearRow {
    public double benchmarkGains;
    public double sectorGains;
    public Date date;
    public double PnF;
    public Date PnFEntry;
    public Date PnFExit;
    
    public IndividualYearRow(double benchmarkGains,double sectorGains,double PnF
    ,Date PnFEntry,Date PnFExit, Date date){
        this.benchmarkGains = benchmarkGains;
        this.sectorGains = sectorGains;
        this.PnF = PnF;
        this.PnFEntry = PnFEntry;
        this.PnFExit = PnFExit;
        this.date = date;
    }
}
