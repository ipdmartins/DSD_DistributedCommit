
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import control.Participante;
import control.Stream;
import model.Registro;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author ipdmartins
 */
public class DSD_DistributedClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Integer[] portasLab = {56000, 56001, 56002, 56003, 56004};
        String[] ipsLAB = {"10.60.185.58", "10.60.185.59", "10.60.185.57", "10.60.185.56", "10.60.185.50"};
        Scanner input = new Scanner(System.in);

        int portaLab = 56000;
        String tipoSistema = "teste";//teste ou real

        System.out.println("DEFINA UM TEMPO DE ESPERA DO SISTEMA EM MILISEGUNDOS");
        int tempo = input.nextInt();

        Registro registro = new Registro();
        ServerSocket server = null;
        try {
            server = new ServerSocket(portaLab);
            server.setReuseAddress(true);
            server.setSoTimeout(tempo);
            System.out.println("SERVIDOR INICIADO, AGUARDANDO PARA CONEXÃO " + tempo + " SEGUNDOS");
            Socket con = server.accept();
            System.out.println("CONEXÃO LOCAL EFETUADA");
            Participante part = new Participante(con, registro, tempo, tipoSistema);
            part.setIpList(ipsLAB);
            part.setPortasList(portasLab);
            part.start();

            while (true) {
                System.out.println("SERVIDOR PRONTO PARA OUTRA CONEXÃO");
                Socket conn = server.accept();
                System.out.println("NOVA CONEXÃO EFETUADA");
                Stream str = new Stream();
                str.createStream(conn);
                if (str.readMessage().equalsIgnoreCase("DECISION_REQUEST")) {
                    str.sendMessage(registro.getLocalRegister());
                } else {
                    System.out.println("SOLICITAÇÃO INVÁLIDA");
                }
                conn.close();
                str.closeStream();
            }

        } catch (Exception e) {
            System.err.println("TEMPO DE CONEXÃO EXCEDIDA NO MAIN " + e);
        } finally {
            try {
                server.close();
            } catch (IOException ex) {
                System.err.println("TEMPO DE CONEXÃO EXCEDIDA NO MAIN " + ex);
            }
        }

    }

}
