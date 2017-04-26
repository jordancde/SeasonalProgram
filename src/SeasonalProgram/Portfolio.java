/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jordandearsley
 */
public class Portfolio {
    
    public Map<Security, Double[]> holdings = new HashMap<Security, Double[]>();
    public ArrayList<Security> securities;
    public Map<Date, Map<Security, Double[]>> historicalPortfolio = new HashMap<Date, Map<Security, Double[]>>();
    public Map<Date,Trade> trades = new HashMap<Date,Trade>();
    
    public Date startDate;
    public Date endDate;
    
    public Calendar calendar;
    
    public Security cash;
        
    
    public Portfolio(ArrayList<Security> securities, Date startDate, Date endDate){
        this.securities = securities;
        this.startDate = startDate;
        this.endDate = endDate;
        calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        
        
        //creates cash at 100% with value of 1 and adds to holdings
        cash = new Core("Cash",getCore(securities).sellDate,getCore(securities).buyDate,100);
        securities.add(0,cash);
        Double[] cashStats = new Double[2];
        cashStats[0] = 100.00;
        cashStats[1] = 1.0;
        holdings.put(cash, cashStats);
    }
    
    public void runPortfolio(){
        while(calendar.getTime().before(endDate)){
            setDates(calendar.getTime());
            Security base = new Security();
            //Determine the bank
            for(Security s:holdings.keySet()){
                if(s instanceof Core){
                    base = s;
                    break;
                }
            }
            
            for(Security s:securities){
                //Security not held
                if(s instanceof Sector){
                    if(!holdings.containsKey(s)){
                       
                       if(calendar.getTime().after(s.buyDate)||calendar.getTime().equals(s.buyDate)){
                           updatePortfolio(new Trade(calendar.getTime(),base,getValue(calendar.getTime(),base),s,getValue(calendar.getTime(),s),s.allocation));
                       }

                    //Security held
                    }else{
                       if(calendar.getTime().after(s.sellDate)||calendar.getTime().equals(s.sellDate)){
                           updatePortfolio(new Trade(calendar.getTime(),s,getValue(calendar.getTime(),s),base,getValue(calendar.getTime(),base),s.allocation));
                       }
                    }
                    
                }else if(s instanceof Core){
                    ///only buy, don't sell, they have swapped buy/sell dates so only buys take care of both
                    if(!holdings.containsKey(s)){
                        if(calendar.getTime().after(s.buyDate)||calendar.getTime().equals(s.buyDate)){
                            updatePortfolio(new Trade(calendar.getTime(),base,getValue(calendar.getTime(),base),s,getValue(calendar.getTime(),s),holdings.get(base)[0]));
                            //update the base
                            base = s;
                        }
                        
                    }
                }
                
            }
            updateHoldingValues(calendar.getTime());
            savePortfolio(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
    }
    
    public void setDates(Date dateNow){
        Calendar c = Calendar.getInstance();
        c.setTime(dateNow);
        //System.out.println(dateNow);
        for(Security s:securities){
            if(compareDates(c.getTime(),s.buyDate)){
                s.buyDate.setYear(dateNow.getYear()+1);
            }else{
                s.buyDate.setYear(dateNow.getYear());
            }
            if(compareDates(c.getTime(),s.sellDate)){
                s.sellDate.setYear(dateNow.getYear()+1);
            }else{
                s.sellDate.setYear(dateNow.getYear());
            }
            
            //System.out.println(s.name+" | "+s.buyDate+" | "+s.sellDate);
        }
        //System.out.println("");
    }
    
    //returns true if date 1 >= date 2
    public boolean compareDates(Date date1,Date date2){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        
        if(cal1.get(Calendar.MONTH)>cal2.get(Calendar.MONTH)){
            return true;
        }else if(cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH)){
            if(cal1.get(Calendar.DAY_OF_MONTH)>cal2.get(Calendar.DAY_OF_MONTH)){
                return true;
            }
        }
        return false;
        
    }
    
    public void savePortfolio(Date date){
        //System.out.println(date);
        //printHoldings(holdings);
        historicalPortfolio.put(date, new HashMap<Security,Double[]>(holdings));
        
    }
    
    public Security getCore(ArrayList<Security> securities){
        for(Security s:securities){
            if(s instanceof Core){
                return s;
                
            }
            
        }
        return null;
    }
    
    public void updatePortfolio(Trade trade){
        //subtract from base
        Double[] holdingFromStats = {holdings.get(trade.from)[0]-trade.percentage,getValue(trade.date,trade.from)};
        holdings.put(trade.from,holdingFromStats);
        //remove base in the case of bank shift
        if(holdings.get(trade.from)[0]<=0){
            holdings.remove(trade.from);
        }
        
        if(!holdings.containsKey(trade.to)){
            Double[] holdingToStats = {trade.percentage,getValue(trade.date,trade.to)};
            holdings.put(trade.to,holdingToStats);
        }else{
            Double[] holdingToStats = {holdings.get(trade.to)[0]+trade.percentage,getValue(trade.date,trade.to)};
            holdings.put(trade.to,holdingToStats);
        }
        
        System.out.println("");
        System.out.println(trade.date);
        
        System.out.println("Sold "+trade.from.name+", bought "+trade.to.name+" - "+trade.percentage);
        printHoldings(holdings);
        
        if(allocationOver()){
            System.out.println("Allocation over 100%, Error");
        }
        
        trades.put(trade.date,trade);
    }
    
    public void updateHoldingValues(Date timeNow){
        for(Security s:holdings.keySet()){
            Double[] newStats = {holdings.get(s)[0],getValue(timeNow,s)};
            holdings.put(s, newStats);
        }
    }
    
    public double getValue(Date d,Security s){
        if(s.name.equals("Cash")){
            return 1;
        }
        Date[] dates = SeasonalProgram.data.getDataset(s.name).dates;
        for(int i = 0;i<dates.length;i++){
            if(dates[i].after(d)||dates[i].equals(d)){
                return SeasonalProgram.data.getDataset(s.name).closes[i];
            }
        }
        return 0;
    }
    
    public void printHoldings(Map<Security, Double[]> map){
        for(Security s:map.keySet()){
            System.out.println(s.name+": "+map.get(s)[0]+"%, "+map.get(s)[1]);
        }
    }
       
    public boolean allocationOver(){
        double sum = 0;
        for(Double[] d:holdings.values()){
            sum+=d[0];
        }
        if(sum>100){
            return true;
        }
        return false;
    }
    
    public Map<Date, Double> getReturns(String settings){
        Map<Date,Double> data = new HashMap<Date,Double>();
        for(Date d:historicalPortfolio.keySet()){
            double weightedGrowth = 0.00000000000;
            //get previous day
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.DATE, -1);
            
            if(historicalPortfolio.containsKey(c.getTime())){
                
                //get base stats
                double coreGrowth = 0;
                if(settings.equals("Relative Benchmark")){
                    for(Security s:historicalPortfolio.get(d).keySet()){
                        if(s instanceof Core){
                            //get current values
                            double coreValue = historicalPortfolio.get(d).get(s)[1];
                            //get previous values
                            double previousCoreValue = 0;
                            //if the security was held yesterday

                            //printHoldings(historicalPortfolio.get(c.getTime()));
                            if(historicalPortfolio.get(c.getTime()).containsKey(s)){
                                //double previousSecurityPercent = historicalPortfolio.get(c.getTime()).get(s)[0];
                                previousCoreValue = historicalPortfolio.get(c.getTime()).get(s)[1];
                            }else{
                                //use current value for comparison if not held yesterday
                                previousCoreValue = historicalPortfolio.get(d).get(s)[1];
                            }
                            //System.out.println(d+" | "+c.getTime());
                            coreGrowth = (coreValue-previousCoreValue)/previousCoreValue;
                            
                            break;
                        }
                    }
                }
                
                for(Security s: historicalPortfolio.get(d).keySet()){
                    
                    if(settings.equals("Benchmark")&&s instanceof Sector){
                        continue;
                    }
                    if(settings.equals("Cash")&&!s.name.equals("Cash")){
                        continue;
                    }else if(settings.equals("Cash")&&s.name.equals("Cash")){
                        weightedGrowth = historicalPortfolio.get(d).get(s)[0];
                        break;
                    }
                    
                    //get current values
                    double securityPercent = historicalPortfolio.get(d).get(s)[0];
                    double securityValue = historicalPortfolio.get(d).get(s)[1];
                    //get previous values
                    double previousSecurityValue = 0;
                    //if the security was held yesterday
                    
                    //printHoldings(historicalPortfolio.get(c.getTime()));
                    if(historicalPortfolio.get(c.getTime()).containsKey(s)){
                        //double previousSecurityPercent = historicalPortfolio.get(c.getTime()).get(s)[0];
                        previousSecurityValue = historicalPortfolio.get(c.getTime()).get(s)[1];
                    }else{
                        //use current value for comparison if not held yesterday
                        previousSecurityValue = historicalPortfolio.get(d).get(s)[1];
                    }
                    //System.out.println(d+" | "+c.getTime());
                    double growth = (securityValue-previousSecurityValue)/previousSecurityValue;
                    weightedGrowth+=growth*(securityPercent/100);
                    
                }
                //should be 0 if relative to core turned off
                weightedGrowth-=coreGrowth;
            }
            
            data.put(d, weightedGrowth);
            //System.out.println(d+" | "+weightedGrowth);
        }
        return data;
    }
    
