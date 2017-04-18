/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.io.IOException;
import java.text.ParseException;



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
    
}
