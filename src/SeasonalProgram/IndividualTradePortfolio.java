/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jordandearsley
 */
public class IndividualTradePortfolio extends Portfolio{

    public Core core;
    public Sector sector;
    public ArrayList<DateSet> dates;
    
    public double boxSizePercent;
    public int reversalBoxes;
    public int signalBoxes;
    
    public HashMap<DateSet,ArrayList<IndividualYearRow>> map;
    
    
    public IndividualTradePortfolio(IndividualModel model) {
        super(model);

        this.core = model.core;
        this.sector = model.sector;
        this.dates = model.dates;
        this.boxSizePercent = model.boxSizePercent;
        this.reversalBoxes = model.reversalBoxes;
        this.signalBoxes = model.signalBoxes;
        
        map = new HashMap<DateSet,ArrayList<IndividualYearRow>>();
    }

    public void runPortfolio(){
        Calendar c = calendar.getInstance();
        for(DateSet d:dates){

            c.setTime(startDate);
            map.put(d, new ArrayList<IndividualYearRow>());
            
            while(c.getTime().before(endDate)){


                convertDateSet(c, d);
                double benchGains = calcGains((Security)core,d);
                double sectorGains = calcGains((Security)sector,d);
                
                DateSet PnFDates = null;
                try {
                    PnFDates = getPnFDates((Security)sector,d);
                } catch (Exception ex) {
                    Logger.getLogger(IndividualTradePortfolio.class.getName()).log(Level.SEVERE, null, ex);
                } 
                
                double pnfGains = 0.0;
                if(PnFDates!=null){
                    pnfGains = calcGains((Security)core,PnFDates);
                }
                
                IndividualYearRow row = new IndividualYearRow(benchGains,sectorGains,pnfGains,PnFDates.startDate, PnFDates.endDate, c.getTime());
                map.get(d).add(row);
                
                c.add(Calendar.YEAR, 1);


            }  
        }
    }
    
    public void convertDateSet(Calendar c,DateSet d){
        Calendar start = Calendar.getInstance();
        start.setTime(d.startDate);
        
        Calendar end = Calendar.getInstance();
        end.setTime(d.endDate);
        
        
        if(compareDates(d.endDate,d.startDate)){
            end.set(Calendar.YEAR,c.get(Calendar.YEAR));
            d.endDate=end.getTime();
            
            start.set(Calendar.YEAR,c.get(Calendar.YEAR));
            d.startDate=start.getTime();
        }else{
            end.set(Calendar.YEAR,c.get(Calendar.YEAR));
            d.endDate=end.getTime();
            
            start.set(Calendar.YEAR,c.get(Calendar.YEAR)-1);
            d.startDate=start.getTime();
            
        }
    }
    
    public double calcGains(Security s,DateSet d){
        double startValue = getValue(d.startDate,s);
        double endValue = getValue(d.endDate,s);
        
        if(d.longShort.equals("Long")){
            return (endValue-startValue)/startValue;
        }else if(d.longShort.equals("Short")){
            return (startValue-endValue)/startValue;
        }else{
            return (endValue-startValue)/startValue;
        }
    }

    public DateSet getPnFDates(Security security, DateSet d) throws IOException, CloneNotSupportedException, ParseException {
        DateSet newDates = new DateSet(new Date(d.startDate.getTime()),new Date(d.endDate.getTime()),d.longShort);
        
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(d.startDate);
        startTime.add(Calendar.MONTH, -1);
        
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(d.endDate);
        endTime.add(Calendar.WEEK_OF_YEAR, -2);
        
        
        PointAndFigure pf = new PointAndFigure("PnF",security.dataset,startDate,startTime.getTime(),boxSizePercent,reversalBoxes,signalBoxes);
        
        if(d.longShort.equals("Long")){
            while(startTime.before(endTime)&&!pf.buySignal()){
                pf = new PointAndFigure("PnF",security.dataset,startDate,startTime.getTime(),boxSizePercent,reversalBoxes,signalBoxes);
                
                startTime.add(Calendar.DATE,1);
            }
        }else if(d.longShort.equals("Short")){
            while(startTime.before(endTime)&&!pf.sellSignal()){
                pf = new PointAndFigure("PnF",security.dataset,startDate,startTime.getTime(),boxSizePercent,reversalBoxes,signalBoxes);

                startTime.add(Calendar.DATE,1);
            }
        }
        
        //redefine for sell date
        endTime = Calendar.getInstance();
        endTime.setTime(d.endDate);
        endTime.add(Calendar.MONTH, 1);
        
        if(startTime.before(endTime)){
            newDates.startDate = new Date(startTime.getTime().getTime());
          
            if(d.longShort.equals("Long")){
                while(startTime.before(endTime)&&!pf.sellSignal()){
                    pf = new PointAndFigure("PnF",security.dataset,this.startDate,startTime.getTime(),boxSizePercent,reversalBoxes,signalBoxes);

                    startTime.add(Calendar.DATE,1);
                }
            }else if(d.longShort.equals("Short")){
                while(startTime.before(endTime)&&!pf.buySignal()){
                    pf = new PointAndFigure("PnF",security.dataset,this.startDate,startTime.getTime(),boxSizePercent,reversalBoxes,signalBoxes);

                    startTime.add(Calendar.DATE,1);
                }
            }
            //startTime is now the late sell date
            newDates.endDate = new Date(startTime.getTime().getTime());
            
        //if never registers Buy
        }else{
            newDates = null;
        }
        
        return newDates;
        
    }
    

}

