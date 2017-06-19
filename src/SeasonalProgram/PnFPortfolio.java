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
public class PnFPortfolio extends Portfolio{
    
    public boolean inSeasonal;
    public int reversalBoxes;
    public int signalBoxes;
    public double minCoreAllocation;
    public double boxSizePercent;
    
    
    
    public PnFPortfolio(ArrayList<Security> securities, Date startDate, Date endDate,double boxSizePercent,int reversalBoxes, int signalBoxes, double minCoreAllocation ){
        super(securities,startDate,endDate);
        inSeasonal = false;
        this.boxSizePercent = boxSizePercent;
        this.reversalBoxes = reversalBoxes;
        this.signalBoxes = signalBoxes;
        this.minCoreAllocation = minCoreAllocation;
        //cash already added
        
        
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
            monthAfter.setTime(SP.sellDate);
            monthAfter.add(Calendar.MONTH, 1);
            
            Calendar twoWeeksBeforeSell = Calendar.getInstance();
            twoWeeksBeforeSell.setTime(SP.sellDate);
            twoWeeksBeforeSell.add(Calendar.DATE, -14);
                
            
            if(!inSeasonal){   
                if(calendar.getTime().after(SP.buyDate)||calendar.getTime().equals(SP.buyDate)){
                    if(getHoldingsCore().name.equals("Cash")){
                        System.out.println("");
                        System.out.println("Buying Remaining Core");
                        buyRemainingCore(calendar.getTime());
                    }
                    try {
                        System.out.println("");
                        System.out.println("Buying Remaining Sectors");
                        buyRemainingSectors(calendar.getTime());
                    } catch (IOException ex) {
                        Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                    
                    inSeasonal = true;
                    setCoreDates(calendar);
                //checks for month before buy Date
                }else if(calendar.getTime().after(monthBefore.getTime())||calendar.getTime().equals(monthBefore.getTime())){
                    
                    try {
                        //System.out.println("Checking Triggered Sectors");
                        buyTriggered(calendar.getTime());
                    } catch (IOException ex) {
                        Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (CloneNotSupportedException ex) {
                        Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }else{
                if(calendar.getTime().after(monthAfter.getTime())||calendar.getTime().equals(monthAfter.getTime())){
                    
                    if(getHoldingsCore().name.equals("S&P 500")){
                        System.out.println("Switching to Cash");
                        swapCores(getHoldingsCore());
                    }
                    System.out.println("Selling Remaining Sectors");
                    sellRemainingSectors();

                    inSeasonal = false;
                    setCoreDates(calendar);
                }else if(calendar.getTime().after(SP.sellDate)||calendar.getTime().equals(SP.sellDate)){
                    try {
                        
                        sellTriggered(calendar.getTime());
                    } catch (Exception ex){System.out.println(ex);}
                }else if(calendar.getTime().after(twoWeeksBeforeSell.getTime())||calendar.getTime().equals(twoWeeksBeforeSell.getTime())){
                    try {
                        if(sellTriggered(calendar.getTime(),(Security)getHoldingsCore())){
                            System.out.println("Selling Core Early");
                            sellCoreEarly(calendar.getTime());
                        }
                    } catch (Exception ex){System.out.println(ex);}
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
    public ArrayList<Security> getTopSectors() throws ParseException, IOException, CloneNotSupportedException{
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
    
    public HashMap<Security, HashMap<Security, Double> > makeMatrix() throws ParseException, IOException, CloneNotSupportedException{
        HashMap<Security, HashMap<Security, Double> > matrix = new HashMap<Security, HashMap<Security, Double> >();
        ArrayList<Security> securities = getPossibleBuys();
        for(Security s:securities){
            HashMap<Security, Double> row = new HashMap<Security, Double>();
            for(Security t:securities){
                if(s.equals(t)){
                    row.put(t, 0.0);
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
            if(s.name.equals(SeasonalProgram.RSModel.core.name)){
                return s;
            }
        }
        return null;
    } 
    
    public Security getCash(){
        for(Security s:securities){
            if(s.name.equals("Cash")){
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
    private void buyRemainingSectors(Date d) throws IOException, CloneNotSupportedException, ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        
        for(Security s:holdings.keySet()){
            if(s instanceof Sector){
                if(((Sector) s).type.equals("Major")){
                    if(100*holdings.get(s)[0]/getPortfolioValue(d)<10){
                        increasePosition(s,d,10.0,(Core)getBenchmark());
                        //setDate(c,s);
                    }
                }
            }
        }
        
        for(Security s:securities){
            if(s instanceof Sector&&!holdings.containsKey(s)){
                if(buyTriggered(d,s)){
                    if(((Sector) s).type.equals("Major")){
                        buy(s,10,false);
                    }else if(((Sector) s).type.equals("Minor")){
                        buy(s,5,false);
                    }
                    //setDate(c,s);
                }
            }
        }
        
    }
    
    //Has to buy triggered sectors at premature trigger positions
    private void buyTriggered(Date d) throws IOException, ParseException, CloneNotSupportedException {
        ArrayList<Security> topSectors = getTopSectors();
        if(topSectors.size()==0){return;}
        
        for(Security s:topSectors){
            if(s instanceof Sector){
                double allocation = 0;
                if(((Sector) s).type.equals("Major")){
                    allocation = 5;
                }else if(((Sector) s).type.equals("Minor")){
                    allocation = 5;
                }
                if(!holdings.containsKey(s)&&buyTriggered(d,s)){
                    if(!overAllocation(d,allocation)){
                        buy(s,allocation,false);
                    }else{
                        while(overAllocation(d,allocation)){
                            dropLowestSector();
                        }
                        buy(s,allocation,false);
                    }
                }
                
            }
        }
    }
    
    @Override
    public boolean overAllocation(Date d, double toBeAdded){
        double sum = 0;
        
        for(Security s:holdings.keySet()){
            //just changed, might want to check if cash needs min 10% allocation
            if(!(s instanceof Core)){   
                sum+=100*holdings.get(s)[0]/getPortfolioValue(d);  
            }
        }
        sum+=toBeAdded;
        return(100-sum<minCoreAllocation);
    };
    
    //Has to sell triggered sectors at premature trigger positions
    private void sellTriggered(Date d) throws IOException, ParseException, CloneNotSupportedException {
        for(Security s:holdings.keySet()){
            if(s instanceof Sector){
                
                if(((Sector) s).sellType.equals("Regular")){
                    if(sellTriggered(d, s)){
                        decreasePosition(s,d,5.0,(Core)getBenchmark());
                    }
                }
  
            }
        }    
    }
    
    //has to sell all sectors not sold
    private void sellRemainingSectors() {
        ArrayList<Security> toBeSold= new ArrayList<Security>();
        for(Security s:holdings.keySet()){
            if(s instanceof Sector){
                toBeSold.add(s);
                
            }
        }
        for(Security s: toBeSold){
            sell(s);
        }
    }

    //increases the position size
    private void increasePosition(Security s, Date d, double increaseTo, Core c) {
        printPreTransaction(false);
        double currentAllocationPercent=0;
        //in the case that it isn't already bought
        try{
            currentAllocationPercent = 100*holdings.get(s)[0]/portfolioValue;
        }catch(Exception e){System.out.println(e);}
        
        double increase = increaseTo-currentAllocationPercent;
        
        double realIncrease = portfolioValue*increase/100;
        double realAllocation = portfolioValue*increaseTo/100;
        
        Security core = c;
        Double[] currentCoreStats = holdings.get(core);
        currentCoreStats[0] -= realIncrease;
        holdings.put(core,currentCoreStats);
        
        double newSecurityValue = getValue(calendar.getTime(),s);
        Double[] newSecurityStats = {realAllocation, newSecurityValue,newSecurityValue};
        holdings.put(s, newSecurityStats);
        
        
        //trades.add(new Trade(calendar.getTime(),core,s));

        System.out.println("Sell "+core.name+" "+round(realIncrease)+" ("+round(increase)+"%), new value "+round(holdings.get(core)[0]));
        System.out.println("Buy "+s.name+" "+round(realIncrease)+" ("+round(increase)+"%), price "+round(holdings.get(s)[1])+", new value "+round(holdings.get(s)[0]));

        System.out.println("End Portfolio");
        printHoldings();
        System.out.println("Portfolio Value "+round(portfolioValue));
        
    }
    
    //half the position size
    private void decreasePosition(Security s, Date d, double decreaseTo, Core c) {
        printPreTransaction(true);
        
        double currentAllocationPercent = 100*holdings.get(s)[0]/portfolioValue;
        double decrease = currentAllocationPercent-decreaseTo;
        
        double realDecrease = portfolioValue*decrease/100;
        double realAllocation = portfolioValue*decreaseTo/100;
        
        Security core = c;
        Double[] currentCoreStats = holdings.get(core);
        currentCoreStats[0] += realDecrease;
        holdings.put(core,currentCoreStats);
        
        /*trades.add(new Trade(calendar.getTime(),s,core));
        
        for(int i = 0;i<trades.size();i++){
            if(trades.get(trades.size()-1).from==trades.get(trades.size()-1-i).to){
                trades.get(trades.size()-1-i).setSellTrade(trades.get(trades.size()-1));
                break;
            }
        }*/
        
        System.out.println("Sell "+s.name+" "+round(realDecrease)+", new value "+round(realAllocation));
        double buyValue = getValue(calendar.getTime(),core);
        System.out.println("Buy "+core.name+" "+round(realDecrease)+", price "+round(buyValue)+", new value "+round(holdings.get(core)[0]));
        
        Double[] currentSectorStats = holdings.get(s);
        currentSectorStats[0] -= realDecrease;
        holdings.put(s,currentSectorStats);
        
        System.out.println("End Portfolio");
        printHoldings();
        System.out.println("Portfolio Value "+round(portfolioValue));
    }

    
    //buys SP500 or increases to replace all cash
    private void buyRemainingCore(Date d) {
        if(!holdings.keySet().contains(getBenchmark())){
            swapCores(getHoldingsCore());
        }else{
            for(Security s:holdings.keySet()){
                if(s.name.equals("Cash")){
                    
                    double allocation = holdings.get(s)[0];
                    
                    decreasePosition(s,d,0.0,(Core)getBenchmark());
                    holdings.remove(s);
                    
                    break;
                }
                    
            }
        }
    }
    
    //sells SP500
    private void sellRemainingCore(Date d) {
        for(Security s: holdings.keySet()){
            if(s.name.equals("S&P 500")){
                sell(s);
                break;
            }
            
        }
    }
    
    //Up to two weeks before seasonal end date, if S&P 500 has a sell PnF, then sell an amount that brings the total holdings in portfolio to 50%
    //??? Is it supposed to sell sectors?
    private void sellCoreEarly(Date d) {
        
        double totalSum = 0;
        double sectorSum = 0;
        for(Security s: holdings.keySet()){
            if(s.name.equals("Cash")){continue;}
            totalSum+=100*holdings.get(s)[0]/portfolioValue;
            if(s instanceof Sector){
                sectorSum+=100*holdings.get(s)[0]/portfolioValue;
            }
        }
        
        if(totalSum<=50){
            return;
        }else if(totalSum>50){
            if(sectorSum>=50){
                sellRemainingCore(d);
            }else if(sectorSum<50){
                decreasePosition(getBenchmark(),d,50-sectorSum,(Core)getCash());
            }
        }
        
    }

    //purchase S&P 500 up to 50% equity, subtracting equity sectors
    private void buyCoreEarly(Date d) {
        double totalSum = 0;
        double sectorSum = 0;
        for(Security s: holdings.keySet()){
            if(s.name.equals("Cash")){continue;}
            totalSum+=100*holdings.get(s)[0]/portfolioValue;
            if(s instanceof Sector){
                sectorSum+=100*holdings.get(s)[0]/portfolioValue;
            }
        }
        
        if(totalSum>=50){
            return;
        }else if(totalSum<50){
            if(sectorSum<50){
                increasePosition(getBenchmark(),d,50-sectorSum,(Core)getCash());
                sellRemainingCore(d);
            }
        }
        
    }
    
    //checks if sector sell is triggered
    //O exceedes previous PnF O row
    //Mame variable minimum how many in a Row 
    private boolean sellTriggered(Date d, Security s) throws IOException, ParseException, CloneNotSupportedException {
        PointAndFigure pf = new PointAndFigure("PnF",SeasonalProgram.data.getDataset(s.name),startDate,calendar.getTime(),boxSizePercent,reversalBoxes,signalBoxes);
        return(pf.sellSignal());    
    }
    
    //checks if sector buy is triggered
    //X exceedes previous PnF X row
    //Make variable minimum how many in a Row 
    private boolean buyTriggered(Date d, Security s) throws IOException, CloneNotSupportedException, ParseException {
        PointAndFigure pf = new PointAndFigure("PnF",SeasonalProgram.data.getDataset(s.name),startDate,calendar.getTime(),boxSizePercent,reversalBoxes,signalBoxes);
        return(pf.buySignal());
    }
    
    private void dropLowestSector() throws ParseException, IOException, CloneNotSupportedException{
        ArrayList<Security> topSectors = getTopSectors();
        int worst = 0;
        for(Security s:holdings.keySet()){
            if(topSectors.indexOf(s)>worst){
                worst = topSectors.indexOf(s);
            }
        }
        Security lowestSector = topSectors.get(worst);
        sell(lowestSector);
    }

    @Override
    public void printUpdate(Date d){
        System.out.println("");
        System.out.println(sm.format(d));
        printHoldings();
        System.out.println("Portfolio Value "+round(portfolioValue));
        
        try {
            printMatrix();
        } catch (ParseException ex) {
            Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            printSignals();
        } catch (IOException ex) {
            Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void printMatrix() throws ParseException, IOException, CloneNotSupportedException {
        System.out.println("");
        System.out.println("Matrix (In list if Sector/Benchmark is Buy):");
        HashMap<Security, HashMap<Security, Double>> matrix = makeMatrix();
        if(matrix.size()==0){System.out.println("No Possible Sector Buys");}
        System.out.print("      ");
        for(Security s: matrix.keySet()){
            System.out.print(s.name);
            printSpaces(5-s.name.length());  
            System.out.print("|");
        }
        System.out.println("");
        
        for(Security s: matrix.keySet()){
            System.out.print(s.name);
            printSpaces(5-s.name.length());  
            System.out.print("|");
            for(Security t:matrix.get(s).keySet()){
                System.out.print(matrix.get(s).get(t));
                printSpaces(5-matrix.get(s).get(t).toString().length()); 
                System.out.print("|");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private void printSignals() throws IOException, CloneNotSupportedException, ParseException {
        System.out.println("Signals (Not over Benchmark):");
        for(Security s:securities){
            if(s.name.equals("Cash")){continue;};
            PointAndFigure pf = new PointAndFigure("PnF",SeasonalProgram.data.getDataset(s.name),startDate,calendar.getTime(),boxSizePercent,reversalBoxes,signalBoxes);
            if(pf.buySignal()){
                System.out.println(s.name+": Buy");
            }else if(pf.sellSignal()){
                System.out.println(s.name+": Sell");
            }else{
                System.out.println(s.name+": ---");
            }
        }
        System.out.println("");
    }
    
    
    //All sectors that pass initial buy criteria
    //X exceedes previous PnF X row
    //Sector/SP500, In array
    //Create PnF from this data
    //Brooke to come back to me on that
    public ArrayList<Security> getPossibleBuys() throws IOException, CloneNotSupportedException, ParseException {
        ArrayList<Security> possibleBuyList = new ArrayList<Security>();
        for(Security s: securities){
            if(s instanceof Core){continue;}
            Dataset comparison = SeasonalProgram.data.getDataset(s.name).compareTo(SeasonalProgram.data.getDataset(getBenchmark().name));
            PointAndFigure pf = new PointAndFigure("PnF",comparison,startDate,calendar.getTime(),boxSizePercent,reversalBoxes,signalBoxes);
            if(pf.buySignal()){
                possibleBuyList.add(s);
            }
        }
        return possibleBuyList;
    }

    //compare two sectors based on either PnF or Core performance
    //sector x/y, Counting Xs
    //Brooke to come back to me on that
    private Double compareSectors(Security s, Security t) throws ParseException, IOException, CloneNotSupportedException {
        Dataset comparison = SeasonalProgram.data.getDataset(s.name).compareTo(SeasonalProgram.data.getDataset(t.name));
        PointAndFigure pf = new PointAndFigure("PnF",comparison,startDate,calendar.getTime(),boxSizePercent,reversalBoxes,signalBoxes);
        if(s.equals(t)){
            return 0.0;
        }
        if(pf.buySignal()){
            return 1.0;
        }else if(pf.sellSignal()){
            return -1.0;
        }else{
            return 0.0;
        }
    }
    
    
 
    
    public void printSpaces(int num){
        for(int i = 0;i<num;i++){
            System.out.print(" ");
        }
    }
    
    public void setCoreDates(Calendar c){
        for(Security s:securities){
            if(s instanceof Core){
                setDate(c,s);
            }
        }
    }
    
    @Override
    public void setInitDates(Date dateNow){
        Calendar c = Calendar.getInstance();
        c.setTime(dateNow);
        
        //System.out.println(dateNow);
        for(Security s:securities){
            if(s instanceof Sector){continue;}
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
    
    @Override
    public void printPreTransaction(boolean sell){
        System.out.println("");
        System.out.println(sm.format(convertToTrading(calendar.getTime(),sell)));
        System.out.println("Initial Portfolio");
        printHoldings();
        
        try {
            printMatrix();
        } catch (ParseException ex) {
            Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            printSignals();
        } catch (IOException ex) {
            Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(PnFPortfolio.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Transactions");
    }


}
