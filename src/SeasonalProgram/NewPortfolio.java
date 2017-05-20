/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.text.DateFormat;
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
public class NewPortfolio {
    
    SimpleDateFormat sm = new SimpleDateFormat("yyyy/MM/dd");
    
    //for constructor
    public Date startDate;
    public Date endDate;
    public Calendar calendar;
    public Security cash;
    public ArrayList<Security> securities;
    public double portfolioValue;
    
    public ArrayList<TradingDay> days = new ArrayList<TradingDay>();
    
    public Map<Security, Double[]> holdings = new HashMap<Security, Double[]>();
    
    public NewPortfolio(ArrayList<Security> securities, Date startDate, Date endDate){
        this.securities = securities;
        this.startDate = startDate;
        this.endDate = endDate;
        calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        portfolioValue = 100;
        
        
        //creates cash at 100% with value of 1 and adds to holdings
        //Uses buy and sell dates of Core position, swapped for Cash
        cash = new Core("Cash",getCore(securities).sellDate,getCore(securities).buyDate,100);
        securities.add(0,cash);
        
        Double[] cashStats = new Double[3];
        //percent of portfolio
        cashStats[0] = 100.00;
        //value
        cashStats[1] = 1.0;
        //buyValue
        cashStats[2] = 1.0;
        holdings.put(cash, cashStats);
        
        runPortfolio();
        
        
    }
    
    public void runPortfolio(){
        days.add(new TradingDay(calendar.getTime(),holdings,portfolioValue));
        setDates(calendar.getTime());
        while(calendar.getTime().before(endDate)){
            if(isWeekend(calendar)){
                calendar.add(Calendar.DATE, 1);
                continue;
            }
            
            updatePortfolio(calendar.getTime());
            
            for(Security s: securities){
                if(!holdings.containsKey(s)){
                    if(calendar.getTime().after(s.buyDate)||calendar.getTime().equals(s.buyDate)){
                        buy(s,s.allocation);
                        setDate(calendar,s);
                    }
                }else{
                    if(calendar.getTime().after(s.sellDate)||calendar.getTime().equals(s.sellDate)){
                        sell(s);
                        setDate(calendar,s);
                    }
                }
            }
            
            
            days.add(new TradingDay(calendar.getTime(),holdings,portfolioValue));
            calendar.add(Calendar.DATE, 1);
            if(calendar.get(Calendar.DATE) == calendar.getActualMaximum(Calendar.DATE)){
                printUpdate(calendar.getTime());
            }
        }   
    }
    
    public void printHoldings(){
        for(Security s:holdings.keySet()){
            double growth = (holdings.get(s)[1]-holdings.get(s)[2])/holdings.get(s)[2];
            growth = round(growth*100);
            System.out.println(s.name+" (Bought "+round(holdings.get(s)[2])+"): Price "+round(holdings.get(s)[1])+", "+growth+"% growth, "+round(holdings.get(s)[0])+" value, "+round(100*holdings.get(s)[0]/portfolioValue)+"% of Portfolio");
        }
    }
    
    public void printUpdate(Date d){
        System.out.println("");
        System.out.println(sm.format(d));
        printHoldings();
        System.out.println("Portfolio Value "+round(portfolioValue));
    }
    
    public void printPreTransaction(){
        System.out.println("");
        System.out.println(sm.format(calendar.getTime()));
        System.out.println("Initial Portfolio");
        printHoldings();
        System.out.println("Transactions");
    }
    

