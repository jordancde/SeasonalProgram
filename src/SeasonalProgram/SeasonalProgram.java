/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;


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
    
    public static void runModel(){
        
        portfolio = new Portfolio(seasonalModel.getSecurities(),seasonalModel.startDate,seasonalModel.endDate);
        portfolio.runPortfolio();
        Map<Date, Double> returns = portfolio.getReturns();
    }
    
}
