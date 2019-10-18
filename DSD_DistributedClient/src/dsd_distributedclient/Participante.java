/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dsd_distributedclient;

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
public class Participante extends Thread{

    private String ip;
    private int porta;
    private ServerSocket server;
    private String localRegister;
    private Socket localConn;
    private Stream stream;
    private Scanner input;
    private Constant constantes;
    private String answer;
    //private Controller control;

    public Participante(int porta, ServerSocket server) {
        this.porta = porta;
        this.server = server;
        this.localConn = null;
        this.localRegister = "";
        this.input = new Scanner(System.in);
        this.constantes = new Constant();
        //this.control = Controller.getInstance();
        //this.control.addObservador(this);
    }

    @Override
    public void run() {
        
        try {
            System.out.println("SERVIDOR INICIADO, AGUARDANDO PARA CONEXÃO");
            localRegister = constantes.INIT;

            server.setSoTimeout(30000);
            localConn = server.accept();
            if (localConn != null) {
                stream = new Stream();
                stream.createStream(localConn);
                System.out.println("CONEXÃO LOCAL EFETUADA");
            } else if (localConn == null) {
                localRegister = constantes.VOTE_ABORT;
                close();
                System.out.println("PARTICIPANTE IP: " + ip + " PORTA: " + porta + " ENCERRADO POR EXCEDER O TEMPO PARA CONEXÃO");
                return;
            }

            localConn.setSoTimeout(10000);
            answer = stream.readMessage();
            if (stream.readMessage().equalsIgnoreCase(constantes.VOTE_REQUEST)) {
                while (true) {
                    System.out.println("VOTAÇÃO PARA DEFINIR O PREÇO DE UM LITRO DE LEITE");
                    System.out.println("COORDENADOR DIZ QUE O PREÇO É :"+answer);
                    System.out.println("Digite (1) para CONCORDAR ou (2) para DISCORDAR");
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
                    System.out.println("GLOBAL COMMIT, todos concordaram");
                } else if (localConn.getSoTimeout() > 0 && answer.equalsIgnoreCase(constantes.GLOBAL_ABORT)) {
                    localRegister = constantes.GLOBAL_ABORT;
                    System.out.println("GLOBAL ABORT, houve discordância");
                } else if (localConn.getSoTimeout() == 0) {
                    System.out.println("Tempo de espera excedido, consultar outros participantes");
                    decisionRequest();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Participante.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Participante.class.getName()).log(Level.SEVERE, null, ex);
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

    public void setIp(String ip) {
        this.ip = ip;
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

}
