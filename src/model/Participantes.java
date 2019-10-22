/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import control.Controller;
import utils.Stream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ipdmartins
 */
public class Participantes extends Thread{

    private String ip;
    private int porta;
    private ServerSocket server;
    private String localRegister;
    private Socket localConn;
    private Stream stream;
    private Scanner input;
    private Constant constantes;
    private Controller control;
    private String answer;

    public Participantes(int porta, String ip) {
        this.ip = ip;
        this.porta = porta;
        this.localConn = null;
        this.localRegister = "";
        this.input = new Scanner(System.in);
        this.constantes = new Constant();
        this.control = Controller.getInstance();
    }

    @Override
    public void run() {
        try {
            System.out.println("SERVIDOR INICIADO, AGUARDANDO 10s PARA CONEXÃO");
            localRegister = constantes.INIT;

            server.setSoTimeout(10000);
            localConn = server.accept();
            if (localConn != null) {
                stream = new Stream();
                stream.createStream(localConn);
                System.out.println("CONEXÃO LOCAL EFETUADA");
            } else if (localConn == null) {
                localRegister = constantes.VOTE_ABORT;
                close();
                System.out.println("PARTICIPANTE IP: " + ip + " PORTA: " + porta + " ENCERRADO POR EXCEDER O TEMPO PARA CONEXÃO");
            }

            localConn.setSoTimeout(10000);
            if (stream.readMessage().equalsIgnoreCase(constantes.VOTE_REQUEST)) {
                //VERIFICAR SE NENHUM PARTICIPANTE PEDIU ABORT
                while (true) {
                    System.out.println("Digite (1) para VOTE_COMMIT ou (2) para VOTE_ABORT");
                    answer = input.nextLine();
                    int num = Integer.parseInt(answer);
                    if (num == 1) {
                        localRegister = constantes.VOTE_COMMIT;
                        stream.sendMessage(localRegister);
                        break;
                    } else if (num == 2) {
                        localRegister = constantes.VOTE_ABORT;
                        stream.sendMessage(localRegister);
                        break;
                    } else {
                        System.out.println("Valor digitado invalido");
                    }
                }
                answer = stream.readMessage();
                if (localConn.getSoTimeout() > 0 && answer.equalsIgnoreCase(constantes.GLOBAL_COMMIT)) {
                    localRegister = constantes.GLOBAL_COMMIT;
                } else if (localConn.getSoTimeout() > 0 && answer.equalsIgnoreCase(constantes.GLOBAL_ABORT)) {
                    localRegister = constantes.GLOBAL_ABORT;
                } else if (localConn.getSoTimeout() == 0) {
                    decisionRequest();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Participantes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }
    }

    public void decisionRequest() {
        try {
            stream.sendMessage(constantes.DECISION_REQUEST);
            localRegister = constantes.DECISION_REQUEST;
            answer = stream.readMessage();
            if (answer.equalsIgnoreCase(constantes.GLOBAL_COMMIT)) {
                localRegister = constantes.GLOBAL_COMMIT;
            } else if (answer.equalsIgnoreCase(constantes.GLOBAL_ABORT)) {
                localRegister = constantes.GLOBAL_ABORT;
            }
        } catch (IOException ex) {
            Logger.getLogger(Participantes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {

        try {
            if (server != null) {
                server.close();
            }
            if (localConn != null) {
                localConn.close();
            }
            if (stream != null) {
                stream.closeStream();
            }
        } catch (Exception e) {
            System.err.println("Erro ao fechar Socket/ServerSocket" + e);
        }

    }

    public String getIp() {
        return ip;
    }

    public Socket getLocalConn() {
        return localConn;
    }

    public void setLocalConn(Socket localConn) {
        this.localConn = localConn;
    }

    public int getPorta() {
        return porta;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

}
