/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

/**
 *
 * @author jordandearsley
 */
class Trigger {
    public String name;
    public boolean expOrSim1;
    public int param1;
    public boolean expOrSim2;
    public int param2;

    public Trigger(String name, boolean expOrSim1,int param1, boolean expOrSim2, int param2){
        this.name = name;
        this.expOrSim1 = expOrSim1;
        this.param1 = param1;
        this.expOrSim2 = expOrSim2;
        this.param2 = param2;
    }
}
