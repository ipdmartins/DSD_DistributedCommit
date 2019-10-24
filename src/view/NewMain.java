/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import control.Controller;
import java.io.IOException;
import model.Coordinator;

/**
 *
 * @author ipdmartins
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        System.out.println("NOW");
        
        Integer[] portasLab = {56001, 56002, 56003, 56004, 56005};
        String[] ipsLAB = {"10.60.185.67", "10.60.185.52", "10.60.185.63","10.60.185.62","10.60.185.55"};
        int numeroParticipantes = ipsLAB.length;
        
        Coordinator coord = new Coordinator();
        coord.setIpsParticipantes(ipsLAB);
        coord.setPortasPartic(portasLab);
        Controller controle = Controller.getInstance();
        
        controle.setCoord(coord);
        controle.innit();
                      
    }
    
}
