/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.daodanhmuc;
import dto.dtodanhmuc;

import java.util.ArrayList;

/**
 *
 * @author Hieu PC
 */
public class busdanhmuc {
    daodanhmuc daodm = new daodanhmuc();
    // get
    
    public ArrayList<dtodanhmuc> getlist(){
        return daodm.getlist();
    }
}
