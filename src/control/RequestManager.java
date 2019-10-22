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
import model.Coordinator;
import utils.SocketConnection;

/**
 *
 * @author ipdmartins
 */
public class RequestManager extends Thread {

    private Stream stream;
    private Constant constantes;
    private Coordinator coord;
    private String response;
    private SocketConnection conector;

    public RequestManager(Stream stream, Coordinator coord) {
        this.stream = stream;
        this.coord = coord;
        this.constantes = new Constant();
        this.response = "";
        this.conector = new SocketConnection();
    }

    @Override
    public void run() {
        try {
            while (true) {
                response = stream.readMessage();

                if (!response.equalsIgnoreCase("")) {
                    if (response.equalsIgnoreCase(constantes.VOTE_COMMIT)) {
                        coord.addVote(true);
                    } else if (response.equalsIgnoreCase(constantes.VOTE_ABORT)) {
                        coord.addVote(false);
                    }
                } else {
                    System.out.println("Resposta inválida");
                }
            }
        } catch (SocketException ex) {
            System.err.println("ERRO NO REQUEST MANAGER " + ex);
        } catch (IOException ex) {
            System.err.println("ERRO NO REQUEST MANAGER " + ex);
        }
    }

}
