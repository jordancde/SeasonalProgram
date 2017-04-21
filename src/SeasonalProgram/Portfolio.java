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
import java.util.Map;

/**
 *
 * @author jordandearsley
 */
public class Portfolio {
    
    public Map<Security, Double> holdings = new HashMap<Security, Double>();
    public ArrayList<Security> securities;
    public Map<Date, Map<Security, Double>> historicalPortfolio = new HashMap<Date, Map<Security, Double>>();
    public ArrayList<Trade> trades;
    
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
        
        //creates cash at 100% and adds to holdings
        cash = new Security("Cash",new Date(Long.MIN_VALUE),new Date(Long.MAX_VALUE),100);
        securities.add(0,cash);
        holdings.put(cash, 100.000);
    }
    
    public void runPortfolio(){
        while(calendar.getTime().before(endDate)){
            
            Security base = new Security();
            //Determine the bank
            for(Security s:holdings.keySet()){
                if(s instanceof Core||s.name.equals("Cash")){
                    base = s;
                    break;
                }
            }
            //MONTHSHSHSHSHSS DO THIS Compare WITHOUT YEAR
            for(Security s:securities){
                //Security not held
                if(!holdings.containsKey(s)){
                   if(calendar.getTime().after(s.buyDate)){
                       updatePortfolio(new Trade(calendar.getTime(),base,s,s.allocation));
                   }
                    
                //Security held
                }else{
                   if(calendar.getTime().after(s.sellDate)){
                       updatePortfolio(new Trade(calendar.getTime(),s,base,s.allocation));
                   }
                }
            }
            
            savePortfolio(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
    }
    
    public void savePortfolio(Date date){
        historicalPortfolio.put(date, holdings);
    }
    
    public void updatePortfolio(Trade trade){
        //subtract from base
        holdings.put(trade.from,holdings.get(trade.from)-trade.percentage);
        //remove base in the case of bank shift
        if(holdings.get(trade.from)<=0){
            holdings.remove(trade.from);
        }
        
        if(!holdings.containsKey(trade.to)){
            holdings.put(trade.to,trade.percentage);
        }else{
            holdings.put(trade.to,holdings.get(trade.to)+trade.percentage);
        }
        if(allocationOver()){
            System.out.println("Allocation over 100%, Error");
        }
        
        trades.add(trade);
    }
        
    public boolean allocationOver(){
        double sum = 0;
        for(Double d:holdings.values()){
            sum+=d;
        }
        if(sum>100){
            return true;
        }
        return false;
    }

}
    
    
    
    

    