    //UPDATE ALLOCATION % BASED ON TOTAL VALUE
    public void updatePortfolio(Date timeNow){
        TradingDay yesterday = days.get(days.size()-1);
        
        for(Security s:holdings.keySet()){
            double dailyGrowth = (getValue(timeNow,s)-getValue(yesterday.d,s))/getValue(yesterday.d,s);
            Double[] newStats = {holdings.get(s)[0]*(1+dailyGrowth),getValue(timeNow,s),holdings.get(s)[2]};
            holdings.put(s, newStats);
        }
        double sum = 0;
        for(Security s:holdings.keySet()){
            sum+=holdings.get(s)[0];
        }
        
        portfolioValue = sum;
    }
    
    
    
    
    public void buy(Security s, double allocationPercent){
        
        //prevents double call of core swap
        if(s instanceof Core){
            return;
        } 
        printPreTransaction();
        double realAllocation = portfolioValue*allocationPercent/100;
        Security core = getHoldingsCore();
        Double[] currentCoreStats = holdings.get(core);
        currentCoreStats[0] -= realAllocation;
        holdings.put(core,currentCoreStats);
        
        Double[] newSecurityStats = {realAllocation, getValue(calendar.getTime(),s),getValue(calendar.getTime(),s)};
        holdings.put(s, newSecurityStats);
        
        System.out.println("Sell "+core.name+" "+round(holdings.get(s)[0])+" ("+allocationPercent+"%), new value "+round(holdings.get(core)[0]));
        System.out.println("Buy "+s.name+" "+round(holdings.get(s)[0])+" ("+allocationPercent+"%), price "+round(holdings.get(s)[1])+", new value "+round(holdings.get(s)[0]));
        
        System.out.println("End Portfolio");
        printHoldings();
        System.out.println("Portfolio Value "+round(portfolioValue));
    }
    
    
    public void sell(Security s){
        if(s instanceof Core){
            swapCores(s);
            return;
        }
        printPreTransaction();
        Security core = getHoldingsCore();
        Double[] currentCoreStats = holdings.get(core);
        currentCoreStats[0] += holdings.get(s)[0];
        holdings.put(core,currentCoreStats);
        
        System.out.println("Sell "+s.name+" "+round(holdings.get(s)[0])+", new value "+round(holdings.get(s)[0]));
        System.out.println("Buy "+core.name+" "+round(holdings.get(s)[0])+", price "+round(getValue(calendar.getTime(),core))+", new value "+round(holdings.get(core)[0]));
        holdings.remove(s);
        System.out.println("End Portfolio");
        printHoldings();
        System.out.println("Portfolio Value "+round(portfolioValue));
    }
    
  
    
    public void swapCores(Security source){
        printPreTransaction();
        double allocation = holdings.get(source)[0];
        System.out.println("Sell "+source.name+" "+round(allocation)+" ("+round(100*allocation/portfolioValue)+"%)");

        holdings.remove(source);
        for(Security s:securities){
            if(s instanceof Core && !(s.equals(source))){
                double currentValue = getValue(calendar.getTime(),s);
                Double[] newCoreStats = {allocation,currentValue,currentValue};
                holdings.put(s,newCoreStats);
                System.out.println("Buy "+s.name+" "+round(allocation)+", price "+round(getValue(calendar.getTime(),s))+", new value "+round(holdings.get(s)[0]));

                
                break;
            }
        }
        setDate(calendar,source);
        System.out.println("End Portfolio");
        printHoldings();
        System.out.println("Portfolio Value "+round(portfolioValue));
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
    
    public void setDate(Calendar c, Security s){
        if(compareDates(c.getTime(),s.buyDate)){
                s.buyDate.setYear(c.getTime().getYear()+1);
                
            }else{
                s.buyDate.setYear(c.getTime().getYear());
            }
            if(compareDates(c.getTime(),s.sellDate)){
                s.sellDate.setYear(c.getTime().getYear()+1);
            }else{
                s.sellDate.setYear(c.getTime().getYear());
            }
    }
    
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
    
    public Security getCore(ArrayList<Security> securities){
        for(Security s:securities){
            if(s instanceof Core){
                return s;
                
            }
            
        }
        return null;
    }
    
    public Security getHoldingsCore(){
        for(Security s:holdings.keySet()){
            if(s instanceof Core){
                return s;  
            }
        }
        return null;
    }
    
    public double getValue(Date d,Security s){
        if(s.name.equals("Cash")){
            return 1;
        }
        Date[] dates = SeasonalProgram.data.getDataset(s.name).dates;
        for(int i = 0;i<dates.length;i++){
            if(dates[i].after(d)||dates[i].equals(d)){
                //-1 for previous day close
                return SeasonalProgram.data.getDataset(s.name).closes[i-1];
            }
        }
        return 0;
    }
    public boolean isWeekend(Calendar c){
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY) { // If it's Friday so skip to Monday
            return true;
        } else if (dayOfWeek == Calendar.SATURDAY) { // If it's Saturday skip to Monday
            return true;
        } else {
            return false;
        }
    }
    
    public double round(double d){
        double factor = 1e4; // = 1 * 10^5 = 100000.
        return Math.round(d * factor) / factor;
    }
    
}
