/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ipdmartins
 */
//ESTA CLASSE SERVE PARA CONECTAR EM UM HOST E PORTA, CASO POSITO RETORNA A CONEXÃO, SE NEGATIVO NULO
public class SocketConnection {

    private Socket conn;

    public Socket createConnection(String host, int porta) {
        try {

            this.conn = new Socket(host, porta);

        } catch (IOException ex) {
            Logger.getLogger(SocketConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (conn.isConnected()) {
            return conn;
        }
        return null;
    }
    
    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("Erro ao fechar Socket" + e);
        }
    }

}
