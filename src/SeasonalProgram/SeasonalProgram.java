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
    public static void main(String[] args) throws IOException, ParseException {
        UI ui = new UI();
        ui.setVisible(true);
        
        Data d = new Data("input.csv");
    }
    
}
