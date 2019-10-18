/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsd_distributedclient;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author ipdmartins
 */
public class DSD_DistributedClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        int portaLab = 56000;
        
        ServerSocket server = new ServerSocket(portaLab);
        Participante part = new Participante(portaLab, server);
        part.start();
    }

}
