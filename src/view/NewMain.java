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
        Integer [] portasLab = {56000, 56001, 56002, 56003, 56004};
        String [] ipsLAB = {"10.60.185.58", "10.60.185.59","10.60.185.57","10.60.185.56", "10.60.185.50"};
        int numeroParticipantes = ipsLAB.length;
        
        Coordinator coord = new Coordinator();
        coord.setIpsParticipantes(ipsLAB);
        coord.setPortasPartic(portasLab);
        Controller controle = Controller.getInstance();
        controle.setCoord(coord);
        //controle.innit();
                
        String ipTeste = "10.60.185.58";
        int portaTeste = 56000;
        controle.teste(ipTeste, portaTeste);
    }
    
}
