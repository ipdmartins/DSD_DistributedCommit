/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author ipdmartins
 */

//ESTA CLASSE CONTÉM OS ESTADOS DOS SISTEMA
public class Constant {

    public final String START_2PC = "START_2PC";
    public final String INIT = "INIT";
    public final String VOTE_REQUEST = "VOTE_REQUEST";
    public final String VOTE_COMMIT = "VOTE_COMMIT";
    public final String VOTE_ABORT = "VOTE_ABORT";
    public final String GLOBAL_COMMIT = "GLOBAL_COMMIT";
    public final String GLOBAL_ABORT = "GLOBAL_ABORT";
    public final String DECISION_REQUEST = "DECISION_REQUEST";
    
}
