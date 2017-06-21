/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;


import SeasonalProgram.OutputTables.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;



/**
 *
 * @author jordandearsley
 */
public class SeasonalProgram {

    /**
     * @param args the command line arguments
     */
    public static StaticModel staticModel;
    public static RSModel RSModel;
    public static IndividualModel individualTradeModel;
    
    public static Data data;
    public static UI ui;
    public static Portfolio portfolio;
    public static PnFPortfolio PnFportfolio;
    public static IndividualTradePortfolio tradeportfolio;
    
    
    public static void main(String[] args) throws IOException, ParseException {

        data = new Data("input.csv");
        
        //initializes staticModel
        ui = new UI();
        ui.setVisible(true);
        
        
 
    }
    
    public static void runIndividualTrade() throws IOException{
        IndividualTradePortfolio tradeportfolio = new IndividualTradePortfolio(individualTradeModel);
        tradeportfolio.runPortfolio();
        
        //NAME OF TABLE HAS TO BE NAME OF SECTOR
        IndividualTable individualTable = new IndividualTable(tradeportfolio.sector.name,individualTradeModel.startDate,individualTradeModel.endDate,tradeportfolio.map);
        individualTable.writeTable();
    }
    
    public static void runRSModel() throws IOException{
        PnFportfolio = new PnFPortfolio(RSModel.getSecurities(),RSModel.startDate,RSModel.endDate,RSModel.boxSizePercent,RSModel.reversalBoxes,RSModel.signalBoxes, RSModel.minCoreAllocation);
        PnFportfolio.runPortfolio();
        
        Map<String, Double[]> monthlyReturns = PnFportfolio.getReturns("Full", false);
        MonthlyStaticTable fullPortfolio = new MonthlyStaticTable("RS Portfolio % Monthly Gains",monthlyReturns,RSModel.startDate,RSModel.endDate,0);
        fullPortfolio.writeTable();
        
        Map<String, Double[]> benchmarkMonthlyReturns = PnFportfolio.getReturns("Benchmark", false);
        MonthlyStaticTable benchmark = new MonthlyStaticTable("Benchmark Gains",benchmarkMonthlyReturns,RSModel.startDate,RSModel.endDate,1);
        benchmark.writeTable();
        
        Map<String, Double[]> relativeBenchmarkMonthlyReturns = PnFportfolio.getReturns("Relative Benchmark", false);
        MonthlyStaticTable relativeBenchmark = new MonthlyStaticTable("RS Portfolio minus Benchmark",relativeBenchmarkMonthlyReturns,RSModel.startDate,RSModel.endDate,2);
        relativeBenchmark.writeTable();
        
        Map<String, Double[]> fqMonthlyReturns = PnFportfolio.getReturns("Full", true);
        MonthlyStaticTable fq = new MonthlyStaticTable("RS Frequency Pos",fqMonthlyReturns,RSModel.startDate,RSModel.endDate,3);
        fq.writeTable();
        
        Map<String, Double[]> fqBenchmarkMonthlyReturns = PnFportfolio.getReturns("Benchmark", true);
        MonthlyStaticTable fqBenchmark = new MonthlyStaticTable("RS Frequency Pos",fqBenchmarkMonthlyReturns,RSModel.startDate,RSModel.endDate,4);
        fqBenchmark.writeTable();
        
        Map<String, Double[]> fqRelativeBenchmarkMonthlyReturns = PnFportfolio.getReturns("Relative Benchmark", true);
        MonthlyStaticTable fqRelativeBenchmark = new MonthlyStaticTable("RS Portfolio minus Benchmark Frequency Pos",fqRelativeBenchmarkMonthlyReturns,RSModel.startDate,RSModel.endDate,5);
        fqRelativeBenchmark.writeTable();
        
        Map<String, Double[]> cash = PnFportfolio.getReturns("Cash", false);
        MonthlyStaticTable cashTable = new MonthlyStaticTable("RS Portfolio Avg % Cash",cash,RSModel.startDate,RSModel.endDate,6);
        cashTable.writeTable();
        
        Map<String, Double[]> trades = PnFportfolio.getMonthlyTrades();
        MonthlyStaticTable tradeTable = new MonthlyStaticTable("RS Portfolio Number of Trades",trades,RSModel.startDate,RSModel.endDate,7);
        tradeTable.writeTable();

    }
    
    public static void runStaticModel() throws IOException, CloneNotSupportedException, ParseException{
        
        portfolio = new Portfolio(staticModel.getSecurities(),staticModel.startDate,staticModel.endDate);
        portfolio.runPortfolio();
        
        Map<String, Double[]> monthlyReturns = portfolio.getReturns("Full", false);
        MonthlyStaticTable fullPortfolio = new MonthlyStaticTable("Seasonal Portfolio % Monthly Gains",monthlyReturns,staticModel.startDate,staticModel.endDate,0);
        fullPortfolio.writeTable();
        
        Map<String, Double[]> benchmarkMonthlyReturns = portfolio.getReturns("Benchmark", false);
        MonthlyStaticTable benchmark = new MonthlyStaticTable("Benchmark Gains",benchmarkMonthlyReturns,staticModel.startDate,staticModel.endDate,1);
        benchmark.writeTable();
        
        Map<String, Double[]> relativeBenchmarkMonthlyReturns = portfolio.getReturns("Relative Benchmark", false);
        MonthlyStaticTable relativeBenchmark = new MonthlyStaticTable("Seasonal Portfolio minus Benchmark",relativeBenchmarkMonthlyReturns,staticModel.startDate,staticModel.endDate,2);
        relativeBenchmark.writeTable();
        
        Map<String, Double[]> fqMonthlyReturns = portfolio.getReturns("Full", true);
        MonthlyStaticTable fq = new MonthlyStaticTable("Seasonal Frequency Pos",fqMonthlyReturns,staticModel.startDate,staticModel.endDate,3);
        fq.writeTable();
        
        Map<String, Double[]> fqBenchmarkMonthlyReturns = portfolio.getReturns("Benchmark", true);
        MonthlyStaticTable fqBenchmark = new MonthlyStaticTable("Benchmark Frequency Pos",fqBenchmarkMonthlyReturns,staticModel.startDate,staticModel.endDate,4);
        fqBenchmark.writeTable();
        
        Map<String, Double[]> fqRelativeBenchmarkMonthlyReturns = portfolio.getReturns("Relative Benchmark", true);
        MonthlyStaticTable fqRelativeBenchmark = new MonthlyStaticTable("Seasonal Portfolio minus Benchmark Frequency Pos",fqRelativeBenchmarkMonthlyReturns,staticModel.startDate,staticModel.endDate,5);
        fqRelativeBenchmark.writeTable();
        
        Map<String, Double[]> cash = portfolio.getReturns("Cash", false);
        MonthlyStaticTable cashTable = new MonthlyStaticTable("Seasonal Portfolio Avg % Cash",cash,staticModel.startDate,staticModel.endDate,6);
        cashTable.writeTable();
        
        Map<String, Double[]> trades = portfolio.getMonthlyTrades();
        MonthlyStaticTable tradeTable = new MonthlyStaticTable("Seasonal Portfolio Number of Trades",trades,staticModel.startDate,staticModel.endDate,7);
        tradeTable.writeTable();
        
        ArrayList<Trade> tradeHistory = portfolio.getTrades();
        StaticTradesTable tradeHistoryTable = new StaticTradesTable("Trade History",tradeHistory,staticModel.startDate,staticModel.endDate);
        tradeHistoryTable.writeTable();

        

        
    }

    
}
