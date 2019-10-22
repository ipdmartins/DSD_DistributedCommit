/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.IOException;
import java.net.Socket;
import utils.Stream;
import model.Constant;
import model.Coordinator;
import utils.SocketConnection;

/**
 *
 * @author ipdmartins
 */
public class Controller {

    private Coordinator coord;
    private SocketConnection conn;
    private Constant constantes;
    private Stream stream;
    private RequestManager manager;
    private static Controller instance;

    private String IPTESTE;
    private Socket socketTESTE;

    private Controller() {
        this.conn = new SocketConnection();
        this.constantes = new Constant();
    }

    public synchronized static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void innit() {
        try {
            coord.setLocalRegister(constantes.START_2PC);
            //(2) conecta com o participante
            multiCastExecutor(2);
            //(3) enviar multicast VOTEREQUEST a todos
            multiCastExecutor(3);
            manageRequets();
        } catch (IOException ex) {
            System.err.println("ERRO NO INNIT" + ex);
        } catch (InterruptedException ex) {
            System.err.println("ERRO NO INNIT " + ex);
        }
    }

    public void teste(String ip, int porta) throws IOException {

        socketTESTE = conn.createConnection(ip, porta);
        stream = new Stream();
        stream.createStream(socketTESTE);
        stream.sendMessage("R$ 2,09");
        stream.sendMessage(constantes.VOTE_REQUEST);
        String res = stream.readMessage();
        if (res.equalsIgnoreCase(constantes.VOTE_COMMIT)) {
            stream.sendMessage(constantes.GLOBAL_COMMIT);
        } else if (res.equalsIgnoreCase(constantes.VOTE_ABORT)) {
            stream.sendMessage(constantes.GLOBAL_ABORT);
        }
        
    }

    public void multiCastExecutor(int option) {
        try {
            for (int i = 0; i < coord.getIpsParticipantes().size(); i++) {
                switch (option) {
                    case 2:
                        System.out.println("INICIANDO CONEXÃO: " + i);
                        coord.addConnection(conn.createConnection(coord.getIpsParticipantes().get(i),
                                coord.getPortasPartic().get(i)));
                        if (coord.getConnectionList().get(i) != null) {
                            stream = new Stream();
                            stream.createStream(coord.getConnectionList().get(i));
                            coord.addStrem(stream);
                        } else {
                            System.out.println("CONEXÃO " + i + " NÃO EFETUADA");
                        }
                        break;
                    case 3:
                        manager = new RequestManager(coord.getStreamList().get(i), coord);
                        coord.getStreamList().get(i).sendMessage("R$ 2,09");
                        coord.getStreamList().get(i).sendMessage(constantes.VOTE_REQUEST);
                        manager.start();
                        System.out.println("VOTE REQUEST: " + i);
                        break;
                    case 4:
                        coord.getStreamList().get(i).sendMessage(constantes.GLOBAL_ABORT);
                        break;
                    case 5:
                        coord.getStreamList().get(i).sendMessage(constantes.GLOBAL_COMMIT);
                        break;
                    case 6:
                        coord.getConnectionList().get(i).close();
                        coord.getStreamList().get(i).closeStream();
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("ERRO NO MULTICAST" + e);
        }
    }

    public void manageRequets() throws InterruptedException, IOException {
        int counter = 25000;
        while (counter > 0) {
            if (coord.getVotesList().size() == coord.getIpsParticipantes().size()) {
                for (int i = 0; i < coord.getVotesList().size(); i++) {
                    if (coord.getVotesList().get(i) == false) {
                        coord.setLocalRegister(constantes.GLOBAL_ABORT);
                        coord.addVote(false);
                        //(4) multicast GLOBAL ABORT
                        multiCastExecutor(4);
                        return;
                    }
                }
                coord.setLocalRegister(constantes.GLOBAL_COMMIT);
                coord.addVote(true);
                //(5) multicast GLOBAL_COMMIT
                multiCastExecutor(5);
                break;
            }
            wait(1000);
            counter -= 1000;
        }
        if (counter == 0) {
            coord.setLocalRegister(constantes.GLOBAL_ABORT);
            coord.addVote(false);
            //(4) multicast GLOBAL_ABORT
            multiCastExecutor(4);
            //(6) close connection e close stream
            multiCastExecutor(6);
        }
    }

    public void setCoord(Coordinator coord) {
        this.coord = coord;
    }

}
