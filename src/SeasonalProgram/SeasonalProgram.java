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
    
    public static void runModel() throws IOException, CloneNotSupportedException, ParseException{
        
        portfolio = new Portfolio(seasonalModel.getSecurities(),seasonalModel.startDate,seasonalModel.endDate);
        portfolio.runPortfolio();
        
        Map<String, Double[]> monthlyReturns = portfolio.getReturns("Full", false);
        MonthlyStaticTable fullPortfolio = new MonthlyStaticTable("Seasonal Portfolio % Monthly Gains",monthlyReturns,seasonalModel.startDate,seasonalModel.endDate,0);
        fullPortfolio.writeTable();
        
        Map<String, Double[]> benchmarkMonthlyReturns = portfolio.getReturns("Benchmark", false);
        MonthlyStaticTable benchmark = new MonthlyStaticTable("Benchmark Gains",benchmarkMonthlyReturns,seasonalModel.startDate,seasonalModel.endDate,1);
        benchmark.writeTable();
        
        Map<String, Double[]> relativeBenchmarkMonthlyReturns = portfolio.getReturns("Relative Benchmark", false);
        MonthlyStaticTable relativeBenchmark = new MonthlyStaticTable("Seasonal Portfolio minus Benchmark",relativeBenchmarkMonthlyReturns,seasonalModel.startDate,seasonalModel.endDate,2);
        relativeBenchmark.writeTable();
        
        Map<String, Double[]> fqMonthlyReturns = portfolio.getReturns("Full", true);
        MonthlyStaticTable fq = new MonthlyStaticTable("Seasonal Frequency Pos",fqMonthlyReturns,seasonalModel.startDate,seasonalModel.endDate,3);
        fq.writeTable();
        
        Map<String, Double[]> fqBenchmarkMonthlyReturns = portfolio.getReturns("Benchmark", true);
        MonthlyStaticTable fqBenchmark = new MonthlyStaticTable("Benchmark Frequency Pos",fqBenchmarkMonthlyReturns,seasonalModel.startDate,seasonalModel.endDate,4);
        fqBenchmark.writeTable();
        
        Map<String, Double[]> fqRelativeBenchmarkMonthlyReturns = portfolio.getReturns("Relative Benchmark", true);
        MonthlyStaticTable fqRelativeBenchmark = new MonthlyStaticTable("Seasonal Portfolio minus Benchmark Frequency Pos",fqRelativeBenchmarkMonthlyReturns,seasonalModel.startDate,seasonalModel.endDate,5);
        fqRelativeBenchmark.writeTable();
        
        Map<String, Double[]> cash = portfolio.getReturns("Cash", false);
        MonthlyStaticTable cashTable = new MonthlyStaticTable("Seasonal Portfolio Avg % Cash",cash,seasonalModel.startDate,seasonalModel.endDate,6);
        cashTable.writeTable();
        
        Map<String, Double[]> trades = portfolio.getMonthlyTrades();
        MonthlyStaticTable tradeTable = new MonthlyStaticTable("Seasonal Portfolio Number of Trades",trades,seasonalModel.startDate,seasonalModel.endDate,7);
        tradeTable.writeTable();
        
        ArrayList<Trade> tradeHistory = portfolio.getTrades();
        StaticTradesTable tradeHistoryTable = new StaticTradesTable("Trade History",tradeHistory,seasonalModel.startDate,seasonalModel.endDate);
        tradeHistoryTable.writeTable();
        
        ArrayList<Double[]> boxSizes = new ArrayList<Double[]>();
        boxSizes.add(new Double[]{0.0,3000.0,50.0});
        
        for(Trigger t:seasonalModel.triggers){
            if(t.name.equals("PnF")){
                PointAndFigure pf = new PointAndFigure("PNF",data.getDataset("S&P 500"),seasonalModel.startDate,seasonalModel.endDate,boxSizes,seasonalModel.triggers.get(0).param1);
            }
        }
        
    }

    
}
