/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author jordandearsley
 */
public class PnFPortfolio extends Portfolio{
    
    public boolean inSeasonal = false;
    
    public PnFPortfolio(ArrayList<Security> securities, Date startDate, Date endDate){
        super(securities,startDate,endDate);
        
        
    }
    
    @Override
    public void runPortfolio(){
        setInitDates(calendar.getTime());
        loadInitialHoldings(calendar);
        printUpdate(calendar.getTime());
        days.add(new TradingDay(calendar.getTime(),holdings,portfolioValue));
        
        
        while(calendar.getTime().before(endDate)){

            updatePortfolio(calendar.getTime());
            
            //Modified logic start
            Core SP = (Core)getBenchmark();
            Calendar monthBefore = Calendar.getInstance();
            monthBefore.setTime(SP.buyDate);
            monthBefore.add(Calendar.MONTH, -1);
            Calendar monthAfter = Calendar.getInstance();
            monthBefore.setTime(SP.sellDate);
            monthBefore.add(Calendar.MONTH, 1);
                
            if(!inSeasonal){   
                if(calendar.getTime().after(SP.buyDate)||calendar.getTime().equals(SP.buyDate)){
                    buyRemainingSectors(calendar.getTime());
                    inSeasonal = true;
                //checks for month before buy Date
                }else if(calendar.getTime().after(monthBefore.getTime())||calendar.getTime().equals(monthBefore.getTime())){
                    buyTriggered(calendar.getTime());
                }
            }else{
                if(calendar.getTime().after(monthAfter.getTime())||calendar.getTime().equals(monthAfter.getTime())){
                    sellRemainingSectors();
                    inSeasonal = false;
                }else if(calendar.getTime().after(SP.sellDate)||calendar.getTime().equals(SP.sellDate)){
                    sellTriggered(calendar.getTime());
                }
            }
            
            //Modified logic end
            
            
            if(calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){ 
                printUpdate(calendar.getTime());
            }
            
            days.add(new TradingDay(calendar.getTime(),new HashMap<Security, Double[]>(holdings),portfolioValue));
            calendar.add(Calendar.DATE, 1);
            
            
        } 
    }
    
    //ordered highest to lowest
    public ArrayList<Security> getTopSectors(){
        HashMap<Security, HashMap<Security, Double> > matrix = makeMatrix();
        HashMap<Security, Double> relativePerformances = new HashMap<Security, Double>();
        for(Security s: matrix.keySet()){
            relativePerformances.put(s, sumDifferences(matrix.get(s)));
        }
        
        ArrayList<Security> topSectors = new ArrayList<Security>();
        
        for(Security s: relativePerformances.keySet()){
            int pos=0;
            for(Security t: topSectors){
                if(relativePerformances.get(s)>relativePerformances.get(t)){
                    pos++;
                }
            }
            topSectors.add(pos, s);
        }
        
        return topSectors;
    }
    
    public HashMap<Security, HashMap<Security, Double> > makeMatrix(){
        HashMap<Security, HashMap<Security, Double> > matrix = new HashMap<Security, HashMap<Security, Double> >();
        ArrayList<Security> securities = getPossibleBuys();
        for(Security s:securities){
            HashMap<Security, Double> row = new HashMap<Security, Double>();
            for(Security t:securities){
                if(s.equals(t)){
                    continue;
                }
                row.put(t, compareSectors(s,t));
                
            }
            matrix.put(s, row);
        }
        
        return matrix;
    }
    
    public Security getBenchmark(){
        for(Security s:securities){
            if(s.name.equals(SeasonalProgram.seasonalModel.core.name)){
                return s;
            }
        }
        return null;
    } 

    private Double sumDifferences(HashMap<Security, Double> row) {
        double sum = 0;
        for(Security s: row.keySet()){
            sum+=row.get(s);
        }
        return sum;
    }
    
    
    
    
    //has to buy all sectors not bought, and increase position size of majors up to 90%
    private void buyRemainingSectors(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        
        for(Security s:holdings.keySet()){
            if(s instanceof Sector){
                if(((Sector) s).type.equals("Major")){
                    if(holdings.get(s)[0]/getPortfolioValue(d)<10){
                        doublePosition(s,d);
                        setDate(c,s);
                    }
                }
            }
        }
        
        for(Security s:securities){
            if(s instanceof Sector&&!holdings.containsKey(s)){
                if(((Sector) s).type.equals("Major")){
                    buy(s,10,false);
                }else if(((Sector) s).type.equals("Minor")){
                    buy(s,5,false);
                }
                setDate(c,s);
            }
        }
        
    }
    
    //Has to buy triggered sectors at premature trigger positions
    private void buyTriggered(Date d) {
        ArrayList<Security> topSectors = getTopSectors();
        
        for(Security s:topSectors){
            if(s instanceof Sector){
                double allocation = 0;
                if(((Sector) s).type.equals("Major")){
                    allocation = 10;
                }else if(((Sector) s).type.equals("Minor")){
                    allocation = 5;
                }
                
                if(!overAllocation(d,allocation)){
                    if(sectorBuyTriggered(d, s)){
                        buy(s,allocation,false);
                    }
                }
                
            }
        }
    }
    
    //Has to sell triggered sectors at premature trigger positions
    private void sellTriggered(Date d) {
        for(Security s:holdings.keySet()){
            if(s instanceof Sector){
                
                if(((Sector) s).sellType.equals("Regular")){
                    if(sectorSellTriggered(d, s)){
                        halfPosition(s,d);
                    }
                }
  
            }
        }    
    }
    
    //has to sell all sectors not sold
    private void sellRemainingSectors() {
        for(Security s:holdings.keySet()){
            if(s instanceof Sector){
                sell(s);
            }
        }
    }
    
    //All sectors that pass initial buy criteria
    public ArrayList<Security> getPossibleBuys() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //compare two secotrs based on either PnF or Core performance
    private Double compareSectors(Security s, Security t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    //checks if sector sell is triggered
    private boolean sectorSellTriggered(Date d, Security s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //checks if sector buy is triggered
    private boolean sectorBuyTriggered(Date d, Security s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //doubles the position size
    private void doublePosition(Security s, Date d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //half the position size
    private void halfPosition(Security s, Date d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
