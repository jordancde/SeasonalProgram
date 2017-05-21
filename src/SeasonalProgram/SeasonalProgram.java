/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;


import SeasonalProgram.OutputTables.MonthlyStaticReturnsTable;
import SeasonalProgram.OutputTables.Table;
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
    public static Model seasonalModel;
    public static Data data;
    public static UI ui;
    public static Portfolio portfolio;
    
    public static void main(String[] args) throws IOException, ParseException {

        data = new Data("input.csv");
        
        //initializes seasonalModel
        ui = new UI();
        ui.setVisible(true);
        

 
    }
    
    public static void runModel() throws IOException{
        
        portfolio = new Portfolio(seasonalModel.getSecurities(),seasonalModel.startDate,seasonalModel.endDate);
        portfolio.runPortfolio();
        
        Map<String, Double> monthlyReturns = portfolio.getMonthlyReturns("Full", false);
        MonthlyStaticReturnsTable fullPortfolio = new MonthlyStaticReturnsTable("Static Seasonal Gains",monthlyReturns,seasonalModel.startDate,seasonalModel.endDate);
        fullPortfolio.writeTable();
        
        Map<String, Double> benchmarkMonthlyReturns = portfolio.getMonthlyReturns("Benchmark", false);
        MonthlyStaticReturnsTable benchmark = new MonthlyStaticReturnsTable("Benchmark Seasonal Gains",benchmarkMonthlyReturns,seasonalModel.startDate,seasonalModel.endDate);
        benchmark.writeTable();
        
        Map<String, Double> relativeBenchmarkMonthlyReturns = portfolio.getMonthlyReturns("Relative Benchmark", false);
        MonthlyStaticReturnsTable relativeBenchmark = new MonthlyStaticReturnsTable("Relative Benchmark Seasonal Gains",relativeBenchmarkMonthlyReturns,seasonalModel.startDate,seasonalModel.endDate);
        relativeBenchmark.writeTable();
        
        Map<String, Double> fqMonthlyReturns = portfolio.getMonthlyReturns("Full", true);
        MonthlyStaticReturnsTable fq = new MonthlyStaticReturnsTable("Seasonal Frequency Pos",fqMonthlyReturns,seasonalModel.startDate,seasonalModel.endDate);
        fq.writeTable();
        
        Map<String, Double> fqBenchmarkMonthlyReturns = portfolio.getMonthlyReturns("Benchmark", true);
        MonthlyStaticReturnsTable fqBenchmark = new MonthlyStaticReturnsTable("Seasonal Benchmark Frequency Pos",fqBenchmarkMonthlyReturns,seasonalModel.startDate,seasonalModel.endDate);
        fqBenchmark.writeTable();
        
        Map<String, Double> fqRelativeBenchmarkMonthlyReturns = portfolio.getMonthlyReturns("Relative Benchmark", true);
        MonthlyStaticReturnsTable fqRelativeBenchmark = new MonthlyStaticReturnsTable("Seasonal Relative Benchmark Frequency Pos",fqRelativeBenchmarkMonthlyReturns,seasonalModel.startDate,seasonalModel.endDate);
        fqRelativeBenchmark.writeTable();
        
        Map<String, Double> cash = portfolio.getMonthlyReturns("Cash", false);
        MonthlyStaticReturnsTable cashTable = new MonthlyStaticReturnsTable("Cash",cash,seasonalModel.startDate,seasonalModel.endDate);
        cashTable.writeTable();
        
        Map<String, Double> trades = portfolio.getMonthlyTrades();
        MonthlyStaticReturnsTable tradeTable = new MonthlyStaticReturnsTable("Number of Trades",trades,seasonalModel.startDate,seasonalModel.endDate);
        tradeTable.writeTable();
        
        ArrayList<Trade> tradeHistory = portfolio.getTrades();
        StaticTradesTable tradeHistoryTable = new StaticTradesTable("Trade History",tradeHistory,seasonalModel.startDate,seasonalModel.endDate);
        tradeHistoryTable.writeTable();
        
    }

    
}
