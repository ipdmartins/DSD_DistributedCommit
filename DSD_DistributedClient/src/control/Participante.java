/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import model.Constant;
import model.Registro;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author ipdmartins
 */
public class Participante extends Thread {

    private List<String> ipList;
    private List<Integer> portasList;
    private String tipoSistema;
    private int tempo;
    private Registro registro;
    private ServerSocket server;
    private Socket localConn;
    private String answer;
    private Stream stream;
    private Scanner input;
    private Constant constantes;

    public Participante(Socket socket, Registro register, int num, String tipo) throws IOException {
        this.localConn = socket;
        this.registro = register;
        this.tempo = num;
        this.input = new Scanner(System.in);
        this.constantes = new Constant();
        this.stream = new Stream();
        this.stream.createStream(localConn);
        this.answer = "";
        this.registro.setLocalRegister(constantes.INIT);
        this.tipoSistema = tipo;
    }

    @Override
    public void run() {
        try {
            localConn.setSoTimeout(tempo);
            String message = "";
            while (true) {
                System.out.println("TEMPO PARA PROCESSAR RESPOSTA: " + tempo + " MILISEGUNDOS");
                answer = stream.readMessage();

                //expera vote request
                if (answer.equalsIgnoreCase(constantes.VOTE_REQUEST)) {
                    while (true) {
                        System.out.println("VOTAÇÃO PARA DEFINIR O PREÇO DE UM LITRO DE LEITE");
                        System.out.println("COORDENADOR DIZ QUE O PREÇO É :" + message);
                        System.out.println("Digite (1) para CONCORDAR ou (2) para DISCORDAR");
                        answer = input.nextLine();
                        int num = Integer.parseInt(answer);
                        if (num == 1) {
                            registro.setLocalRegister(constantes.VOTE_COMMIT);
                            stream.sendMessage(constantes.VOTE_COMMIT);
                            System.out.println("Recebido VOTE_COMMIT");
                            break;
                        } else if (num == 2) {
                            registro.setLocalRegister(constantes.VOTE_ABORT);
                            stream.sendMessage(constantes.VOTE_ABORT);
                            System.out.println("Recebido VOTE_ABORT");
                            break;
                        } else {
                            System.out.println("Valor digitado invalido");
                        }
                    }
                    //espera decisão global
                } else if (answer.equalsIgnoreCase(constantes.GLOBAL_COMMIT)) {
                    registro.setLocalRegister(constantes.GLOBAL_COMMIT);
                    System.out.println("Recebido GLOBAL_COMMIT");
                    break;
                } else if (answer.equalsIgnoreCase(constantes.GLOBAL_ABORT)) {
                    registro.setLocalRegister(constantes.GLOBAL_ABORT);
                    System.out.println("Recebido GLOBAL_ABORT");
                    break;
                } else {
                    message = answer;
                }
            }
        } catch (java.net.SocketTimeoutException ex) {
            if (answer.equalsIgnoreCase("") && localConn != null) {
                registro.setLocalRegister(constantes.VOTE_ABORT);
                try {
                    stream.sendMessage(constantes.VOTE_ABORT);
                } catch (IOException ex1) {
                    System.err.println("ERRO ENVIO MSG AO COORDENADOR " + ex1);
                }
                close();
                System.out.println("PARTICIPANTE ENCERRADO POR EXCEDER O TEMPO LIMITE");
                return;
            } else {
                System.out.println("TEMPO EXCEDIDO, FAZENDO DECISION REQUEST");
                decisionRequest();
            }
        } catch (IOException ex) {
            System.err.println("ERRO NO RUN " + ex);
        }
    }

    public void decisionRequest() {
        String response = "";
        Stream str = null;
        try {
            if (tipoSistema.equalsIgnoreCase("real")) {
                for (int j = 0; j < ipList.size(); j++) {
                    str = new Stream();
                    Socket conn = new Socket(ipList.get(j), portasList.get(j));
                    str.createStream(conn);
                    sleep(1000);
                    str.sendMessage(constantes.DECISION_REQUEST);
                }
                registro.setLocalRegister(constantes.DECISION_REQUEST);
                System.out.println("CONECTADOS PARTICIPANTES E ENVIADOS DECISION_REQUEST");
            } else if (tipoSistema.equalsIgnoreCase("teste")) {
                stream.sendMessage(constantes.DECISION_REQUEST);
            }

            response = str.readMessage();
            if (response.equalsIgnoreCase(constantes.GLOBAL_COMMIT)) {
                registro.setLocalRegister(constantes.GLOBAL_COMMIT);
                System.out.println("Recebido GLOBAL_COMMIT");
            } else if (response.equalsIgnoreCase(constantes.GLOBAL_ABORT)) {
                registro.setLocalRegister(constantes.GLOBAL_ABORT);
                System.out.println("Recebido GLOBAL_ABORT");
            }
        } catch (IOException ex) {
            System.err.println("ERRO NO DECISION REQUEST " + ex);
        } catch (InterruptedException ex) {
            System.err.println("ERRO NO DECISION REQUEST " + ex);
        } finally {
            close();
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

    public Socket getLocalConn() {
        return localConn;
    }

    public void setLocalConn(Socket localConn) {
        this.localConn = localConn;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setIpList(String[] ipsLAB) {
        this.ipList = Arrays.asList(ipsLAB);
    }

    public void setPortasList(Integer[] portasLab) {
        this.portasList = Arrays.asList(portasLab);
    }

}
