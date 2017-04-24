/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author jordandearsley
 */
public class UI extends JFrame {
    public UI(){
        super("Seasonal Program");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        getContentPane().setPreferredSize(new Dimension(400,400));
        
        
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        ModelUI model = new ModelUI();
        tabbedPane.addTab("Model", model);
        
        MonthSummaryUI month = new MonthSummaryUI();
        tabbedPane.addTab("Month Summary", month);
        
        IndividualTradesUI trades = new IndividualTradesUI();
        tabbedPane.addTab("Individual Trades", trades);
        
        getContentPane().add(tabbedPane);
        
        pack();
    }
}