    public Map<String, Double> getMonthlyReturns(String preference, boolean useFrequency){
        Map<Date, Double> returns = getReturns(preference);
        Calendar c = Calendar.getInstance();
        Map<String, Double> monthlyReturns = new HashMap<String, Double>();
        Map<String,Integer> monthlyTradingDayCount = new HashMap<String,Integer>();
        for(Date d:returns.keySet()){
            c.setTime(d);
            String monthString = c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);

            //System.out.println(monthString);
            if(monthlyReturns.containsKey(monthString)){
                monthlyReturns.put(monthString, monthlyReturns.get(monthString)+returns.get(d));
                monthlyTradingDayCount.put(monthString,monthlyTradingDayCount.get(monthString)+1);
            }else {
                monthlyReturns.put(monthString, returns.get(d));
                monthlyTradingDayCount.put(monthString,1);
            }
        }
        if(preference.equals("Cash")){
            for(String s: monthlyReturns.keySet()){
                monthlyReturns.put(s,(monthlyReturns.get(s)/monthlyTradingDayCount.get(s))/100);
            }
        }
        if(useFrequency){
            for(String s: monthlyReturns.keySet()){
                if(monthlyReturns.get(s)>0){
                   monthlyReturns.put(s,1.0);
                }else{
                   monthlyReturns.put(s,0.0);
                }
            }
        }
        
