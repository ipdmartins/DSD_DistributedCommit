/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import utils.Stream;
import model.Constant;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Coordinator;

/**
 *
 * @author ipdmartins
 */
public class RequestManager extends Thread {

    private Stream stream;
    private Constant constantes;
    private Coordinator coord;
    private String response;
    private Controller control;
    
    public RequestManager(Stream stream, Coordinator coord) {
        this.stream = stream;
        this.coord = coord;
        this.constantes = new Constant();
        this.response = "";
        this.control = Controller.getInstance();
    }

    @Override
    public void run() {
        try {
            response = stream.readMessage();
            if (!response.equalsIgnoreCase("")) {
                if (response.equalsIgnoreCase(constantes.VOTE_COMMIT)) {
                    coord.addVote(true);
                } else if (response.equalsIgnoreCase(constantes.VOTE_ABORT)) {
                    coord.addVote(false);
                } else if (response.equalsIgnoreCase(constantes.DECISION_REQUEST)) {
                    control.multiCastExecutor(7, stream.getPortaParticipante());
                }
            }else{
                System.out.println("Resposta inválida");
            }
        } catch (SocketException ex) {
            Logger.getLogger(RequestManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RequestManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
