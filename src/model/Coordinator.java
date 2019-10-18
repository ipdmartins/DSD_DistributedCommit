/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import utils.Stream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ipdmartins
 */
public class Coordinator {
    
    private List <String> ipsParticipantes;
    private List <Integer> portasPartic;
    private List<Socket> connectionList;
    private List<Stream> streamList;
    private List<Boolean> votesList;
    private String localRegister;

    public Coordinator() {
        this.connectionList = new ArrayList<>();
        this.streamList = new ArrayList<>();
        this.votesList = new ArrayList<>();
    }
    
    public void addVote(boolean vote) {
        votesList.add(vote);
    }
    
    public void addConnection(Socket socket) {
        connectionList.add(socket);
    }
    
    public void addStrem(Stream stream) {
        streamList.add(stream);
    }
    
    public List<Socket> getConnectionList() {
        return connectionList;
    }

    public List<Stream> getStreamList() {
        return streamList;
    }

    public List<Boolean> getVotesList() {
        return votesList;
    }

    public String getLocalRegister() {
        return localRegister;
    }

    public void setLocalRegister(String localRegister) {
        this.localRegister = localRegister;
    }

    public List<String> getIpsParticipantes() {
        return ipsParticipantes;
    }

    public void setIpsParticipantes(String [] ipsParticipantes) {
        this.ipsParticipantes = Arrays.asList(ipsParticipantes) ;
    }

    public List<Integer> getPortasPartic() {
        return portasPartic;
    }

    public void setPortasPartic(Integer [] portasPartic) {
        this.portasPartic = Arrays.asList(portasPartic);
    }

    
}
