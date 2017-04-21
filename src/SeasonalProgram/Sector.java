/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SeasonalProgram;

import java.util.Date;

/**
 *
 * @author jordandearsley
 */
class Sector extends Security{
    public Sector(String name, Date buyDate, Date sellDate, double allocation, double leverage){
        super(name, buyDate, sellDate, allocation);
    }
}