        return monthlyReturns;
    }
    
    public Map<String, Double> getMonthlyTrades(){
        Calendar c = Calendar.getInstance();
        Map<String, Double> monthlyTrades = new HashMap<String, Double>();
        for(Date d:trades.keySet()){
            c.setTime(d);
            String monthString = c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);
            if(monthlyTrades.containsKey(monthString)){
                monthlyTrades.put(monthString,monthlyTrades.get(monthString)+1);
            }else {
                monthlyTrades.put(monthString, 1.0);
            }
        }
        return monthlyTrades;
    }
    
    public ArrayList<Trade> getTrades(){
        Calendar c = Calendar.getInstance();
        ArrayList<Trade> tradeOrderedList= new ArrayList<Trade>();
        for(Date d:trades.keySet()){
            c.setTime(d);
            //String monthString = c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);
            if(tradeOrderedList.size()==0){
                tradeOrderedList.add(trades.get(d));
            }else{
                for(int i = 0;i<tradeOrderedList.size();i++){
                    if(d.after(tradeOrderedList.get(i).date)){
                        tradeOrderedList.add(trades.get(d));
                    }
                }
            }
            
        }
        for(Trade t:tradeOrderedList){
            System.out.println(t.date);
            
        }
        return tradeOrderedList;
    }
    
 

}
    