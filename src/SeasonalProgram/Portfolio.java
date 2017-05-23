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
public class Portfolio {
    
    SimpleDateFormat sm = new SimpleDateFormat("yyyy/MM/dd");
    
    //for constructor
    public Date startDate;
    public Date endDate;
    public Calendar calendar;
    public Security cash;
    public ArrayList<Security> securities;
    public double portfolioValue;
    
    public ArrayList<TradingDay> days = new ArrayList<TradingDay>();
    public ArrayList<Trade> trades = new ArrayList<Trade>();
    public Map<Security, Double[]> holdings = new HashMap<Security, Double[]>();
    
    //public boolean weekend = false;
    
    public Portfolio(ArrayList<Security> securities, Date startDate, Date endDate){
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
        setInitDates(calendar.getTime());
        
        //START HERE: IF WEEKEND PRINT NEXT TRADING DAY LABEL, THE REST SHOULD WORK JUST LOG
        while(calendar.getTime().before(endDate)){
            /*if(isWeekend(calendar)){
                calendar.add(Calendar.DATE, 1);
                updatePortfolio(calendar.getTime());
                
                //if end of month falls on weekend
                if(calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){ 
                    printUpdate(calendar.getTime());
                }
                weekend = true;
                continue;
            }*/
            
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
                        trades.add(new Trade(calendar.getTime()));
                    }
                }
            }
            if(calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){ 
                printUpdate(calendar.getTime());
            }
            
            days.add(new TradingDay(calendar.getTime(),new HashMap<Security, Double[]>(holdings),portfolioValue));
            calendar.add(Calendar.DATE, 1);
            
            
        }   
    }
    
    public void printHoldings(){
        for(Security s:holdings.keySet()){
            double growth = (holdings.get(s)[1]-holdings.get(s)[2])/holdings.get(s)[2];
            growth = round(growth*100);
            System.out.println(s.name+" (Bought "+getBoughtDate(s)+" "+round(holdings.get(s)[2])+"): Price "+round(holdings.get(s)[1])+", "+growth+"% growth, "+round(holdings.get(s)[0])+" value, "+round(100*holdings.get(s)[0]/portfolioValue)+"% of Portfolio");
        }
    }
    
    public String getBoughtDate(Security s){
        for(int i = 0;i<trades.size();i++){
            if(trades.get(trades.size()-1-i).to==s){
                return sm.format(trades.get(trades.size()-1-i).date);
            }
        }
        return null;
    }
    
    public void printUpdate(Date d){
        System.out.println("");
        System.out.println(sm.format(d));
        printHoldings();
        System.out.println("Portfolio Value "+round(portfolioValue));
    }
    
    public void printPreTransaction(){
        System.out.println("");
        System.out.println(sm.format(convertToTrading(calendar.getTime())));
        System.out.println("Initial Portfolio");
        printHoldings();
        System.out.println("Transactions");
    }
    

    //UPDATE ALLOCATION % BASED ON TOTAL VALUE
    public void updatePortfolio(Date timeNow){
        TradingDay yesterday = days.get(days.size()-1);
        
        for(Security s:holdings.keySet()){
            double nowValue = getValue(timeNow,s);
            double previousValue = getValue(yesterday.d,s);
            
            double dailyGrowth = (nowValue-previousValue)/previousValue;
            Double[] newStats = {yesterday.holdings.get(s)[0]*(1+dailyGrowth),nowValue,holdings.get(s)[2]};
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
        
        double newSecurityValue = getValue(calendar.getTime(),s);
        Double[] newSecurityStats = {realAllocation, newSecurityValue,newSecurityValue};
        holdings.put(s, newSecurityStats);
        trades.add(new Trade(calendar.getTime(),core,s));
        
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
        trades.add(new Trade(calendar.getTime(),s,core));
        
        for(int i = 0;i<trades.size();i++){
            if(trades.get(trades.size()-1).from==trades.get(trades.size()-1-i).to){
                trades.get(trades.size()-1-i).setSellTrade(trades.get(trades.size()-1));
                break;
            }
        }
        
        System.out.println("Sell "+s.name+" "+round(holdings.get(s)[0])+", new value "+round(0));
        double buyValue = getValue(calendar.getTime(),core);
        System.out.println("Buy "+core.name+" "+round(holdings.get(s)[0])+", price "+round(buyValue)+", new value "+round(holdings.get(core)[0]));
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
                trades.add(new Trade(calendar.getTime(),source,s));
                
                System.out.println("Buy "+s.name+" "+round(allocation)+", price "+round(currentValue)+", new value "+round(holdings.get(s)[0]));
                break;
            }
        }
        
        
        setDate(calendar,source);
        System.out.println("End Portfolio");
        printHoldings();
        System.out.println("Portfolio Value "+round(portfolioValue));
    }
    
    
    public void setInitDates(Date dateNow){
        Calendar c = Calendar.getInstance();
        c.setTime(dateNow);
        //System.out.println(dateNow);
        for(Security s:securities){
            //Draw it out
            if(compareDates(s.sellDate, s.buyDate)){
                if(compareDates(s.buyDate, c.getTime())){
                    s.buyDate.setYear(dateNow.getYear());
                    s.sellDate.setYear(dateNow.getYear());
                }else if(compareDates(c.getTime(),s.sellDate)){
                    s.buyDate.setYear(dateNow.getYear()+1);
                    s.sellDate.setYear(dateNow.getYear()+1);
                }else{
                    s.buyDate.setYear(dateNow.getYear());
                    s.sellDate.setYear(dateNow.getYear());
                }
            }else{
                if(compareDates(s.sellDate, c.getTime())){
                    s.buyDate.setYear(dateNow.getYear()-1);
                    s.sellDate.setYear(dateNow.getYear());
                }else if(compareDates(c.getTime(),s.buyDate)){
                    s.buyDate.setYear(dateNow.getYear());
                    s.sellDate.setYear(dateNow.getYear()+1);
                }else{
                    s.buyDate.setYear(dateNow.getYear());
                    s.sellDate.setYear(dateNow.getYear()+1);
                }
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
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                if(isWeekend(c)){ 
                    return SeasonalProgram.data.getDataset(s.name).closes[i-1];
                }else{
                    return SeasonalProgram.data.getDataset(s.name).closes[i];
                }
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
    
    //START OUTPUT STUFF
    public Map<Date, Double> getReturns(String settings){
        Map<Date,Double> data = new HashMap<Date,Double>();
        for(TradingDay day:days){
            Date date = day.d;
            TradingDay previousDay;
            try{
                previousDay = days.get(days.indexOf(day)-1);
            }catch(Exception e){
                previousDay = day;
            }
            
            double portfolioGrowth = (day.portfolioValue-previousDay.portfolioValue)/previousDay.portfolioValue;
            
            Security previousCore = new Security();
            Security core = new Security();
            Security cash = new Security();
            
            for(Security s:previousDay.holdings.keySet()){
                if(s instanceof Core){
                    previousCore = s;
                    break;
                }
            }
            for(Security s:day.holdings.keySet()){
                if(s instanceof Core){
                    core = s;
                    break;
                }
            }
            double coreGrowth = (getBenchmarkValue(day.d)-getBenchmarkValue(previousDay.d))/getBenchmarkValue(previousDay.d);
            for(Security s:day.holdings.keySet()){
                if(s instanceof Core&&s.name.equals("Cash")){
                    cash = s;
                    break;
                }
            }
            
            if(settings.equals("Full")){
                data.put(date,portfolioGrowth);
                
            }else if(settings.equals("Benchmark")){
                data.put(date,coreGrowth);
            
            }else if(settings.equals("Relative Benchmark")){
                data.put(date,portfolioGrowth-coreGrowth);
                
            }else if(settings.equals("Cash")){
                try{
                    data.put(date,day.holdings.get(cash)[0]);
                }catch(Exception e){
                    data.put(date,0.0);
                }
            }
            
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
        for(Trade t:trades){
            c.setTime(t.date);
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
        return trades;
    }
    
    public double getBenchmarkValue(Date d){
        for(Security s:securities){
            if(s.name.equals(SeasonalProgram.seasonalModel.core.name)){
                return getValue(d,s);
            }
        }
        return 0;
    }
    
    public Date convertToTrading(Calendar c){
        Calendar newCal = Calendar.getInstance();
        newCal.setTime(c.getTime());
        while(isWeekend(newCal)){
            newCal.add(Calendar.DATE, 1);
        }
        return newCal.getTime();
    }
    public Date convertToTrading(Date d){
        Calendar newCal = Calendar.getInstance();
        newCal.setTime(d);
        while(isWeekend(newCal)){
            newCal.add(Calendar.DATE, 1);
        }
        return newCal.getTime();
    }
    
    
    
}
